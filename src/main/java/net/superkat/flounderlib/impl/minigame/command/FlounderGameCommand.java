package net.superkat.flounderlib.impl.minigame.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.minigame.v1.FlounderApi;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.util.FlounderGameStartResult;
import net.superkat.flounderlib.impl.minigame.game.FlounderGameManager;
import net.superkat.flounderlib.impl.minigame.game.FlounderRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * I sorta strongly dislike this class, but at least the user facing result is sorta decent
 */
public class FlounderGameCommand {

    public static final Map<Identifier, ArgumentBuilder<CommandSourceStack, ?>> MINIGAME_AUTOFILLS = new HashMap<>();

    public static final SuggestionProvider<CommandSourceStack> AVAILABLE_MINIGAME_IDS = SuggestionProviders.register(
            Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "available_minigame_ids"),
            (commandContext, suggestionsBuilder) -> {
                Set<Identifier> idSet = FlounderRegistry.getRegistry().keySet();
                return SharedSuggestionProvider.suggestResource(idSet, suggestionsBuilder);
            }
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(
                Commands.literal("flounderlib")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .then(Commands.literal("minigames")
                                .then(listCommand())
                                .then(locateCommand())
                                .then(highlightCommand())
                                .then(startCommand(registryAccess))
                                .then(stopCommand())
                                .then(stopAllCommand())
                        )
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> listCommand() {
        return Commands.literal("list")
                .executes(context -> executeList(context.getSource()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> locateCommand() {
        return Commands.literal("locate")
                .executes(context -> executeLocate(context.getSource(), BlockPos.containing(context.getSource().getPosition()), true))
                .then(Commands.argument("position", BlockPosArgument.blockPos())
                        .executes(context -> executeLocate(context.getSource(), BlockPosArgument.getBlockPos(context, "position"), false))
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> highlightCommand() {
        return Commands.literal("highlight")
                .executes(context -> executeHighlight(context.getSource(), -1))
                .then(Commands.argument("minigameIntId", IntegerArgumentType.integer(0))
                        .executes(context -> executeHighlight(context.getSource(), IntegerArgumentType.getInteger(context, "minigameIntId")))
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> startCommand(CommandBuildContext registryAccess) {
        return forAllMinigames(registryAccess, "start");
    }

    private static LiteralArgumentBuilder<CommandSourceStack> forAllMinigames(CommandBuildContext registryAccess, String name) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(name);

        for (Identifier minigameId : FlounderRegistry.getRegistry().keySet()) {
            if(MINIGAME_AUTOFILLS.containsKey(minigameId)) {
                command.then(
                        Commands.literal(minigameId.toString())
                                .then(MINIGAME_AUTOFILLS.get(minigameId))
                );
            } else {
                command.then(
                        Commands.argument("minigame", FlounderMinigameArgumentType.flounderGame(registryAccess))
                                .suggests(AVAILABLE_MINIGAME_IDS)
                                .executes(context -> executeStart(context.getSource(), FlounderMinigameArgumentType.getFlounderGame(context, "minigame")))
                );
            }
            command.then(Commands.literal(minigameId.toString()));
        }

        return command;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> stopCommand() {
        return Commands.literal("stop")
                .then(Commands.argument("minigameIntId", IntegerArgumentType.integer(0))
                        .executes(context -> executeStop(context.getSource(), IntegerArgumentType.getInteger(context, "minigameIntId")))
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> stopAllCommand() {
        return Commands.literal("stopAll")
                .executes(context -> executeStopAll(context.getSource()));
    }



    public static int executeList(CommandSourceStack source) {
        ServerLevel world = source.getLevel();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            return noMinigameActive(source);
        }

        listMinigames(source, games.values());
        return 1;
    }

    public static int executeLocate(CommandSourceStack source, BlockPos searchPos, boolean playerLocation) {
        ServerLevel world = source.getLevel();

        List<FlounderableGame> games = FlounderApi.findMinigamesAt(world, searchPos);
        Component locationText = playerLocation ? Component.translatable("command.flounderlib.player_location") : getBlockPosText(searchPos, false);
        if(games == null || games.isEmpty()) {
            return noMinigameActive(source, Component.translatable("command.flounderlib.found_fail", locationText));
        }

        locationText = Component.literal(" ").append(locationText); // cursed
        listMinigames(source, games, locationText);
        return 1;
    }

    public static int executeHighlight(CommandSourceStack source, int highlightedMinigame) throws CommandSyntaxException {
        ServerLevel world = source.getLevel();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(highlightedMinigame == -1) {
            if(games == null || games.isEmpty()) {
                return noMinigameActive(source);
            }

            for (FlounderableGame game : games.values()) {
                highlightMinigame(source, game);
            }
        } else {
            FlounderableGame game = games.get(highlightedMinigame);
            if(game == null) {
                return noMinigameActive(source, highlightedMinigame);
            }

            highlightMinigame(source, game);
        }
        return 1;
    }

    public static int executeStart(CommandSourceStack source, FlounderableGame minigame) {
        ServerLevel world = source.getLevel();
        FlounderGameStartResult gameStartResult = FlounderApi.startMinigame(world, minigame);
        source.sendSuccess(() -> getMinigameStartResultText(minigame, gameStartResult), false);
        return gameStartResult.isSuccessful() ? 1 : 0;
    }

    public static int executeStop(CommandSourceStack source, int minigameId) {
        ServerLevel world = source.getLevel();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            return noMinigameActive(source);
        }

        if(!games.containsKey(minigameId)) {
            return noMinigameActive(source, minigameId);
        }

        FlounderableGame game = games.get(minigameId);
        FlounderApi.endMinigame(game);
        source.sendSuccess(() -> Component.translatable("command.flounderlib.stop", getMinigameText(game)), false);
        return 1;
    }

    public static int executeStopAll(CommandSourceStack source) {
        ServerLevel world = source.getLevel();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            return noMinigameActive(source);
        }

        source.sendSuccess(() -> Component.translatable("command.flounderlib.stop_all", games.size()), false);
        for (FlounderableGame game : games.values()) {
            FlounderApi.endMinigame(game);
            source.sendSuccess(() -> Component.translatable("command.flounderlib.stop", getMinigameText(game)), false);
        }
        return 1;
    }

    public static void listMinigames(CommandSourceStack source, Collection<FlounderableGame> games) {
        listMinigames(source, games, Component.empty());
    }

    public static void listMinigames(CommandSourceStack source, Collection<FlounderableGame> games, Component location) {
        Component message = Component.translatable("command.flounderlib.found", games.size())
                .append(location)
                .append(Component.translatable("command.flounderlib.found_suffix"));
        source.sendSuccess(() -> message, false);
        for (FlounderableGame game : games) {
            source.sendSuccess(() -> listMinigame(game), false);
        }
    }

    public static Component listMinigame(FlounderableGame game) {
        return getMinigameText(game, true, true);
    }

    public static void highlightMinigame(CommandSourceStack source, FlounderableGame game) throws CommandSyntaxException {
        ServerLevel world = source.getLevel();
        ServerPlayer player = source.getPlayerOrException();
        BlockPos gamePos = game.getCenterPos();

        world.sendParticles(player, ParticleTypes.END_ROD, true, true, gamePos.getX(), gamePos.getY(), gamePos.getZ(), 10, 0.1, 0.1, 0.1, 0);
        source.sendSuccess(() -> Component.translatable("command.flounderlib.highlight", getMinigameText(game)), false);
    }

    public static Component getMinigameStartResultText(FlounderableGame game, FlounderGameStartResult result) {
        return switch (result) {
            case SUCCESS -> Component.translatable("command.flounderlib.start_success", getMinigameText(game));
            case FAILED_SINGLETON -> {
                Component gameText = getMinigameText(game, false, false);
                Component errorText = Component.translatable("command.flounderlib.start_fail_singleton").withStyle(ChatFormatting.RED);
                Component tooltipText = Component.translatable("command.flounderlib.start_fail_singleton_tooltip").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                yield Component.translatable("command.flounderlib.could_not_start", gameText, errorText, tooltipText);
            }
            case FAILED_OVERLAP -> {
                Component gameText = getMinigameText(game, false, false);
                Component errorText = Component.translatable("command.flounderlib.start_fail_overlap").withStyle(ChatFormatting.RED);
                Component tooltipText = Component.translatable("command.flounderlib.start_fail_overlap_tooltip").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                yield Component.translatable("command.flounderlib.could_not_start", gameText, errorText, tooltipText);
            }
            default -> {
                Component gameText = getMinigameText(game, false, false);
                Component errorText = Component.translatable("command.flounderlib.start_fail").withStyle(ChatFormatting.RED);
                Component tooltipText = Component.empty();
                yield Component.translatable("command.flounderlib.could_not_start", gameText, errorText, tooltipText);
            }
        };
    }

    public static Component getBlockPosText(BlockPos pos, boolean teleport) {
        Component coords = Component.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ());
        if(teleport) {
            coords = ComponentUtils.wrapInSquareBrackets(coords)
                    .withStyle(style -> style.withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent.SuggestCommand("/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                            .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.coordinates.tooltip")))
                    );
        }
        return coords;
    }

    public static Component getMinigameText(FlounderableGame game) {
        return getMinigameText(game, true, false);
    }

    public static Component getMinigameText(FlounderableGame game, boolean includeIntId, boolean includeCoords) {
        MutableComponent text = Component.literal(String.valueOf(game.getIdentifier())).withStyle(ChatFormatting.AQUA);
        if(includeIntId) {
            text.append(Component.literal(" (Int ID: " + game.getMinigameId() + ")").withStyle(ChatFormatting.DARK_AQUA));
        } if(includeCoords) {
            text.append(Component.literal(" ").append(getBlockPosText(game.getCenterPos(), true)));
        }
        return text;
    }

    public static int noMinigameActive(CommandSourceStack source) {
        return noMinigameActive(source, Component.translatable("command.flounderlib.none_active"));
    }

    public static int noMinigameActive(CommandSourceStack source, int minigameId) {
        return noMinigameActive(source, Component.translatable("command.flounderlib.id_not_active", minigameId));
    }

    public static int noMinigameActive(CommandSourceStack source, MutableComponent message) {
        source.sendSuccess(() -> message.withStyle(ChatFormatting.RED, ChatFormatting.ITALIC), false);
        return 0;
    }

}

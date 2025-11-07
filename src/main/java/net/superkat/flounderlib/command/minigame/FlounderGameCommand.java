package net.superkat.flounderlib.command.minigame;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.FlounderGameStartResult;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.command.minigame.argument.FlounderMinigameArgumentType;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import net.superkat.flounderlib.minigame.FlounderRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * I sorta strongly dislike this class, but at least the user facing result is sorta decent
 */
public class FlounderGameCommand {

    public static final Map<Identifier, ArgumentBuilder<ServerCommandSource, ?>> MINIGAME_AUTOFILLS = new HashMap<>();

    public static final SuggestionProvider<ServerCommandSource> AVAILABLE_MINIGAME_IDS = SuggestionProviders.register(
            Identifier.of(FlounderLib.MOD_ID, "available_minigame_ids"),
            (commandContext, suggestionsBuilder) -> {
                Set<Identifier> idSet = FlounderRegistry.getRegistry().getIds();
                return CommandSource.suggestIdentifiers(idSet, suggestionsBuilder);
            }
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("flounderlib")
                    .requires(source -> source.hasPermissionLevel(3))
                    .then(CommandManager.literal("minigames")
                                .then(listCommand())
                                .then(locateCommand())
                                .then(highlightCommand())
                                .then(startCommand(registryAccess))
                                .then(stopCommand())
                                .then(stopAllCommand())
                        )
        );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> listCommand() {
        return CommandManager.literal("list")
                .executes(context -> executeList(context.getSource()));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> locateCommand() {
        return CommandManager.literal("locate")
                .executes(context -> executeLocate(context.getSource(), BlockPos.ofFloored(context.getSource().getPosition()), true))
                .then(CommandManager.argument("position", BlockPosArgumentType.blockPos())
                        .executes(context -> executeLocate(context.getSource(), BlockPosArgumentType.getBlockPos(context, "position"), false))
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> highlightCommand() {
        return CommandManager.literal("highlight")
                .executes(context -> executeHighlight(context.getSource(), -1))
                .then(CommandManager.argument("minigameIntId", IntegerArgumentType.integer(0))
                        .executes(context -> executeHighlight(context.getSource(), IntegerArgumentType.getInteger(context, "minigameIntId")))
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> startCommand(CommandRegistryAccess registryAccess) {
        return forAllMinigames(registryAccess, "start");
    }

    private static LiteralArgumentBuilder<ServerCommandSource> forAllMinigames(CommandRegistryAccess registryAccess, String name) {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal(name);

        for (Identifier minigameId : FlounderRegistry.getRegistry().getIds()) {
            if(MINIGAME_AUTOFILLS.containsKey(minigameId)) {
                command.then(
                        CommandManager.literal(minigameId.toString())
                                .then(MINIGAME_AUTOFILLS.get(minigameId))
                );
            } else {
                command.then(
                        CommandManager.argument("minigame", FlounderMinigameArgumentType.flounderGame(registryAccess))
                                .suggests(AVAILABLE_MINIGAME_IDS)
                                .executes(context -> executeStart(context.getSource(), FlounderMinigameArgumentType.getFlounderGame(context, "minigame")))
                );
            }
            command.then(CommandManager.literal(minigameId.toString()));
        }

        return command;
    }

    private static LiteralArgumentBuilder<ServerCommandSource> stopCommand() {
        return CommandManager.literal("stop")
                .then(CommandManager.argument("minigameIntId", IntegerArgumentType.integer(0))
                        .executes(context -> executeStop(context.getSource(), IntegerArgumentType.getInteger(context, "minigameIntId")))
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> stopAllCommand() {
        return CommandManager.literal("stopAll")
                .executes(context -> executeStopAll(context.getSource()));
    }



    public static int executeList(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            return noMinigameActive(source);
        }

        listMinigames(source, games.values());
        return 1;
    }

    public static int executeLocate(ServerCommandSource source, BlockPos searchPos, boolean playerLocation) {
        ServerWorld world = source.getWorld();

        List<FlounderableGame> games = FlounderApi.findMinigamesAt(world, searchPos);
        Text locationText = playerLocation ? Text.translatable("command.flounderlib.player_location") : getBlockPosText(searchPos, false);
        if(games == null || games.isEmpty()) {
            return noMinigameActive(source, Text.translatable("command.flounderlib.found_fail", locationText));
        }

        locationText = Text.literal(" ").append(locationText); // cursed
        listMinigames(source, games, locationText);
        return 1;
    }

    public static int executeHighlight(ServerCommandSource source, int highlightedMinigame) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
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

    public static int executeStart(ServerCommandSource source, FlounderableGame minigame) {
        ServerWorld world = source.getWorld();
        FlounderGameStartResult gameStartResult = FlounderApi.startMinigame(world, minigame);
        source.sendFeedback(() -> getMinigameStartResultText(minigame, gameStartResult), false);
        return gameStartResult.isSuccessful() ? 1 : 0;
    }

    public static int executeStop(ServerCommandSource source, int minigameId) {
        ServerWorld world = source.getWorld();
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
        source.sendFeedback(() -> Text.translatable("command.flounderlib.stop", getMinigameText(game)), false);
        return 1;
    }

    public static int executeStopAll(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            return noMinigameActive(source);
        }

        source.sendFeedback(() -> Text.translatable("command.flounderlib.stop_all", games.size()), false);
        for (FlounderableGame game : games.values()) {
            FlounderApi.endMinigame(game);
            source.sendFeedback(() -> Text.translatable("command.flounderlib.stop", getMinigameText(game)), false);
        }
        return 1;
    }

    public static void listMinigames(ServerCommandSource source, Collection<FlounderableGame> games) {
        listMinigames(source, games, Text.empty());
    }

    public static void listMinigames(ServerCommandSource source, Collection<FlounderableGame> games, Text location) {
        Text message = Text.translatable("command.flounderlib.found", games.size())
                .append(location)
                .append(Text.translatable("command.flounderlib.found_suffix"));
        source.sendFeedback(() -> message, false);
        for (FlounderableGame game : games) {
            source.sendFeedback(() -> listMinigame(game), false);
        }
    }

    public static Text listMinigame(FlounderableGame game) {
        return getMinigameText(game, true, true);
    }

    public static void highlightMinigame(ServerCommandSource source, FlounderableGame game) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        ServerPlayerEntity player = source.getPlayerOrThrow();
        BlockPos gamePos = game.getCenterPos();

        world.spawnParticles(player, ParticleTypes.END_ROD, true, true, gamePos.getX(), gamePos.getY(), gamePos.getZ(), 10, 0.1, 0.1, 0.1, 0);
        source.sendFeedback(() -> Text.translatable("command.flounderlib.highlight", getMinigameText(game)), false);
    }

    public static Text getMinigameStartResultText(FlounderableGame game, FlounderGameStartResult result) {
        return switch (result) {
            case SUCCESS -> Text.translatable("command.flounderlib.start_success", getMinigameText(game));
            case FAILED_SINGLETON -> {
                Text gameText = getMinigameText(game, false, false);
                Text errorText = Text.translatable("command.flounderlib.start_fail_singleton").formatted(Formatting.RED);
                Text tooltipText = Text.translatable("command.flounderlib.start_fail_singleton_tooltip").formatted(Formatting.GRAY, Formatting.ITALIC);
                yield Text.translatable("command.flounderlib.could_not_start", gameText, errorText, tooltipText);
            }
            case FAILED_OVERLAP -> {
                Text gameText = getMinigameText(game, false, false);
                Text errorText = Text.translatable("command.flounderlib.start_fail_overlap").formatted(Formatting.RED);
                Text tooltipText = Text.translatable("command.flounderlib.start_fail_overlap_tooltip").formatted(Formatting.GRAY, Formatting.ITALIC);
                yield Text.translatable("command.flounderlib.could_not_start", gameText, errorText, tooltipText);
            }
            default -> {
                Text gameText = getMinigameText(game, false, false);
                Text errorText = Text.translatable("command.flounderlib.start_fail").formatted(Formatting.RED);
                Text tooltipText = Text.empty();
                yield Text.translatable("command.flounderlib.could_not_start", gameText, errorText, tooltipText);
            }
        };
    }

    public static Text getBlockPosText(BlockPos pos, boolean teleport) {
        Text coords = Text.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ());
        if(teleport) {
            coords = Texts.bracketed(coords)
                    .styled(style -> style.withColor(Formatting.GREEN)
                            .withClickEvent(new ClickEvent.SuggestCommand("/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                            .withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.coordinates.tooltip")))
                    );
        }
        return coords;
    }

    public static Text getMinigameText(FlounderableGame game) {
        return getMinigameText(game, true, false);
    }

    public static Text getMinigameText(FlounderableGame game, boolean includeIntId, boolean includeCoords) {
        MutableText text = Text.literal(String.valueOf(game.getIdentifier())).formatted(Formatting.AQUA);
        if(includeIntId) {
            text.append(Text.literal(" (Int ID: " + game.getMinigameId() + ")").formatted(Formatting.DARK_AQUA));
        } if(includeCoords) {
            text.append(Text.literal(" ").append(getBlockPosText(game.getCenterPos(), true)));
        }
        return text;
    }

    public static int noMinigameActive(ServerCommandSource source) {
        return noMinigameActive(source, Text.translatable("command.flounderlib.none_active"));
    }

    public static int noMinigameActive(ServerCommandSource source, int minigameId) {
        return noMinigameActive(source, Text.translatable("command.flounderlib.id_not_active", minigameId));
    }

    public static int noMinigameActive(ServerCommandSource source, MutableText message) {
        source.sendFeedback(() -> message.formatted(Formatting.RED, Formatting.ITALIC), false);
        return 0;
    }

}

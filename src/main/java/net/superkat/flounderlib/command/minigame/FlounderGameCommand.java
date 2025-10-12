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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.action.FlounderGameStartResult;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.command.minigame.argument.FlounderMinigameArgumentType;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import net.superkat.flounderlib.minigame.FlounderRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            source.sendFeedback(() -> Text.literal("No active minigames found!"), false);
            return 0;
        }

        listMinigames(source, games.values());
        return 1;
    }

    public static int executeLocate(ServerCommandSource source, BlockPos searchPos, boolean playerLocation) {
        ServerWorld world = source.getWorld();

        List<FlounderableGame> games = FlounderApi.findMinigamesAt(world, searchPos);
        String locationString = playerLocation ? "your location" : searchPos.toShortString();
        if(games == null || games.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No minigames found at " + locationString + "!"), false);
            return 0;
        }

        listMinigames(source, games, locationString);
        return 1;
    }

    public static int executeHighlight(ServerCommandSource source, int highlightedMinigame) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(highlightedMinigame == -1) {
            if(games == null || games.isEmpty()) {
                source.sendFeedback(() -> Text.literal("No minigames are active!"), false);
                return 0;
            }

            for (FlounderableGame game : games.values()) {
                highlightMinigame(source, game);
            }
        } else {
            FlounderableGame game = games.get(highlightedMinigame);
            if(game == null) {
                source.sendFeedback(() -> Text.literal("Minigame with Int ID " + highlightedMinigame + " does not exist!"), false);
                return 0;
            }

            highlightMinigame(source, game);
        }
        return 1;
    }

    public static int executeStart(ServerCommandSource source, FlounderableGame minigame) {
        ServerWorld world = source.getWorld();
        FlounderGameStartResult gameStartResult = FlounderApi.startMinigame(world, minigame);
        if(gameStartResult.isSuccessful()) {
            source.sendFeedback(() -> Text.literal("Started minigame " + minigame.getIdentifier() + " - Int ID: " + minigame.getMinigameId()), false);
            return 1;
        } else {
            source.sendFeedback(() -> Text.literal("Could not start minigame " + minigame.getIdentifier() + "!"), false);
            return 0;
        }
    }

    public static int executeStop(ServerCommandSource source, int minigameId) {
        ServerWorld world = source.getWorld();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No minigames are active!"), false);
            return 0;
        }

        if(!games.containsKey(minigameId)) {
            source.sendFeedback(() -> Text.literal("There are no active minigames with the Int ID: " + minigameId), false);
            return 0;
        }

        FlounderableGame game = games.get(minigameId);
        FlounderApi.endMinigame(game);
        source.sendFeedback(() -> Text.literal("Ended " + game.getIdentifier() + " - Int ID: " + game.getMinigameId()), false);
        return 1;
    }

    public static int executeStopAll(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        Map<Integer, FlounderableGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No minigames are active!"), false);
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Ending " + games.size() + " minigames!"), false);
        for (FlounderableGame game : games.values()) {
            FlounderApi.endMinigame(game);
            source.sendFeedback(() -> Text.literal("Ended " + game.getIdentifier() + " - Int ID: " + game.getMinigameId()), false);
        }
        return 1;
    }

    private static void listMinigames(ServerCommandSource source, Collection<FlounderableGame> games) {
        listMinigames(source, games, "");
    }

    private static void listMinigames(ServerCommandSource source, Collection<FlounderableGame> games, String suffix) {
        source.sendFeedback(() -> Text.literal("Found " + games.size() + " minigames " + suffix + ":"), false);
        for (FlounderableGame game : games) {
            source.sendFeedback(() -> Text.literal(game.getIdentifier() + " - Int ID: " + game.getMinigameId()), false);
        }
    }

    private static void highlightMinigame(ServerCommandSource source, FlounderableGame game) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        ServerPlayerEntity player = source.getPlayerOrThrow();
        BlockPos gamePos = game.getCenterPos();

        world.spawnParticles(player, ParticleTypes.END_ROD, true, true, gamePos.getX(), gamePos.getY(), gamePos.getZ(), 10, 0.1, 0.1, 0.1, 0);
        source.sendFeedback(() -> Text.literal("Highlighted " + game.getIdentifier() + " - Int ID: " + game.getMinigameId()), false);
    }

}

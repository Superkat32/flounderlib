package net.superkat.flounderlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.minigame.FlounderServerGameManager;

import java.util.Map;
import java.util.Set;

public class FlounderGameCommand {

    public static final SuggestionProvider<ServerCommandSource> AVAILABLE_MINIGAME_IDS = SuggestionProviders.register(
            Identifier.of(FlounderLib.MOD_ID, "available_minigame_ids"),
            (commandContext, suggestionsBuilder) -> {
                Set<Identifier> idSet = FlounderApi.getRegistry().getIds();
                return CommandSource.suggestIdentifiers(idSet, suggestionsBuilder);
            }
    );

    // Not synced to client yet :(
//    public static final SuggestionProvider<ServerCommandSource> AVAILABLE_ACTIVE_MINIGAME_INT_IDS = SuggestionProviders.register(
//            Identifier.of(FlounderLib.MOD_ID, "available_active_minigame_int_ids"),
//            (commandContext, suggestionsBuilder) -> {
//                if(commandContext.getSource() instanceof ServerCommandSource serverCommandSource) {
//                    ServerWorld world = serverCommandSource.getWorld();
//                    FlounderGameManager manager = FlounderApi.getFlounderGameManager(world);
//                    return CommandSource.suggestMatching(manager.getGamesMap().keySet().stream().entries(Object::toString), suggestionsBuilder);
//                }
//                return CommandSource.suggestMatching(List.of(), suggestionsBuilder);
//            }
//    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("flounderlib")
                        .requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.literal("games")
                                .then(listCommand())
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

    private static LiteralArgumentBuilder<ServerCommandSource> startCommand(CommandRegistryAccess registryAccess) {
        return CommandManager.literal("start")
                .then(
                        CommandManager.argument("game", FlounderGameArgumentType.flounderGame(registryAccess))
                                .suggests(AVAILABLE_MINIGAME_IDS)
                                .executes(context -> executeStart(context.getSource(), FlounderGameArgumentType.getFlounderGame(context, "game")))
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> stopCommand() {
        return CommandManager.literal("stop")
                .then(CommandManager.argument("intId", IntegerArgumentType.integer(0))
                        .executes(context -> executeStop(context.getSource(), IntegerArgumentType.getInteger(context, "intId")))
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> stopAllCommand() {
        return CommandManager.literal("stopAll")
                .executes(
                        context -> stopAll(context.getSource())
                ).then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                        .suggests(AVAILABLE_MINIGAME_IDS)
                        .executes(context -> stopAll(context.getSource(), IdentifierArgumentType.getIdentifier(context, "id")))
                );
    }

    public static int executeList(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        FlounderServerGameManager manager = FlounderApi.getFlounderServerGameManager(world);
        Map<Integer, IFlounderGame> map = manager.getGames();

        if(map == null || map.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No active minigames found!"), false);
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Found " + map.size() + " minigames:"), false);

        for (Map.Entry<Integer, IFlounderGame> entry : map.entrySet()) {
            source.sendFeedback(() -> Text.literal("ID: " + entry.getValue().getIdentifier() + " - Int ID: " + entry.getKey().toString()), false);
        }
        return 1;
    }

    public static int executeStart(ServerCommandSource source, IFlounderGame game) {
        ServerWorld world = source.getWorld();
        Identifier id = game.getIdentifier();
        FlounderApi.addGame(world, game);
        source.sendFeedback(() -> Text.literal("Started minigame " + id + "!"), false);
        return 1;
    }

    public static int executeStop(ServerCommandSource source, int intId) {
        ServerWorld world = source.getWorld();
        FlounderServerGameManager manager = FlounderApi.getFlounderServerGameManager(world);
        Map<Integer, IFlounderGame> map = manager.getGames();

        if(map == null || map.isEmpty()) {
            return noGamesActive(source);
        }

        if(!map.containsKey(intId)) {
            source.sendFeedback(() -> Text.literal("No minigames are active with that integer id!"), false);
            return 0;
        }

        IFlounderGame game = map.get(intId);
        if(game != null) {
            Identifier idId = game.getIdentifier();
            FlounderApi.endGame(game);
            source.sendFeedback(() -> Text.literal("Invalidated minigame '" + idId + "' with intId " + intId + "!"), false);
            return 1;
        }

        return 0;
    }

    public static int stopAll(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        FlounderServerGameManager manager = FlounderApi.getFlounderServerGameManager(world);
        Map<Integer, IFlounderGame> games = manager.getGames();

        if(games == null || games.isEmpty()) {
            return noGamesActive(source);
        }

        int gameCount = games.size();
        for (IFlounderGame game : games.values()) {
            FlounderApi.endGame(game);
        }

        source.sendFeedback(() -> Text.literal("Ended " + gameCount + " minigames!"), false);
        return 1;
    }

    public static int stopAll(ServerCommandSource source, Identifier id) {
        ServerWorld world = source.getWorld();
        FlounderServerGameManager manager = FlounderApi.getFlounderServerGameManager(world);

        int gameCount = 0;
        Map<Integer, IFlounderGame> games = manager.getGames();
        if(games == null || games.isEmpty()) {
            return noGamesActive(source);
        }

        for (IFlounderGame game : games.values()) {
            if (game.getIdentifier().equals(id)) {
                FlounderApi.endGame(game);
                gameCount++;
            }
        }

        if(gameCount == 0) {
            source.sendFeedback(() -> Text.literal("No minigames with that id are currently active!"), false);
            return 0;
        } else {
            int finalGameCount = gameCount; //why i can't just use gamecount i have no clue
            source.sendFeedback(() -> Text.literal("Ended " + finalGameCount + " games with ID " + id), false);
            return 1;
        }
    }

    private static int noGamesActive(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("No minigames are currently active!"), false);
        return 0;
    }

}

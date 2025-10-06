package net.superkat.flounderlib.command.minigame;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;

public class FlounderGameCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("flounderlib")
                        .requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.literal("games")
                                .then(toggleBoundingBoxes())
                        )
        );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> toggleBoundingBoxes() {
        return CommandManager.literal("toggleBoundingBoxes")
                .executes(context -> executeToggleBoundingBoxes(context.getSource()));
    }

    public static int executeToggleBoundingBoxes(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        return 1;
    }

}

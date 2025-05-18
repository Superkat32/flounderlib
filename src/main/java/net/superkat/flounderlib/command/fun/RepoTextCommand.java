package net.superkat.flounderlib.command.fun;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.superkat.flounderlib.network.fun.packets.RepoTextS2CPacket;

public class RepoTextCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("repotext")
                        .requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.argument("text", StringArgumentType.string())
                                .executes(context -> executeSend(context.getSource(), StringArgumentType.getString(context, "text")))
                        )
        );
    }

    public static int executeSend(ServerCommandSource source, String text) {
        ServerPlayerEntity player = source.getPlayer();

        ServerPlayNetworking.send(player, new RepoTextS2CPacket(text));
        return 0;
    }

}

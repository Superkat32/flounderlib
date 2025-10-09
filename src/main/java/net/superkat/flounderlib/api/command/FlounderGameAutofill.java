package net.superkat.flounderlib.api.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;

@FunctionalInterface
public interface FlounderGameAutofill {

    RequiredArgumentBuilder<ServerCommandSource, ?> argumentBuilder(CommandRegistryAccess registryAccess);

    default int executeStart(CommandContext<ServerCommandSource> context, FlounderableGame game) {
        return FlounderGameCommand.executeStart(context.getSource(), game);
    }
}

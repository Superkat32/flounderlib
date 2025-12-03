package net.superkat.flounderlib.api.minigame.v1.registry.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.impl.minigame.command.FlounderGameCommand;

public class FlounderGameCommandApi {

    public static void registerGameAutofill(Identifier minigameId, RequiredArgumentBuilder<ServerCommandSource, ?> argumentBuilder) {
        FlounderGameCommand.MINIGAME_AUTOFILLS.put(minigameId, argumentBuilder);
    }

    public static int executeMinigameStart(CommandContext<ServerCommandSource> context, FlounderableGame game) {
        return executeMinigameStart(context.getSource(), game);
    }

    public static int executeMinigameStart(ServerCommandSource source, FlounderableGame game) {
        return FlounderGameCommand.executeStart(source, game);
    }

}

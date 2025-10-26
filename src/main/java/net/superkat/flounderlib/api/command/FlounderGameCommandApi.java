package net.superkat.flounderlib.api.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;

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

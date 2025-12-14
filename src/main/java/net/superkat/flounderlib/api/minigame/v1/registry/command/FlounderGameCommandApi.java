package net.superkat.flounderlib.api.minigame.v1.registry.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.impl.minigame.command.FlounderGameCommand;

public class FlounderGameCommandApi {

    public static void registerGameAutofill(Identifier minigameId, RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder) {
        FlounderGameCommand.MINIGAME_AUTOFILLS.put(minigameId, argumentBuilder);
    }

    public static int executeMinigameStart(CommandContext<CommandSourceStack> context, FlounderableGame game) {
        return executeMinigameStart(context.getSource(), game);
    }

    public static int executeMinigameStart(CommandSourceStack source, FlounderableGame game) {
        return FlounderGameCommand.executeStart(source, game);
    }

}

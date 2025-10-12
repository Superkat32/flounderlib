package net.superkat.flounderlibtest;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.superkat.flounderlib.api.command.FlounderGameCommandApi;
import net.superkat.flounderlibtest.test.ExampleMinigame;
import net.superkat.flounderlibtest.test.SimpleTestMinigame;

public class MinigameAutofills {

    public static void init() {
        FlounderGameCommandApi.registerGameAutofill(
                ExampleMinigame.ID,
                CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                        .executes(
                                context -> FlounderGameCommandApi.executeMinigameStart(
                                        context, new ExampleMinigame(BlockPosArgumentType.getBlockPos(context, "pos"))
                                )
                        )
        );

        FlounderGameCommandApi.registerGameAutofill(
                SimpleTestMinigame.ID,
                CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                        .executes(
                                context -> FlounderGameCommandApi.executeMinigameStart(
                                        context, new SimpleTestMinigame(BlockPosArgumentType.getBlockPos(context, "pos"))
                                )
                        )
        );
    }

}

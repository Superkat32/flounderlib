package net.superkat.flounderlibtest;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.superkat.flounderlib.api.minigame.v1.registry.command.FlounderGameCommandApi;
import net.superkat.flounderlibtest.test.TestMinigame;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;

public class MinigameAutofills {

    public static void init() {
        FlounderGameCommandApi.registerGameAutofill(
                TestMinigame.ID,
                Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(
                                context -> FlounderGameCommandApi.executeMinigameStart(
                                        context, new TestMinigame(BlockPosArgument.getBlockPos(context, "pos"))
                                )
                        )
        );

        FlounderGameCommandApi.registerGameAutofill(
                TestSyncedMinigame.ID,
                Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(
                                context -> FlounderGameCommandApi.executeMinigameStart(
                                        context, new TestSyncedMinigame(BlockPosArgument.getBlockPos(context, "pos"))
                                )
                        )
        );
    }

}

package net.superkat.flounderlibtest;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.superkat.flounderlib.api.minigame.v1.registry.command.FlounderGameCommandApi;
import net.superkat.flounderlibtest.test.TestMinigame;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;

public class MinigameAutofills {

    public static void init() {
        FlounderGameCommandApi.registerGameAutofill(
                TestMinigame.ID,
                CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                        .executes(
                                context -> FlounderGameCommandApi.executeMinigameStart(
                                        context, new TestMinigame(BlockPosArgumentType.getBlockPos(context, "pos"))
                                )
                        )
        );

        FlounderGameCommandApi.registerGameAutofill(
                TestSyncedMinigame.ID,
                CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                        .executes(
                                context -> FlounderGameCommandApi.executeMinigameStart(
                                        context, new TestSyncedMinigame(BlockPosArgumentType.getBlockPos(context, "pos"))
                                )
                        )
        );
    }

}

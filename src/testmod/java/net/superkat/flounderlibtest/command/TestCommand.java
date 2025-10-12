package net.superkat.flounderlibtest.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.command.FlounderArguments;
import net.superkat.flounderlib.api.command.FlounderCommand;

public class TestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                FlounderCommand.create(
                        instance -> instance.literal("floundertest")
                                .literal("minigames")
                                .group(group -> group.literal("list").executes(TestCommand::executeList))
                                .group(group -> group.literal("locate")
                                        .arguments(
                                                FlounderArguments.ofBlockPos("position", true)
                                        ).apply(TestCommand::executeLocate)
                                ).group(group -> group.literal("highlight")
                                        .arguments(
                                                FlounderArguments.ofInteger("minigameIntId", -1)
                                        ).apply(TestCommand::executeHighlight)
                                ).group(group -> group.literal("stop")
                                        .arguments(
                                                FlounderArguments.ofInteger("minigameIntId", 0)
                                        ).apply(TestCommand::executeStop)
                                ).group(group -> group.literal("stopAll").executes(TestCommand::executeStopAll))
                ).build()
        );
    }

    public static int executeList(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("list"), false);
        return 1;
    }

    public static int executeLocate(ServerCommandSource source, BlockPos pos) {
        source.sendFeedback(() -> Text.literal("locate - " + pos.toString()), false);
        return 1;
    }

    public static int executeHighlight(ServerCommandSource source, int minigameIntId) {
        source.sendFeedback(() -> Text.literal("highlight - " + minigameIntId), false);
        return 1;
    }

    public static int executeStop(ServerCommandSource source, int minigameIntId) {
        source.sendFeedback(() -> Text.literal("stop - " + minigameIntId), false);
        return 1;
    }

    public static int executeStopAll(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("stopall"), false);
        return 1;
    }

}

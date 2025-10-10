package net.superkat.flounderlibtest.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.superkat.flounderlib.api.command.FlounderCommand;
import net.superkat.flounderlib.command.argument.FlCommandArg;

public class TestCommand {

//    public static final FlounderCommand PARTICLE_COMMAND = FlounderCommand.create(
//            instance -> instance.literal("particle")
//                    .requiresPermission(2)
//                    .argument(FlCommandArg.ofParticleEffect("name"))
//                    .executes(this::execute, context.getSource().getPosition(), Vec3d.ZERO, 0f, 0, false, context.getSource().getServer().getPlayerManager().getPlayerList())
//                    .argument(FlCommandArg.ofBlockPos("pos"))
//                    .executes(this::execute, Vec3d.ZERO, 0f, 0, false, context.getSource().getServer().getPlayerManager().getPlayerList())
//                    .argument(FlCommandArg.ofVec3("delta"))
//                    .argument(FlCommandArg.ofFloat("speed"))
//                    .argument(FlCommandArg.ofInt("count"))
//                    .executes(this::execute, false, context.getSource().getServer().getPlayerManager().getPlayerList())
//                    .argument(FlCommandArg.ofBoolean("force", "force", "normal"))
//                    .executes(this::execute, context.getSource().getServer().getPlayerManager().getPlayerList())
//                    .argument(FlCommandArg.ofPlayers("viewers"))
//                    .executes(this::execute)
//    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                FlounderCommand.create(
                        instance -> instance.literal("floundertest")
                                .executes(TestCommand::executeTest)
                                .argument(argInstance -> argInstance
                                        .argument(FlCommandArg.ofBoolean("test"))
                                        .executes(TestCommand::executeTestBool)
                                )
                ).build()
        );
    }

    public static int executeTestBool(ServerCommandSource source, boolean test) {
        source.sendFeedback(() -> Text.literal("hiya " + test), false);
        return 1;
    }

    public static int executeTest(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("hiya"), false);
        return 1;
    }

}

package net.superkat.flounderlibtest;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.superkat.flounderlib.api.command.FlounderCommandApi;
import net.superkat.flounderlib.api.command.FlounderGameAutofill;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;
import net.superkat.flounderlibtest.test.ExampleMinigame;
import net.superkat.flounderlibtest.test.MonkeyMinigame;
import net.superkat.flounderlibtest.test.TestMinigame;

public class MinigameAutofills {

    public static final FlounderGameAutofill EXAMPLE_AUTOFILL = new FlounderGameAutofill() {
        @Override
        public RequiredArgumentBuilder<ServerCommandSource, ?> argumentBuilder(CommandRegistryAccess registryAccess) {
            return CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                    .executes(context -> this.executeStart(context,
                            new ExampleMinigame(BlockPosArgumentType.getBlockPos(context, "pos")))
                    )
                    .then(CommandManager.argument("ticks", IntegerArgumentType.integer(0))
                            .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"0"}, builder))
                            .then(CommandManager.argument("myBoolean", BoolArgumentType.bool())
                                    .then(CommandManager.argument("myInteger", IntegerArgumentType.integer())
                                            .then(CommandManager.argument("myString", StringArgumentType.string())
                                                    .then(CommandManager.argument("myVec3d", Vec3ArgumentType.vec3())
                                                            .then(CommandManager.argument("myText", TextArgumentType.text(registryAccess))
                                                                    .executes(context -> this.executeStart(context,
                                                                            new ExampleMinigame(
                                                                                    IntegerArgumentType.getInteger(context, "ticks"),
                                                                                    BlockPosArgumentType.getBlockPos(context, "pos"),
                                                                                    BoolArgumentType.getBool(context, "myBoolean"),
                                                                                    IntegerArgumentType.getInteger(context, "myInteger"),
                                                                                    StringArgumentType.getString(context, "myString"),
                                                                                    Vec3ArgumentType.getVec3(context, "myVec3d"),
                                                                                    TextArgumentType.getTextArgument(context, "myText")
                                                                            ))
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    );
        }
    };

    public static final FlounderGameAutofill TEST_AUTOFILL = new FlounderGameAutofill() {
        @Override
        public RequiredArgumentBuilder<ServerCommandSource, ?> argumentBuilder(CommandRegistryAccess registryAccess) {
            return CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                    .executes(context -> this.executeStart(context,
                            new TestMinigame(BlockPosArgumentType.getBlockPos(context, "pos")))
                    )
                    .then(CommandManager.argument("ticks", IntegerArgumentType.integer(0))
                            .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"0"}, builder))
                            .then(CommandManager.argument("test", BoolArgumentType.bool())
                                    .executes(context -> this.executeStart(context,
                                                    new TestMinigame(
                                                            IntegerArgumentType.getInteger(context, "ticks"),
                                                            BlockPosArgumentType.getBlockPos(context, "pos"),
                                                            BoolArgumentType.getBool(context, "test"))
                                            )
                                    )
                            )
                    );
        }
    };

    public static void init() {
        FlounderCommandApi.registerAutofill(ExampleMinigame.ID, EXAMPLE_AUTOFILL);
        FlounderCommandApi.registerAutofill(TestMinigame.ID, TEST_AUTOFILL);

        FlounderGameCommand.AUTOFILL_TEST.put(MonkeyMinigame.ID, MonkeyMinigame.AUTOFILL);
    }

}

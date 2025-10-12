package net.superkat.flounderlib.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.command.argument.FlCommandArg;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FlounderArguments {

    public static FlCommandArg<Boolean> ofBoolean(String name) {
        return of(name, BoolArgumentType::bool, BoolArgumentType::getBool);
    }

    public static FlCommandArg<Boolean> ofBoolean(String name, boolean defaultValue) {
        return ofBoolean(name).optional(defaultValue);
    }

    public static FlCommandArg<Integer> ofInteger(String name) {
        return of(name, IntegerArgumentType::integer, IntegerArgumentType::getInteger);
    }

    public static FlCommandArg<Integer> ofInteger(String name, int defaultValue) {
        return ofInteger(name).optional(defaultValue);
    }

    public static FlCommandArg<Long> ofLong(String name) {
        return of(name, LongArgumentType::longArg, LongArgumentType::getLong);
    }

    public static FlCommandArg<Long> ofLong(String name, long defaultValue) {
        return ofLong(name).optional(defaultValue);
    }

    public static FlCommandArg<Float> ofFloat(String name) {
        return of(name, FloatArgumentType::floatArg, FloatArgumentType::getFloat);
    }

    public static FlCommandArg<Float> ofFloat(String name, float defaultValue) {
        return ofFloat(name).optional(defaultValue);
    }

    public static FlCommandArg<Double> ofDouble(String name) {
        return of(name, DoubleArgumentType::doubleArg, DoubleArgumentType::getDouble);
    }

    public static FlCommandArg<Double> ofDouble(String name, double defaultValue) {
        return ofDouble(name).optional(defaultValue);
    }

    public static FlCommandArg<String> ofString(String name) {
        return of(name, StringArgumentType::string, StringArgumentType::getString);
    }

    public static FlCommandArg<String> ofString(String name, String defaultValue) {
        return ofString(name).optional(defaultValue);
    }

    public static FlCommandArg<Vec2f> ofVec2(String name) {
        return of(name, Vec2ArgumentType::vec2, Vec2ArgumentType::getVec2);
    }

    public static FlCommandArg<Vec2f> ofVec2(String name, Vec2f defaultValue) {
        return ofVec2(name).optional(defaultValue);
    }

    public static FlCommandArg<Vec3d> ofVec3(String name) {
        return of(name, Vec3ArgumentType::vec3, Vec3ArgumentType::getVec3);
    }

    public static FlCommandArg<Vec3d> ofVec3(String name, Vec3d defaultValue) {
        return ofVec3(name).optional(defaultValue);
    }

    public static FlCommandArg<BlockPos> ofBlockPos(String name) {
        return of(name, BlockPosArgumentType::new, BlockPosArgumentType::getBlockPos);
    }

    public static FlCommandArg<BlockPos> ofBlockPos(String name, BlockPos defaultValue) {
        return ofBlockPos(name).optional(defaultValue);
    }

    public static FlCommandArg<BlockPos> ofBlockPos(String name, boolean defaultToSourcePos) {
        return ofBlockPos(name).optional(context -> BlockPos.ofFloored(context.getSource().getPosition()));
    }

//    public static FlCommandArg<Entity> ofEntity(String name) {
//        return of(name, EntityArgumentType::entity, EntityArgumentType::getEntity);
//    }
//
//    public static FlCommandArg<Collection<? extends Entity>> ofEntities(String name) {
//        return of(name, EntityArgumentType::entities, EntityArgumentType::getEntities);
//    }
//
//    public static FlCommandArg<ServerPlayerEntity> ofPlayer(String name) {
//        return of(name, EntityArgumentType::player, EntityArgumentType::getPlayer);
//    }
//
//    public static FlCommandArg<Collection<ServerPlayerEntity>> ofPlayers(String name) {
//        return of(name, EntityArgumentType::players, EntityArgumentType::getPlayers);
//    }

    public static <T> FlCommandArg<T> of(
            String name,
            Supplier<? extends ArgumentType<?>> argTypeSupplier,
            BiFunction<CommandContext<ServerCommandSource>, String, T> argGetter
    ) {
        return FlCommandArg.of(name, argTypeSupplier, argGetter);
    }

    public static <T> FlCommandArg<T> of(
            String name,
            Supplier<? extends ArgumentType<?>> argTypeSupplier,
            BiFunction<CommandContext<ServerCommandSource>, String, T> argGetter,
            T defaultValue
    ) {
        return of(name, argTypeSupplier, argGetter).optional(defaultValue);
    }

}

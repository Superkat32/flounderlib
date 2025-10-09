package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FlCommandArg<T> {

    public static FlCommandArg<Boolean> ofBoolean(String name) {
        return of(name, BoolArgumentType::bool, BoolArgumentType::getBool);
    }

    public static FlCommandArg<BlockPos> ofBlockPos(String name) {
        return of(name, BlockPosArgumentType::new, BlockPosArgumentType::getBlockPos);
    }

    public static <T> FlCommandArg<T> of(
            String name,
            Supplier<? extends ArgumentType<?>> argTypeSupplier,
            BiFunction<CommandContext<ServerCommandSource>, String, T> argGetter
    ) {
        return new FlCommandArg<>(name, argTypeSupplier, argGetter);
    }

    private final String name;
    private final Supplier<? extends ArgumentType<?>> argTypeSupplier;
    private final BiFunction<CommandContext<ServerCommandSource>, String, T> argGetter;

    private SuggestionProvider<ServerCommandSource> suggests = null;
    private Command<ServerCommandSource> command = null;

    private FlCommandArg(
            String name,
            Supplier<? extends ArgumentType<?>> argTypeSupplier,
            BiFunction<CommandContext<ServerCommandSource>, String, T> argGetter
    ) {
        this.name = name;
        this.argTypeSupplier = argTypeSupplier;
        this.argGetter = argGetter;
    }

    public FlCommandArg<T> suggests(SuggestionProvider<ServerCommandSource> suggestionProvider) {
        this.suggests = suggestionProvider;
        return this;
    }

    public FlCommandArg<T> executes(Command<ServerCommandSource> command) {
        this.command = command;
        return this;
    }

    public RequiredArgumentBuilder<ServerCommandSource, ?> createArgument() {
        RequiredArgumentBuilder<ServerCommandSource, ?> argument = CommandManager.argument(this.name, this.argTypeSupplier.get());
        if(this.suggests != null) {
            argument = argument.suggests(this.suggests);
        }
        if(this.command != null) {
            argument = argument.executes(this.command);
        }
        return argument;
    }

    public T getArgument(CommandContext<ServerCommandSource> context) {
        return argGetter.apply(context, this.name);
    }
}

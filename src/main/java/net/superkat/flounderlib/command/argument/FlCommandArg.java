package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class FlCommandArg<T> {

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

    private final List<Command<ServerCommandSource>> commands = new ArrayList<>();
    private SuggestionProvider<ServerCommandSource> suggests = null;
    private Function<CommandContext<ServerCommandSource>, T> defaultValueGetter;

    private FlCommandArg(
            String name,
            Supplier<? extends ArgumentType<?>> argTypeSupplier,
            BiFunction<CommandContext<ServerCommandSource>, String, T> argGetter
    ) {
        this.name = name;
        this.argTypeSupplier = argTypeSupplier;
        this.argGetter = argGetter;
    }

    public FlCommandArg<T> optional(T defaultValue) {
        return optional(context -> defaultValue);
    }

    public FlCommandArg<T> optional(Function<CommandContext<ServerCommandSource>, T> defaultValueGetter) {
        this.defaultValueGetter = defaultValueGetter;
        return this;
    }

    public FlCommandArg<T> suggests(SuggestionProvider<ServerCommandSource> suggestionProvider) {
        this.suggests = suggestionProvider;
        return this;
    }

    public FlCommandArg<T> executes(Command<ServerCommandSource> command) {
        this.commands.add(command);
        return this;
    }

    public FlCommandArg<T> executesIfOptional(Command<ServerCommandSource> command) {
        if(this.isOptional()) {
            this.commands.add(command);
        }
        return this;
    }

    public RequiredArgumentBuilder<ServerCommandSource, ?> createArgument() {
        RequiredArgumentBuilder<ServerCommandSource, ?> argument = CommandManager.argument(this.name, this.argTypeSupplier.get());
        if(this.suggests != null) {
            argument = argument.suggests(this.suggests);
        }
        if(!this.commands.isEmpty()) {
            for (Command<ServerCommandSource> command : commands) {
                argument = argument.executes(command);
            }
        }
        return argument;
    }

    public T getArgument(CommandContext<ServerCommandSource> context) {
        if(this.isOptional()) {
            try {
                return argGetter.apply(context, this.name);
            } catch (Exception e) {
                return this.defaultValueGetter.apply(context);
            }
        }
        return argGetter.apply(context, this.name);
    }

    public boolean isOptional() {
        return this.defaultValueGetter != null;
    }
}

package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.superkat.flounderlib.command.argument.instance.FlounderArgumentInstance;
import net.superkat.flounderlib.command.argument.util.CommandViaSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class FlCommandBuilder {
    protected final List<ArgumentBuilder<ServerCommandSource, ?>> argumentBuilders = new ArrayList<>();

    public FlCommandBuilder literal(String name) {
        this.argumentBuilders.add(CommandManager.literal(name));
        return this;
    }

//    public <T> FlounderArgumentInstance<T> argument() {
//
//    }

    public <T> FlCommandBuilder argument(Function<FlounderArgumentInstance<T>, RequiredArgumentBuilder<ServerCommandSource, ?>> argument) {
        this.argumentBuilders.add(argument.apply(new FlounderArgumentInstance<>()));
        return this;
    }

//    public <T> FlCommandBuilder argument(String name, FlCommandArg<T> argument) {
//        RequiredArgumentBuilder<ServerCommandSource, ?> argumentBuilder = argument.createArgument();
//        this.argumentBuilders.add(argumentBuilder);
//        this.flArguments.add(argument);
//        return this;
//    }

    public FlCommandBuilder executes(Command<ServerCommandSource> command) {
        this.getLatestArgument().executes(command);
        return this;
    }

    public FlCommandBuilder executes(CommandViaSource<ServerCommandSource> command) {
        this.getLatestArgument().executes(command);
        return this;
    }

    @SuppressWarnings("unchecked")
    private LiteralArgumentBuilder<ServerCommandSource> getRootArgument() {
        return (LiteralArgumentBuilder<ServerCommandSource>) argumentBuilders.getFirst();
    }

    private ArgumentBuilder<ServerCommandSource, ?> getLatestArgument() {
        return argumentBuilders.getLast();
    }

    public LiteralArgumentBuilder<ServerCommandSource> build() {
        LiteralArgumentBuilder<ServerCommandSource> root = getRootArgument();
        for (ArgumentBuilder<ServerCommandSource, ?> argumentBuilder : argumentBuilders) {
            if(argumentBuilder == root) continue;
            root.then(argumentBuilder);
        }
        return root;
    }

}

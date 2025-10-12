package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.superkat.flounderlib.command.argument.instance.FlounderCommandInstance;
import net.superkat.flounderlib.command.argument.util.CommandViaSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FlCommandBuilder implements FlArgumentProducts<Integer> {
    protected final List<ArgumentBuilder<ServerCommandSource, ?>> argumentBuilders = new ArrayList<>();
    protected final List<ArgumentBuilder<ServerCommandSource, ?>> groupArgumentBuilders = new ArrayList<>();

    public FlCommandBuilder literal(String name) {
        this.argumentBuilders.add(CommandManager.literal(name));
        return this;
    }

    public FlCommandBuilder argument(RequiredArgumentBuilder<ServerCommandSource, ?> argument) {
        this.argumentBuilders.add(argument);
        return this;
    }

    public FlCommandBuilder group(Function<FlounderCommandInstance, FlCommandBuilder> group) {
        return this.group(group.apply(new FlounderCommandInstance()).build());
    }

    public FlCommandBuilder group(ArgumentBuilder<ServerCommandSource, ?> group) {
        this.groupArgumentBuilders.add(group);
        return this;
    }

    public FlCommandBuilder executes(Command<ServerCommandSource> command) {
        this.getLatestArgument().executes(command);
        return this;
    }

    public FlCommandBuilder executes(CommandViaSource<ServerCommandSource> command) {
        this.getLatestArgument().executes(command);
        return this;
    }

    public FlCommandBuilder requires(int permissionLevel) {
        return this.requires(source -> source.hasPermissionLevel(permissionLevel));
    }

    public FlCommandBuilder requires(Predicate<ServerCommandSource> requirement) {
        this.getLatestArgument().requires(requirement);
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
        ArgumentBuilder<ServerCommandSource, ?> previous = null;
        ArgumentBuilder<ServerCommandSource, ?> last = null;

        // must go reversed because arguments are built immediately upon being added as a child,
        // which won't account for further children being added
        for (ArgumentBuilder<ServerCommandSource, ?> argumentBuilder : this.argumentBuilders.reversed()) {
            if(previous != null) {
                argumentBuilder.then(previous);
            } else {
                // apply groups to last argument
                for (ArgumentBuilder<ServerCommandSource, ?> groupArgumentBuilder : groupArgumentBuilders) {
                    argumentBuilder.then(groupArgumentBuilder);
                }
            }
            previous = argumentBuilder;
        }
        return this.getRootArgument();
    }

    @Override
    public FlCommandBuilder getBuilder() {
        return this;
    }
}

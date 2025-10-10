package net.superkat.flounderlib.command.argument.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface CommandViaSource<S> extends Command<S> {

    int run(S source) throws CommandSyntaxException;

    @Override
    default int run(CommandContext<S> context) throws CommandSyntaxException {
        return run(context.getSource());
    }

}

package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Function;

public interface AutofillProducts<T> {

    default <A1> P1<T, A1> group(FlCommandArg<A1> arg1) {
        return new P1<>(arg1);
    }

    final class P1<T, A1> {
        private final FlCommandArg<A1> arg1;

        public P1(FlCommandArg<A1> arg1) {
            this.arg1 = arg1;
        }

        public RequiredArgumentBuilder<ServerCommandSource, ?> apply(final Function<A1, T> applyFunc) {
            return this.arg1.createArgument();
        }
    }

}

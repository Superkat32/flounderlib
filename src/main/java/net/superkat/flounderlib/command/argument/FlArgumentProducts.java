package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function8;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.function.BiFunction;

public interface FlArgumentProducts<T> {

    FlCommandBuilder getBuilder();

    default <A1> P1<T, A1> arguments(FlCommandArg<A1> arg1) {
        return new P1<>(this.getBuilder(), arg1);
    }

    default <A1, A2> P2<T, A1, A2> arguments(FlCommandArg<A1> arg1, FlCommandArg<A2> arg2) {
        return new P2<>(this.getBuilder(), arg1, arg2);
    }

    default <A1, A2, A3> P3<T, A1, A2, A3> arguments(FlCommandArg<A1> arg1, FlCommandArg<A2> arg2, FlCommandArg<A3> arg3) {
        return new P3<>(this.getBuilder(), arg1, arg2, arg3);
    }

    default <A1, A2, A3, A4, A5, A6, A7> P7<T, A1, A2, A3, A4, A5, A6, A7> arguments(FlCommandArg<A1> arg1, FlCommandArg<A2> arg2, FlCommandArg<A3> arg3, FlCommandArg<A4> arg4, FlCommandArg<A5> arg5, FlCommandArg<A6> arg6, FlCommandArg<A7> arg7) {
        return new P7<>(this.getBuilder(), arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    class Product {
        final FlCommandBuilder builder;

        public Product(FlCommandBuilder builder) {
            this.builder = builder;
        }

        protected FlCommandBuilder apply(Command<ServerCommandSource> command, FlCommandArg<?>... args) {
            boolean allArgsOptional = Arrays.stream(args).allMatch(FlCommandArg::isOptional);
            if(allArgsOptional) {
                this.builder.executes(command);
            }

            RequiredArgumentBuilder<ServerCommandSource, ?> previous = null;

            // must go reversed because arguments are built immediately upon being added as a child,
            // which won't account for further children being added
            for (FlCommandArg<?> arg : Arrays.stream(args).toList().reversed()) {
                RequiredArgumentBuilder<ServerCommandSource, ?> argumentBuilder = arg.executesIfOptional(command).createArgument();

                if(previous == null) {
                    argumentBuilder.executes(command);
                } else {
                    argumentBuilder.then(previous);
                }
                previous = argumentBuilder;
            }

            return this.builder.argument(previous);
        }
    }

    final class P1<T, A1> extends Product {
        private final FlCommandArg<A1> arg1;

        public P1(FlCommandBuilder builder, FlCommandArg<A1> arg1) {
            super(builder);
            this.arg1 = arg1;
        }

        public FlCommandBuilder apply(final BiFunction<ServerCommandSource, A1, T> applyFunc) {
            Command<ServerCommandSource> command = context ->
                    (int) applyFunc.apply(context.getSource(), arg1.getArgument(context));

            return this.apply(command, arg1);
        }
    }

    final class P2<T, A1, A2> extends Product {
        private final FlCommandArg<A1> arg1;
        private final FlCommandArg<A2> arg2;

        public P2(FlCommandBuilder builder, FlCommandArg<A1> arg1, FlCommandArg<A2> arg2) {
            super(builder);
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        public FlCommandBuilder apply(final Function3<ServerCommandSource, A1, A2, T> applyFunc) {
            Command<ServerCommandSource> command = context ->
                    (int) applyFunc.apply(context.getSource(), arg1.getArgument(context), arg2.getArgument(context));

            return this.apply(command, arg1, arg2);
        }
    }

    final class P3<T, A1, A2, A3> extends Product {
        private final FlCommandArg<A1> arg1;
        private final FlCommandArg<A2> arg2;
        private final FlCommandArg<A3> arg3;

        public P3(FlCommandBuilder builder, FlCommandArg<A1> arg1, FlCommandArg<A2> arg2, FlCommandArg<A3> arg3) {
            super(builder);
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }

        public FlCommandBuilder apply(final Function4<ServerCommandSource, A1, A2, A3, T> applyFunc) {
            Command<ServerCommandSource> command = context ->
                    (int) applyFunc.apply(context.getSource(), arg1.getArgument(context), arg2.getArgument(context), arg3.getArgument(context));

            return this.apply(command, this.arg1, this.arg2, this.arg3);
        }
    }

    final class P7<T, A1, A2, A3, A4, A5, A6, A7> extends Product {
        private final FlCommandArg<A1> arg1;
        private final FlCommandArg<A2> arg2;
        private final FlCommandArg<A3> arg3;
        private final FlCommandArg<A4> arg4;
        private final FlCommandArg<A5> arg5;
        private final FlCommandArg<A6> arg6;
        private final FlCommandArg<A7> arg7;

        public P7(FlCommandBuilder builder, FlCommandArg<A1> arg1, FlCommandArg<A2> arg2, FlCommandArg<A3> arg3, FlCommandArg<A4> arg4, FlCommandArg<A5> arg5, FlCommandArg<A6> arg6, FlCommandArg<A7> arg7) {
            super(builder);
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
            this.arg4 = arg4;
            this.arg5 = arg5;
            this.arg6 = arg6;
            this.arg7 = arg7;
        }

        public FlCommandBuilder apply(final Function8<ServerCommandSource, A1, A2, A3, A4, A5, A6, A7, T> applyFunc) {
            Command<ServerCommandSource> command = context ->
                    (int) applyFunc.apply(context.getSource(), arg1.getArgument(context), arg2.getArgument(context), arg3.getArgument(context), arg4.getArgument(context), arg5.getArgument(context), arg6.getArgument(context), arg7.getArgument(context));

            return this.apply(command, this.arg1, this.arg2, this.arg3, this.arg4, this.arg5, this.arg6, this.arg7);
        }
    }
}

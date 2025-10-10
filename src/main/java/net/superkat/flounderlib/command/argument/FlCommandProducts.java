package net.superkat.flounderlib.command.argument;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.BiFunction;

public interface FlCommandProducts<T> {

    default <A1> P1<T, A1> argument(FlCommandArg<A1> arg1) {
        return new P1<>(arg1);
    }

    final class P1<T, A1> {
        private final FlCommandArg<A1> arg1;

        public P1(FlCommandArg<A1> arg1) {
            this.arg1 = arg1;
        }

//        public FlCommandBuilder executes(final BiFunction<ServerCommandSource, A1, T> applyFunc) {
//
//        }

        public RequiredArgumentBuilder<ServerCommandSource, ?> executes(final BiFunction<ServerCommandSource, A1, T> applyFunc) {
            return this.arg1.executes(context -> (int) applyFunc.apply(context.getSource(), arg1.getArgument(context))).createArgument();
        }
    }

//    default FlCommandProducts<T> literal(String name) {
//        return this;
//    }
//
////    default Literal<T> literal(String name) {
////        return new Literal<>();
////    }
//
//    default <A1> P1<T, A1> group(FlCommandArg<A1> arg1) {
//        return new P1<>(arg1);
//    }
//
//    final class Literal<T> {
//
//    }
//
//    final class P1<T, A1> {
//        private final FlCommandArg<A1> arg1;
//
//        public P1(FlCommandArg<A1> arg1) {
//            this.arg1 = arg1;
//        }
//
//        public RequiredArgumentBuilder<ServerCommandSource, ?> apply(final Function<A1, T> applyFunc) {
//            return this.arg1.createArgument();
//        }
//    }

}

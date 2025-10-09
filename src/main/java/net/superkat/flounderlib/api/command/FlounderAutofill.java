package net.superkat.flounderlib.api.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.superkat.flounderlib.command.argument.FlounderCommandInstance;

import java.util.function.Function;

public class FlounderAutofill<T> {

    public static <T> FlounderAutofill<T> create(
            Function<FlounderCommandInstance<T>, RequiredArgumentBuilder<ServerCommandSource, ?>> createFunc
    ) {
        return new FlounderAutofill<>(createFunc);
    }

    public final Function<FlounderCommandInstance<T>, RequiredArgumentBuilder<ServerCommandSource, ?>> createFunc;

    public FlounderAutofill(Function<FlounderCommandInstance<T>, RequiredArgumentBuilder<ServerCommandSource, ?>> createFunc) {
        this.createFunc = createFunc;
    }

    public RequiredArgumentBuilder<ServerCommandSource, ?> createArguments() {
        return this.createFunc.apply(new FlounderCommandInstance<>());
    }
}

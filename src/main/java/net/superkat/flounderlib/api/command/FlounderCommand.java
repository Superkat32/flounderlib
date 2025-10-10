package net.superkat.flounderlib.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.superkat.flounderlib.command.argument.FlCommandBuilder;
import net.superkat.flounderlib.command.argument.instance.FlounderCommandInstance;

import java.util.function.Function;

public class FlounderCommand {

    public static FlounderCommand create(Function<FlounderCommandInstance, FlCommandBuilder> instanceBuilder) {
        return new FlounderCommand(instanceBuilder);
    }

    private final Function<FlounderCommandInstance, FlCommandBuilder> builder;

    private FlounderCommand(Function<FlounderCommandInstance, FlCommandBuilder> builder) {
        this.builder = builder;
    }

    public LiteralArgumentBuilder<ServerCommandSource> build() {
        return builder.apply(new FlounderCommandInstance()).build();
    }

}

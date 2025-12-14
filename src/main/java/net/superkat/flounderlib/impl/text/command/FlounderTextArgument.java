package net.superkat.flounderlib.impl.text.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

public class FlounderTextArgument implements ArgumentType<FlounderText> {

    public static final DynamicCommandExceptionType INVALID_OPTIONS_EXCEPTION = new DynamicCommandExceptionType(
            error -> Component.translatableEscape("particle.invalidOptions", error)
    );

    private static final TagParser<?> SNBT_READER = TagParser.create(NbtOps.INSTANCE);
    private final HolderLookup.Provider registries;

    public static FlounderTextArgument flounderText(CommandBuildContext registryAccess) {
        return new FlounderTextArgument(registryAccess);
    }

    public static FlounderText getFlounderText(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, FlounderText.class);
    }

    public FlounderTextArgument(CommandBuildContext registryAccess) {
        this.registries = registryAccess;
    }

    @Override
    public FlounderText parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.read(reader);
        FlounderTextType<?> type = FlounderTextRegistry.TEXT_TYPE_REGISTRY.getValue(id);

        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        Tag game;

        if (type.codec() == null) {
            return null;
        }

        if(reader.canRead() && reader.peek() == '{') {
            game = (Tag) SNBT_READER.parseAsArgument(reader);
        } else {
            game = registryOps.emptyMap();
        }

        return type.codec().codec().parse(registryOps, game).getOrThrow(INVALID_OPTIONS_EXCEPTION::create);
    }
}

package net.superkat.flounderlib.command.text;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextType;
import net.superkat.flounderlib.text.FlounderTextRegistry;

public class FlounderTextArgument implements ArgumentType<FlounderText> {

    public static final DynamicCommandExceptionType INVALID_OPTIONS_EXCEPTION = new DynamicCommandExceptionType(
            error -> Text.stringifiedTranslatable("particle.invalidOptions", error)
    );

    private static final StringNbtReader<?> SNBT_READER = StringNbtReader.fromOps(NbtOps.INSTANCE);
    private final RegistryWrapper.WrapperLookup registries;

    public static FlounderTextArgument flounderText(CommandRegistryAccess registryAccess) {
        return new FlounderTextArgument(registryAccess);
    }

    public static FlounderText getFlounderText(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, FlounderText.class);
    }

    public FlounderTextArgument(CommandRegistryAccess registryAccess) {
        this.registries = registryAccess;
    }

    @Override
    public FlounderText parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.fromCommandInput(reader);
        FlounderTextType<?> type = FlounderTextRegistry.TEXT_TYPE_REGISTRY.get(id);

        RegistryOps<NbtElement> registryOps = registries.getOps(NbtOps.INSTANCE);
        NbtElement game;

        if (type.codec() == null) {
            return null;
        }

        if(reader.canRead() && reader.peek() == '{') {
            game = (NbtElement) SNBT_READER.readAsArgument(reader);
        } else {
            game = registryOps.emptyMap();
        }

        return type.codec().codec().parse(registryOps, game).getOrThrow(INVALID_OPTIONS_EXCEPTION::create);
    }
}

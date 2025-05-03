package net.superkat.flounderlib.command;

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
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

public class FlounderGameArgumentType implements ArgumentType<IFlounderGame> {

    public static final DynamicCommandExceptionType INVALID_OPTIONS_EXCEPTION = new DynamicCommandExceptionType(
            error -> Text.stringifiedTranslatable("particle.invalidOptions", error)
    );

    private static final StringNbtReader<?> SNBT_READER = StringNbtReader.fromOps(NbtOps.INSTANCE);
    private final RegistryWrapper.WrapperLookup registries;

    public static FlounderGameArgumentType flounderGame(CommandRegistryAccess registryAccess) {
        return new FlounderGameArgumentType(registryAccess);
    }

    public static IFlounderGame getFlounderGame(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, IFlounderGame.class);
    }

    public FlounderGameArgumentType(CommandRegistryAccess registryAccess) {
        this.registries = registryAccess;
    }

    @Override
    public IFlounderGame parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.fromCommandInput(reader);
        FlounderGameType<?> type = FlounderApi.getRegistry().get(id);

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

        return type.codec().parse(registryOps, game).getOrThrow(INVALID_OPTIONS_EXCEPTION::create);
    }
}

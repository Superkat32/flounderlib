package net.superkat.flounderlib.impl.minigame.command;

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
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.impl.minigame.game.FlounderRegistry;

public class FlounderMinigameArgumentType implements ArgumentType<FlounderableGame> {

    public static final DynamicCommandExceptionType INVALID_OPTIONS_EXCEPTION = new DynamicCommandExceptionType(
            error -> Component.translatableEscape("particle.invalidOptions", error)
    );

    private static final TagParser<?> SNBT_READER = TagParser.create(NbtOps.INSTANCE);
    private final HolderLookup.Provider registries;

    public static FlounderMinigameArgumentType flounderGame(CommandBuildContext registryAccess) {
        return new FlounderMinigameArgumentType(registryAccess);
    }

    public static FlounderableGame getFlounderGame(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, FlounderableGame.class);
    }

    public FlounderMinigameArgumentType(CommandBuildContext registryAccess) {
        this.registries = registryAccess;
    }

    @Override
    public FlounderableGame parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.read(reader);
        FlounderGameType<?> type = FlounderRegistry.getRegistry().getValue(id);

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

        return type.codec().parse(registryOps, game).getOrThrow(INVALID_OPTIONS_EXCEPTION::create);
    }
}

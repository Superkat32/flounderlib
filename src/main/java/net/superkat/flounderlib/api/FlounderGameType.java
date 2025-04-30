package net.superkat.flounderlib.api;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public record FlounderGameType<T extends IFlounderGame>(Identifier id, Codec<T> codec, FlounderGameFactory<T> factory) {

}

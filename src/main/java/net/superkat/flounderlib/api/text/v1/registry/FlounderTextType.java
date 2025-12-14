package net.superkat.flounderlib.api.text.v1.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;

/**
 * Common component used for dispatching/remembering codecs and identifiers
 *
 * @param <T> FlounderText type
 */
public record FlounderTextType<T extends FlounderText>(Identifier id, MapCodec<T> codec) {

}

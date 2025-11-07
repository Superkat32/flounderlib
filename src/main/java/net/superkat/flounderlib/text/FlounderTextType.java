package net.superkat.flounderlib.text;

import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.FlounderText;
import net.superkat.flounderlib.api.text.FlounderTextRenderer;

import java.util.function.Function;

public record FlounderTextType<T extends FlounderText>(Identifier id, Codec<T> codec, FlounderTextRenderer<T> renderer, Function<Text, T> factory) {

    public T create(Text text) {
        return this.factory.apply(text);
    }

}

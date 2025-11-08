package net.superkat.flounderlib.api.text.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public interface FlounderTextParams {

    Text getText();

    Codec<? extends FlounderTextParams> getCodec();

    static record Default(Text text) implements FlounderTextParams {
        public static final Codec<Default> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        TextCodecs.CODEC.fieldOf("text").forGetter(Default::text)
                ).apply(instance,  Default::new)
        );

        @Override
        public Text getText() {
            return this.text;
        }

        @Override
        public Codec<? extends FlounderTextParams> getCodec() {
            return CODEC;
        }
    }

}

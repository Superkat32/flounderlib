package net.superkat.flounderlib.api.text.type;

import com.mojang.serialization.Codec;
import net.superkat.flounderlib.api.text.FlounderTextFactory;
import net.superkat.flounderlib.api.text.FlounderTextRenderer;

public record FlounderTextType<T extends FlounderTextParams>(Codec<T> codec, FlounderTextFactory<T> factory, FlounderTextRenderer renderer) {

}

//public abstract class FlounderTextType<T extends FlounderTextParams> {
//    private FlounderTextFactory<T> factory;
//    private FlounderTextRenderer renderer;
//
//    public abstract Codec<T> getCodec();
//
//    public FlounderTextFactory<T> getFactory() {
//        return factory;
//    }
//
//    public void setFactory(FlounderTextFactory<T> factory) {
//        this.factory = factory;
//    }
//
//    public FlounderTextRenderer getRenderer() {
//        return renderer;
//    }
//
//    public void setRenderer(FlounderTextRenderer renderer) {
//        this.renderer = renderer;
//    }
//}

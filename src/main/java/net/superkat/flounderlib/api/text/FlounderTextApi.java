package net.superkat.flounderlib.api.text;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.type.FlounderTextParams;
import net.superkat.flounderlib.api.text.type.FlounderTextType;
import net.superkat.flounderlib.duck.FlounderInGameHud;
import net.superkat.flounderlib.text.client.FlounderClientTextManager;

public class FlounderTextApi {

    public static <T extends FlounderTextParams> FlounderTextType<T> register(Identifier id, Codec<T> codec, FlounderTextFactory<T> factory) {
        return register(id, codec, factory, FlounderTextRenderer.DEFAULT_INSTANCE);
    }

    public static <T extends FlounderTextParams> FlounderTextType<T> register(Identifier id, Codec<T> codec, FlounderTextFactory<T> factory, FlounderTextRenderer renderer) {
        FlounderTextType<T> type = new FlounderTextType<>(codec, factory, renderer);
        return FlounderClientTextManager.register(id, type);
    }


//    public static <T extends FlTextParams> SimpleFlounderTextType register(Identifier id, FlounderTextFactory<T> factory) {
//        return register(id, factory, FlounderTextRenderer.DEFAULT_INSTANCE);
//    }
//
//    public static <T extends FlTextParams> SimpleFlounderTextType register(Identifier id, FlounderTextFactory<T> factory, FlounderTextRenderer renderer) {
//        return FlounderClientTextManager.register(id, )
//    }

//    public static <T extends FlounderText> FlounderTextType<T> register(Identifier id, Codec<T> codec, FlounderTextRenderer<T> renderer, Function<Text, T> factory) {
//        return FlounderClientTextManager.register(new FlounderTextType<>(id, codec, renderer, factory));
//    }

    public static FlounderClientTextManager getTextManager() {
        return ((FlounderInGameHud) MinecraftClient.getInstance().inGameHud).flounderlib$getFlounderTextManager();
    }

}

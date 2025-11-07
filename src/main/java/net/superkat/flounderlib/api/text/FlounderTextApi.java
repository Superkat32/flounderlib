package net.superkat.flounderlib.api.text;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.duck.FlounderInGameHud;
import net.superkat.flounderlib.text.FlounderTextType;
import net.superkat.flounderlib.text.client.FlounderClientTextManager;

import java.util.function.Function;

public class FlounderTextApi {

    public static <T extends FlounderText> FlounderTextType<T> register(Identifier id, Codec<T> codec, FlounderTextRenderer<T> renderer, Function<Text, T> factory) {
        return FlounderClientTextManager.register(new FlounderTextType<>(id, codec, renderer, factory));
    }

    public static FlounderClientTextManager getTextManager() {
        return ((FlounderInGameHud) MinecraftClient.getInstance().inGameHud).flounderlib$getFlounderTextManager();
    }

}

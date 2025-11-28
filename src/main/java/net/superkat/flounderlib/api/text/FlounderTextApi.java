package net.superkat.flounderlib.api.text;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextRenderer;
import net.superkat.flounderlib.api.text.core.FlounderTextType;
import net.superkat.flounderlib.network.text.FlounderTextS2CPacket;
import net.superkat.flounderlib.text.FlounderTextRegistry;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class FlounderTextApi {

    public static void sendText(ServerPlayerEntity player, Identifier id, FlounderText text) {
        sendText(List.of(player), id, text);
    }

    public static void sendText(Collection<ServerPlayerEntity> players, Identifier id, FlounderText text) {
        FlounderTextS2CPacket packet = new FlounderTextS2CPacket(id, text);
        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, packet);
        }
    }

    public static <T extends FlounderText> FlounderTextType<T> register(Identifier id, MapCodec<T> codec, FlounderTextRenderer<T> renderer) {
        return FlounderTextRegistry.register(new FlounderTextType<>(id, codec, renderer));
    }


    public static <T extends FlounderText> void registerRenderer(Identifier id, BiConsumer<Identifier, FlounderTextRenderer<T>> idAndRendererConsumer) {
        idAndRendererConsumer.accept(id, getRendererFromId(id));
    }

    public static void registerDefaultRenderer(Identifier id) {
        registerRenderer(id, (identifier, flounderTextRenderer) ->
                HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, identifier, flounderTextRenderer)
        );
    }

    public static <T extends FlounderText> FlounderTextRenderer<T> getRendererFromId(Identifier id) {
        return FlounderTextRegistry.getRenderer(id);
    }

}

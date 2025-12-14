package net.superkat.flounderlib.api.text.v1;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.network.packets.FlounderTextS2CPacket;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

import java.util.Collection;

public class FlounderTextApi {

    public static <T extends FlounderText> FlounderTextType<T> register(Identifier textTypeId, MapCodec<T> textCodec) {
        return FlounderTextRegistry.register(new FlounderTextType<>(textTypeId, textCodec));
    }

    public static <T extends FlounderText> void send(T text, ServerPlayer player) {
        CustomPacketPayload payload = createSendPacket(text.getFlounderTextType().id(), text);
        ServerPlayNetworking.send(player, payload);
    }

    public static <T extends FlounderText> void send(T text, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            send(text, player);
        }
    }

    private static CustomPacketPayload createSendPacket(Identifier textTypeId, FlounderText text) {
        return new FlounderTextS2CPacket(textTypeId, text);
    }

}

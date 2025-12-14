package net.superkat.flounderlib.impl.minigame.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;

public record FlounderGameRemoveS2CPacket(PackedFlGameInfo gameInfo) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_game_remove");
    public static final CustomPacketPayload.Type<FlounderGameRemoveS2CPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, FlounderGameRemoveS2CPacket> CODEC = StreamCodec.composite(
            PackedFlGameInfo.PACKET_CODEC, FlounderGameRemoveS2CPacket::gameInfo,
            FlounderGameRemoveS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

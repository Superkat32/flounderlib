package net.superkat.flounderlib.impl.minigame.network.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;

public record FlounderGameAddS2CPacket(PackedFlGameInfo gameInfo) implements CustomPayload {
    public static final Identifier GAME_ADD_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_add");
    public static final CustomPayload.Id<FlounderGameAddS2CPacket> ID = new CustomPayload.Id<>(GAME_ADD_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderGameAddS2CPacket> CODEC = PacketCodec.tuple(
            PackedFlGameInfo.PACKET_CODEC, FlounderGameAddS2CPacket::gameInfo,
            FlounderGameAddS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

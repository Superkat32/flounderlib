package net.superkat.flounderlib.network.sync.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;

public record FlounderGameDestroyS2CPacket(int minigameId) implements CustomPayload {
    public static final Identifier GAME_DESTROY_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_destroy");
    public static final CustomPayload.Id<FlounderGameDestroyS2CPacket> ID = new CustomPayload.Id<>(GAME_DESTROY_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderGameDestroyS2CPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, game -> game.minigameId, FlounderGameDestroyS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

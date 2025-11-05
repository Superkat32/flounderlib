package net.superkat.flounderlib.network.sync.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;

public record FlounderGameRemoveS2CPacket(Identifier gameId, int minigameId) implements CustomPayload {
    public static final Identifier FLOUNDER_GAME_REMOVE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_remove");
    public static final CustomPayload.Id<FlounderGameRemoveS2CPacket> ID = new CustomPayload.Id<>(FLOUNDER_GAME_REMOVE_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderGameRemoveS2CPacket> CODEC = PacketCodec.of(
            FlounderGameRemoveS2CPacket::write, FlounderGameRemoveS2CPacket::fromBuf
    );

    public static FlounderGameRemoveS2CPacket fromBuf(RegistryByteBuf buf) {
        Identifier gameId = buf.readIdentifier();
        int minigameId = buf.readVarInt();
        return new FlounderGameRemoveS2CPacket(gameId, minigameId);
    }

    public void write(RegistryByteBuf buf) {
        buf.writeIdentifier(this.gameId);
        buf.writeVarInt(this.minigameId);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

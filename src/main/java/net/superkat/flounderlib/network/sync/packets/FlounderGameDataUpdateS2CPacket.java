package net.superkat.flounderlib.network.sync.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.sync.FlounderSyncData;
import net.superkat.flounderlib.minigame.FlounderRegistry;

public record FlounderGameDataUpdateS2CPacket<D extends FlounderSyncData>(Identifier gameTypeId, int minigameId, D data) implements CustomPayload {
    public static final Identifier FLOUNDER_GAME_DATA_UPDATE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_data_update");
    public static final CustomPayload.Id<FlounderGameDataUpdateS2CPacket<?>> ID = new CustomPayload.Id<>(FLOUNDER_GAME_DATA_UPDATE_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderGameDataUpdateS2CPacket<?>> CODEC = PacketCodec.of(
            FlounderGameDataUpdateS2CPacket::write, FlounderGameDataUpdateS2CPacket::fromBuf
    );

    @SuppressWarnings("unchecked")
    public static <D extends FlounderSyncData> FlounderGameDataUpdateS2CPacket<D> fromBuf(RegistryByteBuf buf) {
        Identifier gameTypeId = buf.readIdentifier();
        int minigameId = buf.readVarInt();

        FlounderGameType<?> gameType = FlounderRegistry.getRegistry().get(gameTypeId);
        assert gameType != null;

        PacketCodec<RegistryByteBuf, D> dataPacketCodec = (PacketCodec<RegistryByteBuf, D>) gameType.dataPacketCodec();
        assert dataPacketCodec != null;

        D data = dataPacketCodec.decode(buf);
        return new FlounderGameDataUpdateS2CPacket<>(gameTypeId, minigameId, data);
    }

    @SuppressWarnings("unchecked")
    public void write(RegistryByteBuf buf) {
        FlounderGameType<?> gameType = FlounderRegistry.getRegistry().get(this.gameTypeId);
        assert gameType != null;

        PacketCodec<RegistryByteBuf, D> dataPacketCodec = (PacketCodec<RegistryByteBuf, D>) gameType.dataPacketCodec();
        assert dataPacketCodec != null;

        buf.writeIdentifier(this.gameTypeId);
        buf.writeVarInt(this.minigameId);
        dataPacketCodec.encode(buf, this.data);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

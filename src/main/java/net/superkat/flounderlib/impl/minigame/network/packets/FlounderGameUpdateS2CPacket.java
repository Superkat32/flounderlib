package net.superkat.flounderlib.impl.minigame.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncValue;
import net.superkat.flounderlib.impl.minigame.sync.FlounderStateSyncerImpl;

import java.util.ArrayList;
import java.util.List;

public record FlounderGameUpdateS2CPacket(PackedFlGameInfo gameInfo, FlounderStateSyncer<?, ?> syncer, List<FlSyncValue.Packed<?>> values) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_game_update");
    public static final CustomPacketPayload.Type<FlounderGameUpdateS2CPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, FlounderGameUpdateS2CPacket> CODEC = StreamCodec.ofMember(
            FlounderGameUpdateS2CPacket::write, FlounderGameUpdateS2CPacket::fromBuf
    );

    public static FlounderGameUpdateS2CPacket fromBuf(RegistryFriendlyByteBuf buf) {
        PackedFlGameInfo gameInfo = PackedFlGameInfo.PACKET_CODEC.decode(buf);

        int syncerId = buf.readByte();
        FlounderStateSyncer<?, ?> syncer = FlounderStateSyncerImpl.getSyncer(syncerId);

        List<FlSyncValue.Packed<?>> list = new ArrayList<>();
        int i;
        while ((i = buf.readUnsignedByte()) != 0) {
            list.add(FlSyncValue.Packed.fromBuf(buf, syncer, i));
        }

        return new FlounderGameUpdateS2CPacket(gameInfo, syncer, list);
    }

    public void write(RegistryFriendlyByteBuf buf) {
        PackedFlGameInfo.PACKET_CODEC.encode(buf, this.gameInfo);

        buf.writeByte(this.gameInfo.gameType().stateSyncer().getId());
        for (FlSyncValue.Packed<?> value : values) {
            value.write(buf);
        }

        buf.writeByte(0); // Reading a zero means the list is done (key IDs start at 1)
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

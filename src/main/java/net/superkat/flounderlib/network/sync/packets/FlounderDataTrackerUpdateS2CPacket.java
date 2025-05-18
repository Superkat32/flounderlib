package net.superkat.flounderlib.network.sync.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;

import java.util.ArrayList;
import java.util.List;

public record FlounderDataTrackerUpdateS2CPacket(int minigameId, List<FlounderDataTracker.SerializedEntry<?>> values) implements CustomPayload {
    public static final Identifier FLOUNDER_DATA_TRACKER_UPDATE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_set_tracker_data");
    public static final CustomPayload.Id<FlounderDataTrackerUpdateS2CPacket> ID = new CustomPayload.Id<>(FLOUNDER_DATA_TRACKER_UPDATE_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderDataTrackerUpdateS2CPacket> CODEC = PacketCodec.of(
            FlounderDataTrackerUpdateS2CPacket::write, FlounderDataTrackerUpdateS2CPacket::new
    );

    public static final int END_MARKER_ID = 255;

    public FlounderDataTrackerUpdateS2CPacket(RegistryByteBuf buf) {
        this(buf.readVarInt(), readList(buf));
    }

    private static List<FlounderDataTracker.SerializedEntry<?>> readList(RegistryByteBuf buf) {
        List<FlounderDataTracker.SerializedEntry<?>> list = new ArrayList<>();

        int i;
        while((i = buf.readUnsignedByte()) != END_MARKER_ID) {
            list.add(FlounderDataTracker.SerializedEntry.fromBuf(buf, i));
        }

        return list;
    }

    private static void writeList(RegistryByteBuf buf, List<FlounderDataTracker.SerializedEntry<?>> values) {
        for (FlounderDataTracker.SerializedEntry<?> entry : values) {
            entry.write(buf);
        }

        buf.writeByte(END_MARKER_ID);
    }

    private void write(RegistryByteBuf buf) {
        buf.writeVarInt(this.minigameId);
        writeList(buf, this.values);
    }


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

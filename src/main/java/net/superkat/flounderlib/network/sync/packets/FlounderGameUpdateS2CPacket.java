package net.superkat.flounderlib.network.sync.packets;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;

public record FlounderGameUpdateS2CPacket(int minigameId, NbtCompound nbt) implements CustomPayload {
    public static final Identifier FLOUNDER_GAME_UPDATE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_update");
    public static final CustomPayload.Id<FlounderGameUpdateS2CPacket> ID = new CustomPayload.Id<>(FLOUNDER_GAME_UPDATE_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderGameUpdateS2CPacket> CODEC = PacketCodec.of(
            FlounderGameUpdateS2CPacket::write, FlounderGameUpdateS2CPacket::new
    );

    public FlounderGameUpdateS2CPacket(RegistryByteBuf buf) {
        this(buf.readVarInt(), buf.readNbt());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeVarInt(this.minigameId);
        buf.writeNbt(this.nbt);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

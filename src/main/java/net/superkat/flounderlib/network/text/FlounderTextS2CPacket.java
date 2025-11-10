package net.superkat.flounderlib.network.text;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.text.FlounderTextRegistry;

public record FlounderTextS2CPacket(Identifier id, FlounderText text) implements CustomPayload {
    public static final Identifier FLOUNDER_TEXT_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_text_create_packet");
    public static final CustomPayload.Id<FlounderTextS2CPacket> ID = new CustomPayload.Id<>(FLOUNDER_TEXT_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderTextS2CPacket> CODEC = PacketCodec.of(
        FlounderTextS2CPacket::write, FlounderTextS2CPacket::fromBuf
    );

    public static FlounderTextS2CPacket fromBuf(RegistryByteBuf buf) {
        Identifier id = buf.readIdentifier();

        NbtCompound nbt = buf.readNbt();
        FlounderText text = nbt.get("text", FlounderTextRegistry.FLOUNDER_TEXT_CODEC).get();
        return new FlounderTextS2CPacket(id, text);
    }

    public void write(RegistryByteBuf buf) {
        buf.writeIdentifier(this.id);

        NbtCompound nbt = new NbtCompound();
        nbt.put("text", FlounderTextRegistry.FLOUNDER_TEXT_CODEC, this.text);
        buf.writeNbt(nbt);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

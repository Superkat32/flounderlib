package net.superkat.flounderlib.network.text;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.FlounderText;
import net.superkat.flounderlib.text.client.FlounderClientTextManager;

public record FlounderTextS2CPacket<T extends FlounderText>(Identifier textId, T text) implements CustomPayload {
    public static final String TEXT_NBT_ID = "text_nbt";
    public static final Identifier FLOUNDER_TEXT_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_text_packet");
    public static final CustomPayload.Id<FlounderTextS2CPacket<?>> ID = new CustomPayload.Id<>(FLOUNDER_TEXT_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderTextS2CPacket<?>> CODEC = PacketCodec.of(
        FlounderTextS2CPacket::write, FlounderTextS2CPacket::fromBuf
    );

    @SuppressWarnings("unchecked")
    public static <T extends FlounderText> FlounderTextS2CPacket<T> fromBuf(RegistryByteBuf buf) {
        Identifier textId = buf.readIdentifier();
        NbtCompound nbt = buf.readNbt();

        Codec<T> codec = (Codec<T>) FlounderClientTextManager.getRegistry().get(textId).codec();
        T flounderText = nbt.get(TEXT_NBT_ID, codec).orElseThrow();

        return new FlounderTextS2CPacket<>(textId, flounderText);
    }

    @SuppressWarnings("unchecked")
    public void write(RegistryByteBuf buf) {
        NbtCompound nbt = new NbtCompound();
        Codec<T> codec = (Codec<T>) FlounderClientTextManager.getRegistry().get(this.textId).codec();
        nbt.put(TEXT_NBT_ID, codec, this.text);

        buf.writeIdentifier(this.textId);
        buf.writeNbt(nbt);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package net.superkat.flounderlib.impl.text.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

public record FlounderTextS2CPacket(Identifier textTypeId, FlounderText text) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_text_create_packet");
    public static final CustomPacketPayload.Type<FlounderTextS2CPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, FlounderTextS2CPacket> CODEC = StreamCodec.ofMember(
        FlounderTextS2CPacket::write, FlounderTextS2CPacket::fromBuf
    );

    public static FlounderTextS2CPacket fromBuf(RegistryFriendlyByteBuf buf) {
        Identifier id = buf.readIdentifier();

        CompoundTag nbt = buf.readNbt();
        FlounderText text = nbt.read("text", FlounderTextRegistry.FLOUNDER_TEXT_CODEC).get();
        return new FlounderTextS2CPacket(id, text);
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeIdentifier(this.textTypeId);

        CompoundTag nbt = new CompoundTag();
        nbt.store("text", FlounderTextRegistry.FLOUNDER_TEXT_CODEC, this.text);
        buf.writeNbt(nbt);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

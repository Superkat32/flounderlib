package net.superkat.flounderlib.network.fun.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;

public record RepoTextS2CPacket(String text) implements CustomPayload {
    public static final Identifier IDENTIFIER = Identifier.of(FlounderLib.MOD_ID, "flounder_repo_text");
    public static final CustomPayload.Id<RepoTextS2CPacket> ID = new CustomPayload.Id<>(IDENTIFIER);
    public static final PacketCodec<RegistryByteBuf, RepoTextS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.STRING, RepoTextS2CPacket::text, RepoTextS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

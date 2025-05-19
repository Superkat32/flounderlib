package net.superkat.flounderlib.network.sync.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

public record FlounderGameCreationS2CPacket(int minigameId, IFlounderGame game) implements CustomPayload {
    public static final Identifier FLOUNDER_GAME_CREATION_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_creation");
    public static final CustomPayload.Id<FlounderGameCreationS2CPacket> ID = new CustomPayload.Id<>(FLOUNDER_GAME_CREATION_ID);
    public static final PacketCodec<RegistryByteBuf, FlounderGameCreationS2CPacket> CODEC = PacketCodec.of(FlounderGameCreationS2CPacket::write, FlounderGameCreationS2CPacket::fromBuf);

    public static FlounderGameCreationS2CPacket fromBuf(RegistryByteBuf buf) {
        int gameIntId = buf.readVarInt();
        Identifier gameId = buf.readIdentifier();
        FlounderGameType<?> gameType = FlounderApi.getRegistry().get(gameId);
        IFlounderGame game = gameType.packetCodec().decode(buf);
        return new FlounderGameCreationS2CPacket(gameIntId, game);
    }

    @SuppressWarnings("unchecked")
    public void write(RegistryByteBuf buf) {
        Identifier gameId = game.getIdentifier();
        FlounderGameType<?> gameType = FlounderApi.getRegistry().get(gameId);
        if(gameType.shouldSync()) {
            // Super safe !!!! :) :) :) (probably)
            PacketCodec<RegistryByteBuf, Object> packetCodec = (PacketCodec<RegistryByteBuf, Object>) gameType.packetCodec();
            if(packetCodec == null) return;

            buf.writeVarInt(this.minigameId);
            buf.writeIdentifier(gameId);
            packetCodec.encode(buf, this.game);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

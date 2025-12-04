package net.superkat.flounderlib.impl.minigame.packed;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;

public record PackedFlGameInfo(FlounderGameType<?> gameType, int gameId) {

    public static final PacketCodec<RegistryByteBuf, PackedFlGameInfo> PACKET_CODEC = PacketCodec.tuple(
            FlounderGameType.PACKET_CODEC, PackedFlGameInfo::gameType,
            PacketCodecs.VAR_INT, PackedFlGameInfo::gameId,
            PackedFlGameInfo::new
    );

    public static PackedFlGameInfo fromGame(FlounderableGame game) {
        return new PackedFlGameInfo(game.getGameType(), game.getMinigameId());
    }

}

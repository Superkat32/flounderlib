package net.superkat.flounderlib.impl.minigame.packed;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;

public record PackedFlGameInfo(FlounderGameType<?> gameType, int gameId) {

    public static final StreamCodec<RegistryFriendlyByteBuf, PackedFlGameInfo> PACKET_CODEC = StreamCodec.composite(
            FlounderGameType.PACKET_CODEC, PackedFlGameInfo::gameType,
            ByteBufCodecs.VAR_INT, PackedFlGameInfo::gameId,
            PackedFlGameInfo::new
    );

    public static PackedFlGameInfo fromGame(FlounderableGame game) {
        return new PackedFlGameInfo(game.getGameType(), game.getMinigameId());
    }

}

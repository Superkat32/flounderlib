package net.superkat.flounderlib.api.gametype;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.IFlounderGame;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class FlounderGameType<T extends IFlounderGame> {
    private final Identifier id;
    private final @Nullable Codec<T> codec;
    private final @Nullable PacketCodec<ByteBuf, T> packetCodec;
    private final int allowedActiveInstances;
    private final int searchDistance;

    private final Map<Integer, T> games = new HashMap<>();

    public FlounderGameType(
            Identifier id,
            @Nullable Codec<T> codec,
            @Nullable PacketCodec<ByteBuf, T> packetCodec,
            int allowedActiveInstances,
            int searchDistance
    ) {
        this.id = id;
        this.codec = codec;
        this.packetCodec = packetCodec;
        this.allowedActiveInstances = allowedActiveInstances;
        this.searchDistance = searchDistance;
    }

    public Collection<T> getActiveGames() {
        return this.games.values();
    }

    @ApiStatus.Internal
    public boolean isPersistent() {
        return this.codec != null;
    }

    @ApiStatus.Internal
    public boolean shouldSync() {
        return this.packetCodec != null;
    }

    @SuppressWarnings("unchecked")
    @ApiStatus.Internal
    public void addActiveGame(IFlounderGame game) {
        this.games.put(game.getMinigameId(), (T) game);
    }

    @ApiStatus.Internal
    public void removeActiveGame(IFlounderGame game) {
        this.games.remove(game.getMinigameId());
    }

    @ApiStatus.Internal
    public void clearActiveGames() {
        this.games.clear();
    }

    public Identifier id() {
        return this.id;
    }

    public @Nullable Codec<T> codec() {
        return this.codec;
    }

    public @Nullable PacketCodec<ByteBuf, T> packetCodec() {
        return this.packetCodec;
    }

    public int allowedActiveInstances() {
        return this.allowedActiveInstances;
    }

    public int searchDistance() {
        return this.searchDistance;
    }


}

package net.superkat.flounderlib.api.minigame.gametype;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.api.minigame.sync.FlounderSyncData;

// goals:
// - singleton by default
// - not persistent by default
// FlounderGameType - singleton normally
// MultiFlounderGameType - allow multiple (MultitonFlounderGameType?)
// option to disallow other gametypes nearby
// singleton for per world and all worlds
public record FlounderGameType<T extends FlounderableGame>(
        Identifier id,
        Codec<T> codec,
        int distance,
        int padding,
        boolean overlap,
        boolean singleton,
        PacketCodec<RegistryByteBuf, ? extends FlounderSyncData> dataPacketCodec
) {

    public static <T extends FlounderableGame> Builder<T> create(Identifier id) {
        return new Builder<T>(id);
    }

    public static <T extends FlounderableGame> Builder<T> create(Identifier id, Codec<T> codec) {
        return new Builder<T>(id).codec(codec);
    }

    public boolean persistent() {
        return this.codec != null;
    }

    public static class Builder<T extends FlounderableGame> {
        private final Identifier id;
        private Codec<T> codec = null;
        private int distance = 48;
        private int padding = 0;
        private boolean overlap = true;
        private boolean singleton = false;

        private PacketCodec<RegistryByteBuf, ? extends FlounderSyncData> dataPacketCodec = null;

        protected Builder(Identifier id) {
            this.id = id;
        }

        public Builder<T> codec(Codec<T> codec) {
            this.codec = codec;
            return this;
        }

        public Builder<T> distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder<T> padding(int padding) {
            this.padding = padding;
            return this;
        }

        public Builder<T> overlap(boolean overlap) {
            this.overlap = overlap;
            return this;
        }

        public Builder<T> singleton(boolean singleton) {
            this.singleton = singleton;
            return this;
        }

        public Builder<T> sync(PacketCodec<RegistryByteBuf, ? extends FlounderSyncData> dataPacketCodec) {
            this.dataPacketCodec = dataPacketCodec;
            return this;
        }

        public FlounderGameType<T> build() {
            return new FlounderGameType<>(
                    this.id, this.codec,
                    this.distance, this.padding, this.overlap,
                    this.singleton,
                    this.dataPacketCodec
            );
        }
    }
}

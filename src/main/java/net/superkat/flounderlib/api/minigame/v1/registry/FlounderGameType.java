package net.superkat.flounderlib.api.minigame.v1.registry;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.impl.minigame.game.FlounderRegistry;

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

        boolean synced,
        FlounderStateSyncer<T, ?> stateSyncer
) {

    public static final PacketCodec<RegistryByteBuf, FlounderGameType<?>> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeIdentifier(value.id),
            buf -> FlounderRegistry.getRegistry().get(buf.readIdentifier())
    );

    public static <T extends FlounderableGame> Builder<T> create(Identifier id) {
        return new Builder<T>(id);
    }

    public static <T extends FlounderableGame> Builder<T> create(Identifier id, Codec<T> codec) {
        return new Builder<T>(id).codec(codec);
    }

    public boolean persistent() {
        return this.codec != null;
    }

    public void onInit() {
        if(this.synced) {
            this.stateSyncer.setFlounderGameType(this);
        }
    }

    public static class Builder<T extends FlounderableGame> {
        private final Identifier id;
        private Codec<T> codec = null;
        private int distance = 48;
        private int padding = 0;
        private boolean overlap = true;
        private boolean singleton = false;

        private boolean synced = false;
        private FlounderStateSyncer<T, ?> stateSyncer = null;
//        private PacketCodec<RegistryByteBuf, ? extends FlounderSyncData> dataPacketCodec = null;

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

        public Builder<T> synced(boolean synced) {
            this.synced = synced;
            return this;
        }

        public Builder<T> synced(FlounderStateSyncer<T, ?> stateSyncer) {
            this.synced = true;
            this.stateSyncer = stateSyncer;
            return this;
        }

        public FlounderGameType<T> build() {
            FlounderGameType<T> gameType = new FlounderGameType<>(
                    this.id, this.codec,
                    this.distance, this.padding, this.overlap,
                    this.singleton,

                    this.synced,
                    this.stateSyncer
            );

            gameType.onInit();
            return gameType;
        }
    }
}

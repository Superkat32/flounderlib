package net.superkat.flounderlib.api.gametype;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.FlounderableGame;

public class FlounderGameTypeBuilder<T extends FlounderableGame> {

    // TODO - I don't know if I like this setup or not - there's some weird type error stuff going on with this#builder

    public static <T extends FlounderableGame> FlounderGameType<T> createSimple(Identifier id) {
        return new FlounderGameTypeBuilder<T>(id).build();
    }

    public static <T extends FlounderableGame> FlounderGameType<T> createPersistent(Identifier id, Codec<T> codec) {
        return new FlounderGameTypeBuilder<T>(id).codec(codec).build();
    }

    public static <T extends FlounderableGame> FlounderGameType<T> createSingleton(Identifier id) {
        return new FlounderGameTypeBuilder<T>(id).singleton(true).build();
    }

    public static <T extends FlounderableGame> FlounderGameTypeBuilder<T> builder(Identifier id) {
        return new FlounderGameTypeBuilder<>(id);
    }

    private final Identifier id;
    private Codec<T> codec = null;
    private int distance = 96;
    private int paddingDistance = 96;
    private boolean overlap = false;
    private boolean singleton = false;

    private FlounderGameTypeBuilder(Identifier id) {
        this.id = id;
    }

    public FlounderGameTypeBuilder<T> codec(Codec<T> codec) {
        this.codec = codec;
        return this;
    }

    public FlounderGameTypeBuilder<T> distance(int distance) {
        this.distance = distance;
        return this;
    }

    public FlounderGameTypeBuilder<T> paddingDistance(int paddingDistance) {
        this.paddingDistance = paddingDistance;
        return this;
    }

    public FlounderGameTypeBuilder<T> overlap(boolean overlap) {
        this.overlap = overlap;
        return this;
    }

    public FlounderGameTypeBuilder<T> singleton(boolean singleton) {
        this.singleton = singleton;
        return this;
    }

    public FlounderGameType<T> build() {
        return new FlounderGameType<>(
                this.id, this.codec,
                this.distance, this.paddingDistance, this.overlap,
                this.singleton
        );
    }
}

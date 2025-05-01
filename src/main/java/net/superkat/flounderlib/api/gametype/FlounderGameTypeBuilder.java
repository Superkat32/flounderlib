package net.superkat.flounderlib.api.gametype;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.IFlounderGame;
import org.jetbrains.annotations.Nullable;

public class FlounderGameTypeBuilder<T extends IFlounderGame> {
    private final Identifier id;
    @Nullable
    private Codec<T> codec = null;
    private int searchDist = 96;

    public FlounderGameTypeBuilder(Identifier id) {
        this.id = id;
    }

    public FlounderGameTypeBuilder<T> setPersistentCodec(Codec<T> codec) {
        this.codec = codec;
        return this;
    }

    public FlounderGameTypeBuilder<T> setSearchDistance(int searchDist) {
        this.searchDist = searchDist;
        return this;
    }

    public FlounderGameType<T> build() {
        return new FlounderGameType<>(id, codec, searchDist);
    }

}

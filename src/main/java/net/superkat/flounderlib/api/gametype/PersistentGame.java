package net.superkat.flounderlib.api.gametype;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.superkat.flounderlib.minigame.FlounderGame;

public interface PersistentGame {
    Codec<?> getCodec();

    default FlounderGame fromNbt(NbtCompound compound) {
        return null;
    }

    default NbtCompound toNbt(NbtCompound compound) {
        return new NbtCompound();
    }

}

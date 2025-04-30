package net.superkat.flounderlib.api.nbt;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NbtCompound;
import net.superkat.flounderlib.minigame.FlounderGame;

import java.util.Map;
import java.util.function.Function;

public class GameNbtBuilder {

    public Map<String, Function<FlounderGame, ?>> values = Maps.newHashMap();

    public <T>void add(String field, Function<FlounderGame, T> nbt) {
        values.put(field, nbt);
    }

    public void writeNbt(NbtCompound compound) {
    }

}

package net.superkat.flounderlib.minigame;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import net.superkat.flounderlib.test.FakeMinigame;

public record FlounderId(int id, FakeMinigame game) {
    public static final Codec<FlounderId> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("id").forGetter(FlounderId::id),
                    FakeMinigame.CODEC.fieldOf("game").forGetter(FlounderId::game)
            ).apply(instance, FlounderId::new)
    );

    public static FlounderId fromMapEntry(Entry<FakeMinigame> entry) {
        return new FlounderId(entry.getIntKey(), entry.getValue());
    }
}

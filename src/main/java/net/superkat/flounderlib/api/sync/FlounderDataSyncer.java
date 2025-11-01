package net.superkat.flounderlib.api.sync;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.superkat.flounderlib.api.minigame.FlounderableGame;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FlounderDataSyncer<T extends FlounderableGame> {

    public final AtomicInteger id = new AtomicInteger();
    public final Map<Integer, FlSyncValue<?>> values = new Int2ObjectArrayMap<>();

    public <V> FlounderDataSyncer(Function<T, V> getter, BiConsumer<T, V> setter) {
//        values.put(id.getAndIncrement(), new FlSyncValue<>(value));
    }

}

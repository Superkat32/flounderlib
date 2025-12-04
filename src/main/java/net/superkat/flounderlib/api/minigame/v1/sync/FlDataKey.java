package net.superkat.flounderlib.api.minigame.v1.sync;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FlDataKey<T> {

    @ApiStatus.Internal
    private static final Map<Integer, FlDataKey<?>> KEYS = new Int2ObjectArrayMap<>();
    @ApiStatus.Internal
    private static final AtomicInteger KEY_INDEX = new AtomicInteger(1);

    public static FlDataKey<Boolean> ofBoolean() {
        return of(PacketCodecs.BOOLEAN);
    }

    public static FlDataKey<Integer> ofInt() {
        return of(PacketCodecs.INTEGER);
    }

    public static FlDataKey<String> ofString() {
        return of(PacketCodecs.STRING);
    }

    public static FlDataKey<Vec3d> ofVec3d() {
        return of(Vec3d.PACKET_CODEC);
    }

    public static FlDataKey<Text> ofText() {
        return of(TextCodecs.PACKET_CODEC);
    }

    public static <T> FlDataKey<T> of(PacketCodec<ByteBuf, T> packetCodec) {
        int i = KEY_INDEX.getAndIncrement();
        return new FlDataKey<>(i, packetCodec);
    }

    private final int id;
    private final PacketCodec<ByteBuf, T> packetCodec;

    private FlDataKey(int id, PacketCodec<ByteBuf, T> packetCodec) {
        this.id = id;
        this.packetCodec = packetCodec;

        KEYS.put(id, this);
    }

    @ApiStatus.Internal
    public void write(ByteBuf buf, T value) {
        this.packetCodec.encode(buf, value);
    }

    @ApiStatus.Internal
    public T read(ByteBuf buf) {
        return this.packetCodec.decode(buf);
    }

    @ApiStatus.Internal
    public int getId() {
        return this.id;
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static <T> FlDataKey<T> getKey(int id) {
        return (FlDataKey<T>) KEYS.get(id);
    }
}

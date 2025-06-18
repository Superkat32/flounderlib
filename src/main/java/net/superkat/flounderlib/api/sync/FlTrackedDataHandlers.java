package net.superkat.flounderlib.api.sync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.network.sync.FlTrackedDataHandler;
import net.superkat.flounderlib.network.sync.FlTrackedDataHandlerRegistry;
import org.joml.Vector3f;

import java.util.UUID;

public class FlTrackedDataHandlers {

    public static final FlTrackedDataHandler<Byte> BYTE = createAndRegister(PacketCodecs.BYTE, "byte");
    public static final FlTrackedDataHandler<Boolean> BOOLEAN = createAndRegister(PacketCodecs.BOOLEAN, "boolean");
    public static final FlTrackedDataHandler<Short> SHORT = createAndRegister(PacketCodecs.SHORT, "short");
    public static final FlTrackedDataHandler<Integer> INTEGER = createAndRegister(PacketCodecs.INTEGER, "integer");
    public static final FlTrackedDataHandler<Long> LONG = createAndRegister(PacketCodecs.LONG, "long");
    public static final FlTrackedDataHandler<Float> FLOAT = createAndRegister(PacketCodecs.FLOAT, "float");
    public static final FlTrackedDataHandler<Double> DOUBLE = createAndRegister(PacketCodecs.DOUBLE, "double");
    public static final FlTrackedDataHandler<String> STRING = createAndRegister(PacketCodecs.STRING, "string");

    public static final FlTrackedDataHandler<Text> TEXT = createAndRegister(TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, "text");
    public static final FlTrackedDataHandler<UUID> UUID = createAndRegister(Uuids.PACKET_CODEC, "uuid");
    public static final FlTrackedDataHandler<BlockPos> BLOCK_POS = createAndRegister(BlockPos.PACKET_CODEC, "block_pos");
    public static final FlTrackedDataHandler<Vector3f> VECTOR_3f = createAndRegister(PacketCodecs.VECTOR_3F, "vector_3f");

    private static <T> FlTrackedDataHandler<T> createAndRegister(PacketCodec<? super RegistryByteBuf, T> packetCodec, String name) {
        return createAndRegister(packetCodec, Identifier.of(FlounderLib.MOD_ID, name));
    }

    public static <T> FlTrackedDataHandler<T> createAndRegister(PacketCodec<? super RegistryByteBuf, T> packetCodec, Identifier id) {
        return FlTrackedDataHandlerRegistry.createAndRegister(packetCodec, id);
    }

}

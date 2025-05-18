package net.superkat.flounderlib.api.sync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.network.sync.FlTrackedDataHandler;
import net.superkat.flounderlib.network.sync.FlTrackedDataHandlerRegistry;

public class FlTrackedDataHandlers {

    public static final FlTrackedDataHandler<Boolean> BOOLEAN = createAndRegister(PacketCodecs.BOOLEAN, "boolean");
    public static final FlTrackedDataHandler<Integer> INTEGER = createAndRegister(PacketCodecs.INTEGER, "integer");
    public static final FlTrackedDataHandler<String> STRING = createAndRegister(PacketCodecs.STRING, "string");

    public static <T> FlTrackedDataHandler<T> createAndRegister(PacketCodec<? super RegistryByteBuf, T> packetCodec, String name) {
        return createAndRegister(packetCodec, Identifier.of(FlounderLib.MOD_ID, name));
    }

    public static <T> FlTrackedDataHandler<T> createAndRegister(PacketCodec<? super RegistryByteBuf, T> packetCodec, Identifier id) {
        return FlTrackedDataHandlerRegistry.createAndRegister(packetCodec, id);
    }

}

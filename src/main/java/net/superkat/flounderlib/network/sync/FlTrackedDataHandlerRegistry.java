package net.superkat.flounderlib.network.sync;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;

public class FlTrackedDataHandlerRegistry {
    public static final Identifier FL_TRACKED_DATA_HANDLER_ID = Identifier.of(FlounderLib.MOD_ID, "fl_tracked_data_handler");
    public static final RegistryKey<Registry<FlTrackedDataHandler<?>>> FL_TRACKED_DATA_HANDLER_REGISTRY_KEY = RegistryKey.ofRegistry(FL_TRACKED_DATA_HANDLER_ID);
    public static final Registry<FlTrackedDataHandler<?>> HANDLER_REGISTRY = FabricRegistryBuilder.createSimple(FL_TRACKED_DATA_HANDLER_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();;

    public static <T> FlTrackedDataHandler<T> createAndRegister(PacketCodec<? super RegistryByteBuf, T> packetCodec, String name) {
        return createAndRegister(packetCodec, Identifier.of(FlounderLib.MOD_ID, name));
    }

    public static <T> FlTrackedDataHandler<T> createAndRegister(PacketCodec<? super RegistryByteBuf, T> packetCodec, Identifier id) {
        FlTrackedDataHandler<T> handler = FlTrackedDataHandler.create(packetCodec);
        Registry.register(HANDLER_REGISTRY, id, handler);
        return handler;
    }

}

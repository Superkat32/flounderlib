package net.superkat.flounderlib.api;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.gametype.FlounderGameTypeBuilder;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.duck.FlounderServerWorld;
import net.superkat.flounderlib.minigame.FlounderServerGameManager;

import java.util.Map;

/**
 * Manages minigames.<br><br>
 * Registering, starting, finding, and removing minigames can be done here.
 *
 * @see FlounderUtils
 */
public class FlounderApi {
    // Known registered minigames - always available.
    // Mapped to an Identifier for encoding/decoding NBT
    private static final Map<Identifier, FlounderGameType<?>> registry = Maps.newHashMap();

    /**
     * Add a minigame to a world. This will begin ticking the minigame
     *
     * @param world The ServerWorld the minigame is in.
     * @param game The minigame to add
     */
    // Called to add a minigame, starting it to tick & be managed by FlounderLib
    public static void addGame(ServerWorld world, IFlounderGame game) {
        FlounderServerGameManager manager = getFlounderGameManager(world);

        manager.addGame(world, game);
    }

    /**
     * End a minigame. Calls the minigames {@link IFlounderGame#invalidate()} method.
     *
     * @param game The minigame to end
     */
    public static void endGame(IFlounderGame game) {
        game.invalidate();
    }

    public static int getMinigameIntId(IFlounderGame game) {
        return game.getMinigameId();
    }

    public static int bruteForceGetMinigameIntId(ServerWorld world, IFlounderGame game) {
        FlounderServerGameManager manager = getFlounderGameManager(world);
        return manager.getMinigameIntId(game);
    }

    // The idea here is that FlounderGameTypeBuilder may get more options for building, and I can't create
    // every combination of methods here, so instead you'd just manually register the built
    // FlounderGameType using the Builder, while I provide the most common use cases (non-persistent, persistent, & synced)
    public static <T extends IFlounderGame> FlounderGameType<T> registerType(FlounderGameType<T> type) {
        registry.put(type.id(), type);
        return type;
    }

    // TODO - check if this breaks anything
    public static <T extends IFlounderGame> FlounderGameType<T> create(Identifier id) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).build();
        return registerType(type);
    }

    public static <T extends IFlounderGame> FlounderGameType<T> createPersistent(Identifier id, Codec<T> persistentCodec) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).setPersistentCodec(persistentCodec).build();
        return registerType(type);
    }

    public static <T extends SyncedFlounderGame> FlounderGameType<T> createSynced(
            Identifier id,
            PacketCodec<RegistryByteBuf, T> packetCodec
    ) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).setPacketCodec(packetCodec).build();
        return registerType(type);
    }

    public static <T extends SyncedFlounderGame> FlounderGameType<T> createPersistentSynced(
            Identifier id, Codec<T> persistentCodec,
            PacketCodec<RegistryByteBuf, T> packetCodec
    ) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id)
                .setPersistentCodec(persistentCodec)
                .setPacketCodec(packetCodec).build();
        return registerType(type);
    }

    public static Map<Identifier, FlounderGameType<?>> getRegistry() {
        return registry;
    }

    public static FlounderServerGameManager getFlounderGameManager(ServerWorld serverWorld) {
        return ((FlounderServerWorld) serverWorld).flounderlib$getFlounderGameManager();
    }

}

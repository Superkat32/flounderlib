package net.superkat.flounderlib.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.gametype.FlounderGameTypeBuilder;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.duck.FlounderClientWorld;
import net.superkat.flounderlib.duck.FlounderServerWorld;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import net.superkat.flounderlib.minigame.FlounderServerGameManager;
import net.superkat.flounderlib.minigame.registry.FlounderGameTypeRegistry;

/**
 * Manages minigames.<br><br>
 * Registering, starting, finding, and removing minigames can be done here.
 *
 * @see FlounderUtils
 */
public class FlounderApi {
    /**
     * Add a minigame to the world and start ticking it.<br><br>
     *
     * This is the entry method to allow FlounderLib to start handling your game, while still allowing your game class to have a custom constructor.
     *
     * @param world The ServerWorld the minigame is in
     * @param game The minigame to add
     */
    public static void addGame(ServerWorld world, IFlounderGame game) {
        FlounderServerGameManager manager = getFlounderServerGameManager(world);

        manager.addGame(game);
    }

    /**
     * End a minigame by calling its {@link IFlounderGame#invalidate()} method.
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
        FlounderGameManager manager = getFlounderGameManager(world);
        return manager.getMinigameIntId(game);
    }



    public static FlounderGameManager getFlounderGameManager(World world) {
        if(world.isClient) {
            return ((FlounderClientWorld) world).flounderlib$getFlounderClientGameManager();
        } else {
            return ((FlounderServerWorld) world).flounderlib$getFlounderGameManager();
        }
    }

    public static FlounderServerGameManager getFlounderServerGameManager(ServerWorld serverWorld) {
        return ((FlounderServerWorld) serverWorld).flounderlib$getFlounderGameManager();
    }



    // The idea here is that FlounderGameTypeBuilder may get more options for building, and I can't create
    // every combination of methods here, so instead you'd just manually register the built
    // FlounderGameType using the Builder, while I provide the most common use cases (non-persistent, persistent, & synced)
    public static <T extends IFlounderGame> FlounderGameType<T> registerType(FlounderGameType<T> type) {
        return FlounderGameTypeRegistry.register(type);
    }

    public static <T extends IFlounderGame> FlounderGameType<T> create(Identifier id) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).build();
        return registerType(type);
    }

    public static <T extends IFlounderGame> FlounderGameType<T> createPersistent(Identifier id, Codec<T> persistentCodec) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).setPersistentCodec(persistentCodec).build();
        return registerType(type);
    }

    public static <T extends SyncedFlounderGame<?>> FlounderGameType<T> createSynced(
            Identifier id,
            PacketCodec<RegistryByteBuf, T> packetCodec
    ) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).setPacketCodec(packetCodec).build();
        return registerType(type);
    }

    public static <T extends SyncedFlounderGame<?>> FlounderGameType<T> createPersistentSynced(
            Identifier id, Codec<T> persistentCodec,
            PacketCodec<ByteBuf, T> packetCodec
    ) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id)
                .setPersistentCodec(persistentCodec)
                .setPacketCodec(packetCodec).build();
        return registerType(type);
    }

    public static Registry<FlounderGameType<?>> getRegistry() {
        return FlounderGameTypeRegistry.getRegistry();
    }

}

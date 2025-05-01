package net.superkat.flounderlib.api;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.gametype.FlounderGameTypeBuilder;
import net.superkat.flounderlib.duck.FlounderWorld;
import net.superkat.flounderlib.minigame.FlounderGameManager;

import java.util.Map;

/**
 * Manages all minigame types, as well as helper methods for minigames.<br><br>
 * Registering, starting, finding, and removing minigames can be done here.<br><br>
 * Spawning/removing multiple entities in an area, spawning an entity from a list of entities, teleporting multiple players, rewarding multiple players items, and syncing data to multiple players can also be done here.
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
        FlounderGameManager manager = getFlounderGameManager(world);

        manager.addGame(world, game);
    }

    // The idea here is that FlounderGameTypeBuilder may get more options for building, and I can't create
    // every combination of methods here, so instead you'd just manually register the built
    // FlounderGameType using the Builder, while I provide the most common use cases (create & createPersistent)
    public static <T extends IFlounderGame> FlounderGameType<T> registerType(FlounderGameType<T> type) {
        if(type.isPersistent()) {
            registry.put(type.id(), type);
        }
        return type;
    }

    public static <T extends IFlounderGame> FlounderGameType<T> create(Identifier id) {
        return new FlounderGameTypeBuilder<T>(id).build();
    }

    public static <T extends IFlounderGame> FlounderGameType<T> createPersistent(Identifier id, Codec<T> codec) {
        FlounderGameType<T> type = new FlounderGameTypeBuilder<T>(id).setPersistentCodec(codec).build();
        registry.put(id, type);
        return type;
    }

    public static Map<Identifier, FlounderGameType<?>> getRegistry() {
        return registry;
    }

    public static FlounderGameManager getFlounderGameManager(ServerWorld serverWorld) {
        return ((FlounderWorld) serverWorld).flounderlib$getFlounderGameManager();
    }

}

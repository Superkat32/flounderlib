package net.superkat.flounderlib.api;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.duck.FlounderWorld;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public static <T extends IFlounderGame> T createGame(FlounderGameType<T> type, ServerWorld world, BlockPos pos) {
        FlounderGameManager manager = getFlounderGameManager(world);
        if(manager == null) return null;

        return manager.createGame(type, world, pos);
    }

    public static void endGame(IFlounderGame game) {
        game.invalidate();
    }

    public static <T extends IFlounderGame> FlounderGameType<T> register(Identifier id, Codec<T> codec, FlounderGameFactory<T> factory) {
        FlounderGameType<T> type = new FlounderGameType<>(id, codec, factory);
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

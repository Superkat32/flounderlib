package net.superkat.flounderlib.impl.minigame.game;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.minigame.v1.FlounderApi;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FlounderNbtHandler {
    public static final String GAMES_LIST_ID = "games";
    public static final String GAME_INT_ID_ID = "id";

    @SuppressWarnings("unchecked")
    public static void serializeMinigames(Map<Integer, FlounderableGame> games, ServerLevel world, CompoundTag nbt) {
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        if(manager == null) {
            FlounderLib.LOGGER.warn("Could not serialize minigames - FlounderGameManager was null!");
            return;
        }

        // Loop through all games, find their FlounderGameType via their Identifier id, then serialize via their codec(if they should)
        ListTag gameNbtList = new ListTag();

        for (Map.Entry<Integer, FlounderableGame> entry : games.entrySet()) {
            int intId = entry.getKey();
            FlounderableGame game = entry.getValue();

            Identifier gameId = game.getIdentifier();
            FlounderGameType<?> gameType = FlounderRegistry.getRegistry().getValue(gameId);

            if(gameType == null) {
                FlounderLib.LOGGER.warn("Could not serialize minigame {} - its FlounderGameType was null!", gameId);
                continue;
            }

            // Skip serializing minigame if it shouldn't be persistent(this being false also means there isn't a codec)
            if (!gameType.persistent()) continue;

            Codec<Object> codec = (Codec<Object>) gameType.codec();
            if(codec == null) {
                FlounderLib.LOGGER.warn("Could not serialize minigame {} - its codec was null!", gameId);
                continue;
            }

            RegistryOps<Tag> registryOps = world.registryAccess().createSerializationContext(NbtOps.INSTANCE);
            codec.encodeStart(registryOps, game)
                    .ifError(partial -> {
                        FlounderLib.LOGGER.warn("Could not serialize minigame {} - Error:", gameId);
                        FlounderLib.LOGGER.warn(partial.message());
                    }).ifSuccess(serialized -> {
                        CompoundTag compound = new CompoundTag();
                        compound.putInt(GAME_INT_ID_ID, intId);
                        compound.put(gameId.toString(), serialized);
                        gameNbtList.add(compound);
                    });
        }

        nbt.put(GAMES_LIST_ID, gameNbtList);
    }

    @Nullable
    public static Map<Integer, FlounderableGame> deserializeMinigames(ServerLevel world, CompoundTag nbt) {
        Optional<ListTag> optionalList = nbt.getList(GAMES_LIST_ID);
        if(optionalList.isEmpty()) return null;

        Registry<FlounderGameType<?>> registry = FlounderRegistry.getRegistry();
        Map<Integer, FlounderableGame> gameMap = Maps.newHashMap();
        ListTag list = optionalList.get();

        // Loop through all entries - for each entry, loop through each key
        // (should only be the minigame's Identifier id and number int id)
        // If the key isn't the number int id, assume it's the Identifier
        // Find FlounderGameType from Identifier id, then deserialize from its codec
        for (CompoundTag compound : list.compoundStream().toList()) {
            for (String key : compound.keySet()) {
                if(Objects.equals(key, GAME_INT_ID_ID)) continue;

                Identifier gameId = Identifier.parse(key);
                if(!registry.containsKey(gameId)) {
                    FlounderLib.LOGGER.warn("Unknown minigame id {} found when deserializing - skipping", key);
                    continue;
                }

                FlounderGameType<?> type = registry.getValue(gameId);
                if(type == null) {
                    FlounderLib.LOGGER.warn("Unknown minigame type {} found when deserializing - skipping", key);
                    continue;
                }

                if(!type.persistent()) continue;

                Codec<?> codec = type.codec();
                if(codec == null) {
                    FlounderLib.LOGGER.warn("Could not deserialize minigame {} - its codec was null!", key);
                    continue;
                }

                RegistryOps<Tag> registryOps = world.registryAccess().createSerializationContext(NbtOps.INSTANCE);
                codec.parse(registryOps, compound.get(key))
                        .ifError(partial -> {
                            FlounderLib.LOGGER.warn("Could not deserialize minigame {} - Error:", key);
                            FlounderLib.LOGGER.warn(partial.message());
                        }).ifSuccess(deserialized -> {
                            int optionalIntId = compound.getIntOr(GAME_INT_ID_ID, 0);
                            FlounderableGame game = (FlounderableGame) deserialized;
                            game.init(world, optionalIntId);
                            gameMap.put(optionalIntId, game);
                        });
            }
        }

        if(gameMap.isEmpty()) return null;
        return gameMap;
    }
}

package net.superkat.flounderlib.impl.minigame.game;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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

//    public static final Codec<Box> BOX_CODEC = Codec.DOUBLE
//            .listOf()
//            .comapFlatMap(
//                    values -> Util.decodeFixedLengthList(values, 6).map(list -> new Box(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5))),
//                    box -> List.of(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
//            )
//            .stable();

    @SuppressWarnings("unchecked")
    public static void serializeMinigames(Map<Integer, FlounderableGame> games, ServerWorld world, NbtCompound nbt) {
        FlounderGameManager manager = FlounderApi.getGameManager(world);
        if(manager == null) {
            FlounderLib.LOGGER.warn("Could not serialize minigames - FlounderGameManager was null!");
            return;
        }

        // Loop through all games, find their FlounderGameType via their Identifier id, then serialize via their codec(if they should)
        NbtList gameNbtList = new NbtList();

        for (Map.Entry<Integer, FlounderableGame> entry : games.entrySet()) {
            int intId = entry.getKey();
            FlounderableGame game = entry.getValue();

            Identifier gameId = game.getIdentifier();
            FlounderGameType<?> gameType = FlounderRegistry.getRegistry().get(gameId);

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

            RegistryOps<NbtElement> registryOps = world.getRegistryManager().getOps(NbtOps.INSTANCE);
            codec.encodeStart(registryOps, game)
                    .ifError(partial -> {
                        FlounderLib.LOGGER.warn("Could not serialize minigame {} - Error:", gameId);
                        FlounderLib.LOGGER.warn(partial.message());
                    }).ifSuccess(serialized -> {
                        NbtCompound compound = new NbtCompound();
                        compound.putInt(GAME_INT_ID_ID, intId);
                        compound.put(gameId.toString(), serialized);
                        gameNbtList.add(compound);
                    });
        }

        nbt.put(GAMES_LIST_ID, gameNbtList);
    }

    @Nullable
    public static Map<Integer, FlounderableGame> deserializeMinigames(ServerWorld world, NbtCompound nbt) {
        Optional<NbtList> optionalList = nbt.getList(GAMES_LIST_ID);
        if(optionalList.isEmpty()) return null;

        Registry<FlounderGameType<?>> registry = FlounderRegistry.getRegistry();
        Map<Integer, FlounderableGame> gameMap = Maps.newHashMap();
        NbtList list = optionalList.get();

        // Loop through all entries - for each entry, loop through each key
        // (should only be the minigame's Identifier id and number int id)
        // If the key isn't the number int id, assume it's the Identifier
        // Find FlounderGameType from Identifier id, then deserialize from its codec
        for (NbtCompound compound : list.streamCompounds().toList()) {
            for (String key : compound.getKeys()) {
                if(Objects.equals(key, GAME_INT_ID_ID)) continue;

                Identifier gameId = Identifier.of(key);
                if(!registry.containsId(gameId)) {
                    FlounderLib.LOGGER.warn("Unknown minigame id {} found when deserializing - skipping", key);
                    continue;
                }

                FlounderGameType<?> type = registry.get(gameId);
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

                RegistryOps<NbtElement> registryOps = world.getRegistryManager().getOps(NbtOps.INSTANCE);
                codec.parse(registryOps, compound.get(key))
                        .ifError(partial -> {
                            FlounderLib.LOGGER.warn("Could not deserialize minigame {} - Error:", key);
                            FlounderLib.LOGGER.warn(partial.message());
                        }).ifSuccess(deserialized -> {
                            int optionalIntId = compound.getInt(GAME_INT_ID_ID, 0);
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

package net.superkat.flounderlib.minigame;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.nbt.FlounderNbtHandler;

import java.util.Iterator;
import java.util.Map;

/**
 * Manages all active minigames, including calling their `tick` method and saving/reading their NBT.
 */
public class FlounderServerGameManager extends PersistentState implements FlounderGameManager {
    public static final String ID = "flounder_games";
    public static final String TICKS_ID = "ticks";
    public static final String NEXT_ID_ID = "next_id";

    public static PersistentStateType<FlounderServerGameManager> getPersistentStateType(ServerWorld world) {
        return new PersistentStateType<>(
                ID,
                FlounderServerGameManager::new,
                getCodec(world),
                null
        );
    }

    public static Codec<FlounderServerGameManager> getCodec(ServerWorld world) {
        return Codec.of(new Encoder<>() {
            @Override
            public <T> DataResult<T> encode(FlounderServerGameManager input, DynamicOps<T> ops, T prefix) {
                NbtCompound nbt = new NbtCompound();
                FlounderNbtHandler.serializeMinigames(input.getGames(), world, nbt);
                nbt.putInt(NEXT_ID_ID, input.nextId);
                nbt.putInt(TICKS_ID, input.ticks);
                return DataResult.success((T) nbt);
            }
        }, new Decoder<>() {
            @Override
            public <T> DataResult<Pair<FlounderServerGameManager, T>> decode(DynamicOps<T> ops, T input) {
                NbtCompound nbt = (NbtCompound) ops.convertTo(NbtOps.INSTANCE, input);
                Map<Integer, IFlounderGame> gameMap = FlounderNbtHandler.deserializeMinigames(world, nbt);
                int next_id = nbt.getInt(NEXT_ID_ID, 0);
                int ticks = nbt.getInt(TICKS_ID, 0);
                return DataResult.success(Pair.of(new FlounderServerGameManager(gameMap, next_id, ticks), ops.empty()));
            }
        });
    }

    // Active, ticking minigames - only available when minigame(s) are running.
    public final Int2ObjectMap<IFlounderGame> games = new Int2ObjectOpenHashMap<>();
    public int nextId = 0;
    public int ticks = 0;

    public FlounderServerGameManager() {
        this.markDirty();
    }

    public FlounderServerGameManager(Map<Integer, IFlounderGame> games, int nextId, int ticks) {
        if(games != null) {
            for (Map.Entry<Integer, IFlounderGame> entry : games.entrySet()) {
                int intId = entry.getKey();
                IFlounderGame game = entry.getValue();
                this.games.put(intId, game);
            }
        }
        this.nextId = nextId;
        this.ticks = ticks;
    }

    @Override
    public void tick() {
        this.ticks++;

        Iterator<IFlounderGame> iterator = this.games.values().iterator();
        while(iterator.hasNext()) {
            IFlounderGame game = iterator.next();

            if(game.isInvalidated()) {
                iterator.remove();
                this.markDirty();
            } else {
                game.tick();
            }
        }

        if(this.ticks % 200 == 0) {
            this.markDirty();
        }
    }

    public void addGame(ServerWorld world, IFlounderGame game) {
        int intId = this.getNextId();
        game.initialize(world, intId);
        this.games.put(intId, game);
        this.markDirty();
    }

//    public int getMinigameIntId(IFlounderGame game) {
//        for (Int2ObjectMap.Entry<IFlounderGame> entry : this.games.int2ObjectEntrySet()) {
//            IFlounderGame entryGame = entry.getValue();
//            if(!entryGame.equals(game)) continue;
//            return entry.getIntKey();
//        }
//        return -1;
//    }

    private int getNextId() {
        return this.nextId++;
    }

    @Override
    public Map<Integer, IFlounderGame> getGames() {
        return this.games;
    }

    @Override
    public boolean isClient() {
        return false;
    }
}

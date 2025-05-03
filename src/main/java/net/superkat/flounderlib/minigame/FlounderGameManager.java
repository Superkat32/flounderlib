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
public class FlounderGameManager extends PersistentState {
    public static final String ID = "flounder_games";
    public static final String TICKS_ID = "ticks";
    public static final String NEXT_ID_ID = "next_id";

    public static PersistentStateType<FlounderGameManager> getPersistentStateType(ServerWorld world) {
        return new PersistentStateType<>(
                ID,
                FlounderGameManager::new,
                getCodec(world),
                null
        );
    }

    public static Codec<FlounderGameManager> getCodec(ServerWorld world) {
        return Codec.of(new Encoder<>() {
            @Override
            public <T> DataResult<T> encode(FlounderGameManager input, DynamicOps<T> ops, T prefix) {
                NbtCompound nbt = new NbtCompound();
                FlounderNbtHandler.serializeMinigames(input.getGamesMap(), world, nbt);
                nbt.putInt(NEXT_ID_ID, input.nextId);
                nbt.putInt(TICKS_ID, input.ticks);
                return DataResult.success((T) nbt);
            }
        }, new Decoder<>() {
            @Override
            public <T> DataResult<Pair<FlounderGameManager, T>> decode(DynamicOps<T> ops, T input) {
                NbtCompound nbt = (NbtCompound) ops.convertTo(NbtOps.INSTANCE, input);
                Map<Integer, IFlounderGame> gameMap = FlounderNbtHandler.deserializeMinigames(world, nbt);
                int next_id = nbt.getInt(NEXT_ID_ID, 0);
                int ticks = nbt.getInt(TICKS_ID, 0);
                return DataResult.success(Pair.of(new FlounderGameManager(gameMap, next_id, ticks), ops.empty()));
            }
        });
    }

    // Active, ticking minigames - only available when minigame(s) are running.
    public final Int2ObjectMap<IFlounderGame> games = new Int2ObjectOpenHashMap<>();
    public int nextId = 0;
    public int ticks = 0;

    public FlounderGameManager() {
        this.markDirty();
    }

    public FlounderGameManager(Map<Integer, IFlounderGame> games, int nextId, int ticks) {
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

    public void tick() {
        this.ticks++;

        Iterator<IFlounderGame> iterator = this.games.values().iterator();
        while(iterator.hasNext()) {
            IFlounderGame game = iterator.next();

            if(game.shouldRemove()) {
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
        game.create(world);

        int id = this.getNextId();
        this.games.put(id, game);
        this.markDirty();
    }

    private int getNextId() {
        return this.nextId++;
    }

    public Map<Integer, IFlounderGame> getGamesMap() {
        return this.games;
    }

}

package net.superkat.flounderlib.impl.minigame.game;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FlounderGameManager extends PersistentState {
    public static final String ID = "flounder_games";
    public static final String TICKS_ID = "ticks";
    public static final String NEXT_ID_ID = "next_id";

    private final ServerWorld world;
    private final Map<Integer, FlounderableGame> games = new Int2ObjectOpenHashMap<>();
    private int nextId = 0;
    private int ticks = 0;

    public static PersistentStateType<FlounderGameManager> getPersistentStateType(ServerWorld world) {
        return new PersistentStateType<>(
                ID,
                () -> new FlounderGameManager(world),
                getCodec(world),
                null
        );
    }

    public static Codec<FlounderGameManager> getCodec(ServerWorld world) {
        return Codec.of(new Encoder<>() {
            @Override
            public <T> DataResult<T> encode(FlounderGameManager input, DynamicOps<T> ops, T t) {
                NbtCompound nbt = new NbtCompound();
                FlounderNbtHandler.serializeMinigames(input.getGames(), world, nbt);
                nbt.putInt(NEXT_ID_ID, input.nextId);
                nbt.putInt(TICKS_ID, input.ticks);
                return DataResult.success((T) nbt);
            }
        }, new Decoder<>() {
            @Override
            public <T> DataResult<Pair<FlounderGameManager, T>> decode(DynamicOps<T> ops, T input) {
                NbtCompound nbt = (NbtCompound) ops.convertTo(NbtOps.INSTANCE, input);
                Map<Integer, FlounderableGame> gameMap = FlounderNbtHandler.deserializeMinigames(world, nbt);
                int next_id = nbt.getInt(NEXT_ID_ID, 0);
                int ticks = nbt.getInt(TICKS_ID, 0);
                return DataResult.success(Pair.of(new FlounderGameManager(world, gameMap, next_id, ticks), ops.empty()));
            }
        });
    }

    public FlounderGameManager(ServerWorld world) {
        this.world = world;
        this.markDirty();
    }

    public FlounderGameManager(ServerWorld world, Map<Integer, FlounderableGame> games, int nextId, int ticks) {
        this.world = world;
        if(games != null) {
            for (Map.Entry<Integer, FlounderableGame> entry : games.entrySet()) {
                int intId = entry.getKey();
                FlounderableGame game = entry.getValue();
                this.games.put(intId, game);
            }
        }
        this.nextId = nextId;
        this.ticks = ticks;
    }

    public void tick() {
        this.ticks++;

        Iterator<FlounderableGame> iterator = this.games.values().iterator();
        while (iterator.hasNext()) {
            FlounderableGame game = iterator.next();

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

    public void addGame(FlounderableGame game) {
        int minigameId = this.getNextId();
        game.init(this.world, minigameId);
        this.games.put(minigameId, game);
        this.markDirty();
    }

    public List<FlounderableGame> findGamesAt(BlockPos pos) {
        return this.getGames().values().stream()
                .filter(game -> game.containsBlockPos(pos))
                .toList();
    }

    public <T extends FlounderableGame> List<FlounderableGame> findGameTypeAt(FlounderGameType<T> gameType, BlockPos pos) {
        return this.getGames().values().stream()
                .filter(game -> game.getGameType() == gameType)
                .filter(game -> game.containsBlockPos(pos))
                .toList();
    }

    public <T extends FlounderableGame> boolean doesGameExist(FlounderGameType<T> gameType) {
        return this.games.values().stream().anyMatch(game -> game.getGameType() == gameType);
    }

    public Map<Integer, FlounderableGame> getGames() {
        return this.games;
    }

    public int getNextId() {
        return this.nextId++;
    }

}

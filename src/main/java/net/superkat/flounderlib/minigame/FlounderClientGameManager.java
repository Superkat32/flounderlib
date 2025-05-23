package net.superkat.flounderlib.minigame;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.world.ClientWorld;
import net.superkat.flounderlib.api.IFlounderGame;

import java.util.Map;

public class FlounderClientGameManager implements FlounderGameManager {
    public final ClientWorld world;
    public final Int2ObjectMap<IFlounderGame> games = new Int2ObjectOpenHashMap<>();

    public FlounderClientGameManager(ClientWorld world) {
        this.world = world;
    }

    public void addGame(int intId, IFlounderGame game) {
        game.initialize(this.world, intId);
        this.games.put(intId, game);
    }

    @Override
    public void tick() {
        for (IFlounderGame game : this.games.values()) {
            game.tick();
        }
    }

    public void removeGame(int intId) {
        this.games.remove(intId);
    }

    @Override
    public Map<Integer, IFlounderGame> getGames() {
        return this.games;
    }

    @Override
    public boolean isClient() {
        return true;
    }
}

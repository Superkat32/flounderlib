package net.superkat.flounderlib.api.minigame.listener;

import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FlounderGameListener<T extends IFlounderGame> {
    private final Map<Integer, T> games = new HashMap<>();
    private final Map<Integer, T> renderableGames = new HashMap<>();

    private final FlounderGameType<T> type;
    private final int maxRenderableGames;

    public FlounderGameListener(FlounderGameType<T> type) {
        this.type = type;
        this.maxRenderableGames = 7;
    }

    public void addGame(T game) {
        this.games.put(game.getMinigameId(), game);
        this.renderableGames.put(game.getMinigameId(), game);
    }

    public void removeGame(T game) {
        this.games.remove(game.getMinigameId());
        this.renderableGames.remove(game.getMinigameId());
    }

    public Collection<T> getRenderableGames() {
        return this.renderableGames.values();
    }


}

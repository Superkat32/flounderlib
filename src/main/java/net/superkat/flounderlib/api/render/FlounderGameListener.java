package net.superkat.flounderlib.api.render;

import com.google.common.collect.Maps;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

import java.util.Map;

public class FlounderGameListener<T extends IFlounderGame> {
    private final Map<Integer, T> games = Maps.newHashMap();
    private final Map<Integer, T> renderableGames = Maps.newHashMap();

    private final FlounderGameType<T> type;
    private final int maxRenderableGames;

    public FlounderGameListener(FlounderGameType<T> type) {
        this.type = type;
        this.maxRenderableGames = type.searchDistance();
    }

    public void addGame(T game) {
        this.games.put(game.getMinigameId(), game);
    }

    public void removeGame(T game) {
        this.games.remove(game.getMinigameId());
    }


}

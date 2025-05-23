package net.superkat.flounderlib.api.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

import java.util.Map;

public interface FlounderRenderer<T extends IFlounderGame> {

    default void drawAllGamesHud(DrawContext context, RenderTickCounter counter) {
        this.draw(context, counter, null, 0);
    }

    void draw(DrawContext context, RenderTickCounter counter, T game, int gameIndex);

    default void renderAllGamesWorld(WorldRenderContext context) {

    }

    void render(WorldRenderContext context, T game, int gameIndex);



    void addGame(T game);

    void removeGame(T game);

    int getTotalRenderableGames();

    Map<Integer, T> getAllGames();

    Map<Integer, T> getRenderableGames();

    @FunctionalInterface
    interface Factory<T extends IFlounderGame> {
        FlounderRenderer<T> create(FlounderGameType<T> flounderGameType);
    }
}

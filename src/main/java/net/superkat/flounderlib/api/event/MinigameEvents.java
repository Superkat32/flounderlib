package net.superkat.flounderlib.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.IFlounderGame;

public final class MinigameEvents {

    public static final Event<CreateGame> AFTER_MINIGAME_CREATE = EventFactory.createArrayBacked(CreateGame.class, callbacks -> (world, game) -> {
        for (CreateGame event : callbacks) {
            event.onMinigameCreate(world, game);
        }
    });

    public static final Event<RemoveGame> AFTER_MINIGAME_REMOVAL = EventFactory.createArrayBacked(RemoveGame.class, callbacks -> (world, game) -> {
        for (RemoveGame event : callbacks) {
            event.onMinigameRemoval(world, game);
        }
    });

    @FunctionalInterface
    public interface CreateGame {
        void onMinigameCreate(World world, IFlounderGame game);
    }

    @FunctionalInterface
    public interface RemoveGame {
        void onMinigameRemoval(World world, IFlounderGame game);
    }

}

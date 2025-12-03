package net.superkat.flounderlib.impl.minigame;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.superkat.flounderlib.api.minigame.v1.FlounderApi;
import net.superkat.flounderlib.impl.minigame.command.FlounderMinigameCommands;
import net.superkat.flounderlib.impl.minigame.network.FlounderMinigamePackets;

public class FlounderMinigameInit {

    public static void init() {
        FlounderMinigamePackets.init();
        FlounderMinigameCommands.init();

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            FlounderApi.getGameManager(world).tick();
        });

        ServerLifecycleEvents.BEFORE_SAVE.register((server, flush, force) -> {
            for (ServerWorld world : server.getWorlds()) {
                FlounderApi.getGameManager(world).markDirty();
            }
        });
    }

}

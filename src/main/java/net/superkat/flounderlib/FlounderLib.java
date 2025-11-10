package net.superkat.flounderlib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.text.builtin.BuiltinFlounderTextRenderers;
import net.superkat.flounderlib.command.FlounderLibCommands;
import net.superkat.flounderlib.network.FlounderPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLib implements ModInitializer {
	public static final String MOD_ID = "flounderlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FlounderPackets.init();
		FlounderLibCommands.init();
		BuiltinFlounderTextRenderers.init();

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
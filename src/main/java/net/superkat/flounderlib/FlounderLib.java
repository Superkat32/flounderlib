package net.superkat.flounderlib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.FlounderGameType;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import net.superkat.flounderlib.test.FakeMinigame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLib implements ModInitializer {

	public static final String MOD_ID = "flounderlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@SuppressWarnings("UnstableApiUsage")
	public static final AttachmentType<FakeMinigame> TEST_MINIGAME = AttachmentRegistry.createPersistent(
			Identifier.of(MOD_ID, "fakeminigame"),
			FakeMinigame.CODEC
	);

	public static final Identifier FAKE_MINIGAME_ID = Identifier.of(MOD_ID, "fake_minigame");

	public static final FlounderGameType<FakeMinigame> FAKE_MINIGAME_TYPE = FlounderApi.register(
			FAKE_MINIGAME_ID,
			FakeMinigame.CODEC,
			FakeMinigame::new
	);

    @Override
	public void onInitialize() {

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			FlounderGameManager flounderGameManager = FlounderApi.getFlounderGameManager(world);
			flounderGameManager.tick();

			for (ServerPlayerEntity player : world.getPlayers()) {
				if (player.getItemUseTime() == 20) {
					FakeMinigame myGame = FlounderApi.createGame(FAKE_MINIGAME_TYPE, world, BlockPos.ORIGIN);
				}
			}
		});

		ServerLifecycleEvents.BEFORE_SAVE.register((server, flush, force) -> {
			for (ServerWorld world : server.getWorlds()) {
				FlounderGameManager flounderGameManager = FlounderApi.getFlounderGameManager(world);
				flounderGameManager.markDirty();
			}
		});

	}
}
package net.superkat.flounderlib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.command.FlounderGameArgumentType;
import net.superkat.flounderlib.command.FlounderGameCommand;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLib implements ModInitializer {
	public static final String MOD_ID = "flounderlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//	@SuppressWarnings("UnstableApiUsage")
//	public static final AttachmentType<Boolean> TEST_MINIGAME = AttachmentRegistry.createPersistent(
//			Identifier.of(MOD_ID, "test_attachment"),
//			Codec.BOOL
//	);

    @Override
	public void onInitialize() {

		ArgumentTypeRegistry.registerArgumentType(
				Identifier.of(MOD_ID, "flounder_game_argument"),
				FlounderGameArgumentType.class,
				ConstantArgumentSerializer.of(FlounderGameArgumentType::new)
		);

		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
			FlounderGameCommand.register(commandDispatcher, commandRegistryAccess);
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			FlounderGameManager flounderGameManager = FlounderApi.getFlounderGameManager(world);
			flounderGameManager.tick();
		});

		ServerLifecycleEvents.BEFORE_SAVE.register((server, flush, force) -> {
			for (ServerWorld world : server.getWorlds()) {
				FlounderGameManager flounderGameManager = FlounderApi.getFlounderGameManager(world);
				flounderGameManager.markDirty();
			}
		});

	}
}
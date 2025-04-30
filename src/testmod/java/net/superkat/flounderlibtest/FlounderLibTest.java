package net.superkat.flounderlibtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.FlounderGameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLibTest implements ModInitializer {
    public static final String MOD_ID = "flounderlibtest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier TEST_MINIGAME_ID = Identifier.of(MOD_ID, "fake_minigame");

    public static final FlounderGameType<TestMinigame> TEST_MINIGAME_TYPE = FlounderApi.register(
            TEST_MINIGAME_ID,
            TestMinigame.CODEC,
            TestMinigame::new
    );

    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.getItemUseTime() == 20) {
                    TestMinigame myGame = FlounderApi.createGame(TEST_MINIGAME_TYPE, world, BlockPos.ORIGIN);
                    MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE);
                    LOGGER.info("Game started!");
                }
            }
        });
    }
}

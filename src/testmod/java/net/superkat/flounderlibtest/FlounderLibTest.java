package net.superkat.flounderlibtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLibTest implements ModInitializer {
    public static final String MOD_ID = "flounderlibtest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final FlounderGameType<TestMinigame> TEST = FlounderApi.createPersistent(
            TestMinigame.TEST_MINIGAME_ID,
            TestMinigame.CODEC
    );

    public static final FlounderGameType<CodecMinigame> TEST_CODEC = FlounderApi.createPersistent(
            CodecMinigame.ID,
            CodecMinigame.CODEC
    );

    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.getItemUseTime() == 20) {

                    TestMinigame myGame = new TestMinigame(player.getBlockPos());
                    FlounderApi.addGame(world, myGame);

                    CodecMinigame alsoMyGame = new CodecMinigame(player.getBlockPos());
                    FlounderApi.addGame(world, alsoMyGame);

                    MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE);
                    LOGGER.info("Game started!");
                }
            }
        });
    }
}

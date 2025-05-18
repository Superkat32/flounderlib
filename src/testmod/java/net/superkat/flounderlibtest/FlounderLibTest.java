package net.superkat.flounderlibtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlibtest.games.MoveQuicklyGame;
import net.superkat.flounderlibtest.testgames.CodecMinigame;
import net.superkat.flounderlibtest.testgames.TestMinigame;
import net.superkat.flounderlibtest.testgames.TestSyncedMinigame;
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

    public static final FlounderGameType<TestSyncedMinigame> TEST_SYNCED_MINIGAME = FlounderApi.createPersistent(
            TestSyncedMinigame.ID,
            TestSyncedMinigame.CODEC
    );

    public static final FlounderGameType<MoveQuicklyGame> MOVE_QUICKLY_GAME = FlounderApi.createPersistent(
            MoveQuicklyGame.ID,
            MoveQuicklyGame.CODEC
    );

    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.getItemUseTime() == 20) {
//                    if(true) {
//                        return;
//                    }

                    TestMinigame myGame = new TestMinigame(player.getBlockPos());
                    FlounderApi.addGame(world, myGame);

                    CodecMinigame alsoMyGame = new CodecMinigame(player.getBlockPos());
                    FlounderApi.addGame(world, alsoMyGame);

                    TestSyncedMinigame alsoAlsoMyGame = new TestSyncedMinigame(player);
                    FlounderApi.addGame(world, alsoAlsoMyGame);

                    if(player.isSneaking()) {
                        MoveQuicklyGame moveQuicklyGame = new MoveQuicklyGame(world, player);
                        FlounderApi.addGame(world, moveQuicklyGame);
                    }

                    player.playSoundToPlayer(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.MASTER, 1f, 1f);
                    LOGGER.info("Game started!");
                }
            }
        });
    }
}

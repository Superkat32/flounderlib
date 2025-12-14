package net.superkat.flounderlibtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.superkat.flounderlib.api.minigame.v1.FlounderApi;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.util.FlounderGameStartResult;
import net.superkat.flounderlibtest.test.TestMinigame;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLibTest implements ModInitializer {
    public static final String MOD_ID = "flounderlibtest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // TODO - Swift Seek (Sprint?) example (move quickly game)

    public static final FlounderGameType<TestMinigame> TEST_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameType.create(TestMinigame.ID, TestMinigame.CODEC)
                    .singleton(true)
    );

    public static final FlounderGameType<TestSyncedMinigame> TEST_SYNCED_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameType.create(TestSyncedMinigame.ID, TestSyncedMinigame.CODEC)
                    .synced(TestSyncedMinigame.STATE_SYNCER)
                    .singleton(true)
    );


    @Override
    public void onInitialize() {
        MinigameAutofills.init();

        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            for (ServerPlayer player : serverWorld.players()) {

                if(player.getTicksUsingItem() == 20) {
                    TestSyncedMinigame simpleSyncedMinigame = new TestSyncedMinigame(player.blockPosition());

                    FlounderGameStartResult startResult = FlounderApi.startMinigame(serverWorld, simpleSyncedMinigame);
                    if(startResult.isSuccessful()) {
                        player.sendSystemMessage(Component.literal("Minigame started!").withStyle(ChatFormatting.GREEN));
                    } else {
                        player.sendSystemMessage(Component.literal("Minigame couldn't start!").withStyle(ChatFormatting.RED));
                    }
                }

            }
        });
    }
}

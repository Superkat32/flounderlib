package net.superkat.flounderlibtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.superkat.flounderlib.api.minigame.v1.FlounderApi;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.util.FlounderGameStartResult;
import net.superkat.flounderlib.api.text.v1.builtin.BuiltinFlounderTextRenderers;
import net.superkat.flounderlib.api.text.v1.builtin.SplatText;
import net.superkat.flounderlibtest.test.ExampleMinigame;
import net.superkat.flounderlibtest.test.SimpleTestMinigame;
import net.superkat.flounderlibtest.test.TestMinigame;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLibTest implements ModInitializer {
    public static final String MOD_ID = "flounderlibtest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // TODO - Swift Seek (Sprint?) example (move quickly game)

    public static final FlounderGameType<ExampleMinigame> EXAMPLE_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameType.create(ExampleMinigame.ID, ExampleMinigame.CODEC)
                    .singleton(true)
    );

    public static final FlounderGameType<TestMinigame> TEST_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameType.create(TestMinigame.ID, TestMinigame.CODEC)
    );

    public static final FlounderGameType<SimpleTestMinigame> SIMPLE_TEST_TYPE = FlounderApi.register(
            FlounderGameType.<SimpleTestMinigame>create(SimpleTestMinigame.ID)
                    .overlap(false)
                    .distance(16)
                    .padding(8)
    );

    public static final FlounderGameType<TestSyncedMinigame> TEST_SYNCED_MINIGAME = FlounderApi.register(
            FlounderGameType.create(TestSyncedMinigame.ID, TestSyncedMinigame.CODEC)
                    .synced(true)
                    .singleton(true)
    );

    @Override
    public void onInitialize() {
        MinigameAutofills.init();

        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {

                if(player.getItemUseTime() == 20) {
                    TestSyncedMinigame testSyncedMinigame = new TestSyncedMinigame(player.getBlockPos());

                    FlounderGameStartResult startResult = FlounderApi.startMinigame(serverWorld, testSyncedMinigame);
                    if(startResult.isSuccessful()) {
                        player.sendMessage(Text.literal("Minigame started!").formatted(Formatting.GREEN));
                    } else {
                        player.sendMessage(Text.literal("Minigame couldn't start!").formatted(Formatting.RED));
                    }
                }
//                if(player.getItemUseTime() == 5) {
//                    BuiltinFlounderTextRenderers.REPO_TEXT_TYPE.send(player, new RepoText(Text.of("What's up homie buddy")));
//                }
//
//                if(player.getItemUseTime() == 20) {
//                    ExampleMinigame exampleMinigame = new ExampleMinigame(player.getBlockPos());
//
//                    FlounderGameStartResult startResult = FlounderApi.startMinigame(serverWorld, exampleMinigame);
//                    if(startResult.isSuccessful()) {
//                        player.sendMessage(Text.literal("Minigame started!").formatted(Formatting.GREEN));
//                    } else {
//                        player.sendMessage(Text.literal("Minigame couldn't start!").formatted(Formatting.RED));
//                    }
//                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (damageSource.getAttacker() instanceof ServerPlayerEntity player) {
                String name = livingEntity.getName().getString();
                Text text = Text.of("\uD83D\uDDE1 Splatted " + name);
                BuiltinFlounderTextRenderers.SPLAT_TEXT_TYPE.send(player, new SplatText(text, Colors.WHITE));
            }
        });
    }
}

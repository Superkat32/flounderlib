package net.superkat.flounderlibtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.action.FlounderGameStartResult;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.gametype.FlounderGameTypeBuilder;
import net.superkat.flounderlibtest.command.TestCommand;
import net.superkat.flounderlibtest.test.ExampleMinigame;
import net.superkat.flounderlibtest.test.MonkeyMinigame;
import net.superkat.flounderlibtest.test.TestMinigame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLibTest implements ModInitializer {
    public static final String MOD_ID = "flounderlibtest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // TODO - Swift Seek (Sprint?) example (move quickly game)

    public static final FlounderGameType<ExampleMinigame> EXAMPLE_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameTypeBuilder.createPersistent(ExampleMinigame.ID, ExampleMinigame.CODEC)
    );

    public static final FlounderGameType<TestMinigame> TEST_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameTypeBuilder.createPersistent(TestMinigame.ID, TestMinigame.CODEC)
    );

    public static final FlounderGameType<MonkeyMinigame> MONKEY_MINIGAME_TYPE = FlounderApi.register(
            FlounderGameTypeBuilder.createSimple(MonkeyMinigame.ID)
    );

    @Override
    public void onInitialize() {
        MinigameAutofills.init();

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            TestCommand.register(commandDispatcher, commandRegistryAccess);
        });

        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {

                if(player.getItemUseTime() == 20) {
                    ExampleMinigame exampleMinigame = new ExampleMinigame(player.getBlockPos());

                    FlounderGameStartResult startResult = FlounderApi.startMinigame(serverWorld, exampleMinigame);
                    if(startResult.isSuccessful()) {
                        player.sendMessage(Text.literal("Minigame started!").formatted(Formatting.GREEN));
                    } else {
                        player.sendMessage(Text.literal("Minigame couldn't start!").formatted(Formatting.RED));
                    }
                }
            }
        });
    }
}

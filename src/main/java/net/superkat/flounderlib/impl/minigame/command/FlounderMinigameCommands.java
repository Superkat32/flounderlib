package net.superkat.flounderlib.impl.minigame.command;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.FlounderLib;

public class FlounderMinigameCommands {

    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_game_argument"),
                FlounderMinigameArgumentType.class,
                SingletonArgumentInfo.contextAware(FlounderMinigameArgumentType::new)
        );

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            FlounderGameCommand.register(commandDispatcher, commandRegistryAccess);
        });
    }

}

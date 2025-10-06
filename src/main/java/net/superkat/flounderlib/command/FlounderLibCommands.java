package net.superkat.flounderlib.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;

public class FlounderLibCommands {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            FlounderGameCommand.register(commandDispatcher, commandRegistryAccess);
        });
    }

}

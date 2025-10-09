package net.superkat.flounderlib.command;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;
import net.superkat.flounderlib.command.minigame.argument.FlounderMinigameArgumentType;

public class FlounderLibCommands {

    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(FlounderLib.MOD_ID, "flounder_game_argument"),
                FlounderMinigameArgumentType.class,
                ConstantArgumentSerializer.of(FlounderMinigameArgumentType::new)
        );

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            FlounderGameCommand.register(commandDispatcher, commandRegistryAccess);
        });
    }

}

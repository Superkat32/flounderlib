package net.superkat.flounderlib.impl.text.command;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.FlounderLib;

public class FlounderTextCommands {

    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_text_argument"),
                FlounderTextArgument.class,
                SingletonArgumentInfo.contextAware(FlounderTextArgument::new)
        );

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            FlounderTextCommand.register(commandDispatcher, commandRegistryAccess);
        });
    }

}

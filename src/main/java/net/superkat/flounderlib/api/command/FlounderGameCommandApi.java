package net.superkat.flounderlib.api.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;

public class FlounderGameCommandApi {

    public static void registerAutofill(Identifier minigameId, ArgumentBuilder<ServerCommandSource, ?> argumentBuilder) {
        FlounderGameCommand.MINIGAME_AUTOFILLS.put(minigameId, argumentBuilder);
    }

}

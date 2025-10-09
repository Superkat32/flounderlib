package net.superkat.flounderlib.api.command;

import net.minecraft.util.Identifier;
import net.superkat.flounderlib.command.minigame.FlounderGameCommand;

public class FlounderCommandApi {

    public static void registerAutofill(Identifier minigameId, FlounderGameAutofill autofill) {
        FlounderGameCommand.MINIGAMES_ARGUMENT_BUILDERS.put(minigameId, autofill);
    }

}

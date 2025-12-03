package net.superkat.flounderlib;

import net.fabricmc.api.ClientModInitializer;
import net.superkat.flounderlib.impl.minigame.FlounderMinigameClientInit;
import net.superkat.flounderlib.impl.text.FlounderTextClientInit;

public class FlounderLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FlounderMinigameClientInit.init();
        FlounderTextClientInit.init();
    }
}

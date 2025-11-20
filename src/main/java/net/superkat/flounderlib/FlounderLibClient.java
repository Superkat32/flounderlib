package net.superkat.flounderlib;

import net.fabricmc.api.ClientModInitializer;
import net.superkat.flounderlib.network.FlounderClientNetworkHandler;
import net.superkat.flounderlib.text.FlounderTextRegistry;

public class FlounderLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FlounderClientNetworkHandler.init();
        FlounderTextRegistry.init();
    }
}

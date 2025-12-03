package net.superkat.flounderlib.impl.text;

import net.superkat.flounderlib.impl.text.network.client.FlounderTextClientNetworkHandler;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

public class FlounderTextClientInit {

    public static void init() {
        FlounderTextClientNetworkHandler.init();
        FlounderTextRegistry.init();
    }

}

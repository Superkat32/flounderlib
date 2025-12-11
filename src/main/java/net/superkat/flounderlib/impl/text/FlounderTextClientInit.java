package net.superkat.flounderlib.impl.text;

import net.superkat.flounderlib.impl.text.client.FlounderTextRendererHandler;
import net.superkat.flounderlib.impl.text.client.builtin.BuiltinFlounderTextRenderers;
import net.superkat.flounderlib.impl.text.network.client.FlounderTextClientNetworkHandler;

public class FlounderTextClientInit {

    public static void init() {
        FlounderTextRendererHandler.init();
        FlounderTextClientNetworkHandler.init();
        BuiltinFlounderTextRenderers.init();
    }

}

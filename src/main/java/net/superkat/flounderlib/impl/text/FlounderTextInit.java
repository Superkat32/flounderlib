package net.superkat.flounderlib.impl.text;

import net.superkat.flounderlib.api.text.v1.builtin.BuiltinFlounderTextRenderers;
import net.superkat.flounderlib.impl.text.command.FlounderTextCommands;
import net.superkat.flounderlib.impl.text.network.FlounderTextPackets;

public class FlounderTextInit {

    public static void init() {
        BuiltinFlounderTextRenderers.init();

        FlounderTextPackets.init();
        FlounderTextCommands.init();
    }

}

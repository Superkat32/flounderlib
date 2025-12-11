package net.superkat.flounderlib.impl.text.client.builtin;

import net.superkat.flounderlib.api.text.v1.FlounderTextClientApi;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;
import net.superkat.flounderlib.impl.text.client.builtin.renderer.ColoredObjectiveTextRenderer;
import net.superkat.flounderlib.impl.text.client.builtin.renderer.RepoTextRenderer;
import net.superkat.flounderlib.impl.text.client.builtin.renderer.ShakeyActionTextRenderer;
import net.superkat.flounderlib.impl.text.client.builtin.renderer.SplatTextRenderer;
import net.superkat.flounderlib.impl.text.client.builtin.renderer.TapeTextRenderer;
import net.superkat.flounderlib.impl.text.client.builtin.renderer.WipeoutTextRenderer;

public class BuiltinFlounderTextRenderers {

    public static void init() {
        FlounderTextClientApi.registerDefaultTextRenderer(BuiltinFlounderTexts.REPO_TEXT_TYPE, new RepoTextRenderer());

        FlounderTextClientApi.registerDefaultTextRenderer(BuiltinFlounderTexts.COLORED_OBJECTIVE_TEXT_TYPE, new ColoredObjectiveTextRenderer());
        FlounderTextClientApi.registerDefaultTextRenderer(BuiltinFlounderTexts.SPLAT_TEXT_TYPE, new SplatTextRenderer());
        FlounderTextClientApi.registerDefaultTextRenderer(BuiltinFlounderTexts.WIPEOUT_TEXT_TYPE, new WipeoutTextRenderer());
        FlounderTextClientApi.registerDefaultTextRenderer(BuiltinFlounderTexts.TAPE_TEXT_TYPE, new TapeTextRenderer());

        FlounderTextClientApi.registerDefaultTextRenderer(BuiltinFlounderTexts.SHAKEY_ACTION_TEXT_TYPE, new ShakeyActionTextRenderer());
    }

}

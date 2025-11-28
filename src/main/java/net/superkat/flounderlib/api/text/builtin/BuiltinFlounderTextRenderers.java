package net.superkat.flounderlib.api.text.builtin;

import net.minecraft.text.Text;
import net.superkat.flounderlib.api.text.FlounderTextApi;
import net.superkat.flounderlib.api.text.core.FlounderTextRenderer;
import net.superkat.flounderlib.api.text.core.FlounderTextType;

public class BuiltinFlounderTextRenderers {
    public static final FlounderTextType<RepoText> REPO_TEXT_TYPE = FlounderTextApi.register(
            RepoText.ID,
            RepoText.CODEC,
            FlounderTextRenderer.createWordQueued((initText, word) -> new RepoText(Text.of(word)))
    );

    public static final FlounderTextType<ColoredObjectiveText> COLORED_OBJECTIVE_TEXT_TYPE = FlounderTextApi.register(
            ColoredObjectiveText.ID,
            ColoredObjectiveText.CODEC,
            FlounderTextRenderer.createSingleton()
    );

    public static final FlounderTextType<SplatText> SPLAT_TEXT_TYPE = FlounderTextApi.register(
            SplatText.ID,
            SplatText.CODEC,
            FlounderTextRenderer.createSimple()
    );

    public static final FlounderTextType<WipeoutText> WIPEOUT_TEXT_TYPE = FlounderTextApi.register(
            WipeoutText.ID,
            WipeoutText.CODEC,
            FlounderTextRenderer.createSingleton()
    );

    public static final FlounderTextType<ShakeyActionText> SHAKEY_ACTION_TEXT_TYPE = FlounderTextApi.register(
            ShakeyActionText.ID,
            ShakeyActionText.CODEC,
            FlounderTextRenderer.createSingleton()
    );

    public static void init() {
        FlounderTextApi.registerDefaultRenderer(RepoText.ID);
        FlounderTextApi.registerDefaultRenderer(ColoredObjectiveText.ID);
        FlounderTextApi.registerDefaultRenderer(SplatText.ID);
        FlounderTextApi.registerDefaultRenderer(WipeoutText.ID);
        FlounderTextApi.registerDefaultRenderer(ShakeyActionText.ID);
    }

}

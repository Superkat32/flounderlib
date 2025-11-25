package net.superkat.flounderlib.api.text.builtin;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
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

    public static void init() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, RepoText.ID, FlounderTextApi.getRendererFromId(RepoText.ID));
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, ColoredObjectiveText.ID, FlounderTextApi.getRendererFromId(ColoredObjectiveText.ID));
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, SplatText.ID, FlounderTextApi.getRendererFromId(SplatText.ID));
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, WipeoutText.ID, FlounderTextApi.getRendererFromId(WipeoutText.ID));
    }

}

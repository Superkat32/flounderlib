package net.superkat.flounderlibtest.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.render.TargetMarkerRenderer;

public class MoveQuicklyTargetMarker extends TargetMarkerRenderer {

    public static final MoveQuicklyTargetMarker INSTANCE = new MoveQuicklyTargetMarker();

    public void renderWorld(WorldRenderContext context) {
        super.renderWorld(context, Identifier.ofVanilla("hud/heart/absorbing_full"), new Vec3d(0, -59.5, 0));
    }

    public void renderGui(DrawContext context, RenderTickCounter counter) {
//        super.renderGui(context, counter, Identifier.ofVanilla("hud/heart/absorbing_full"), new Vec3d(0.5, -60, 0.5));
    }
}

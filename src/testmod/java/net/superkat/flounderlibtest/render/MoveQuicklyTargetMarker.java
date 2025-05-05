package net.superkat.flounderlibtest.render;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.render.TargetMarker;
import net.superkat.flounderlib.render.TargetMarkerRenderer;

public class MoveQuicklyTargetMarker extends TargetMarkerRenderer {

    public static final MoveQuicklyTargetMarker INSTANCE = new MoveQuicklyTargetMarker();

    public TargetMarker targetMarker = new TargetMarker.Builder(
            Identifier.ofVanilla("hud/heart/absorbing_full"), () -> new Vec3d(0.5, -59, 0.5))
            .build();

    public TargetMarker targetMarker2 = new TargetMarker.Builder(
            Identifier.ofVanilla("hud/heart/frozen_full"), () -> new Vec3d(0.5, -59, 3.5))
            .setSmoothGuiRendering(false)
            .build();

    public TargetMarker targetMarker3 = new TargetMarker.Builder(
            Identifier.ofVanilla("hud/heart/full"), () -> new Vec3d(0.5, -59, 7.5))
            .setUseWorldRendering(true)
            .build();

    public TargetMarker targetMarker4 = new TargetMarker.Builder(
            Identifier.ofVanilla("hud/heart/hardcore_full"), () -> new Vec3d(0.5, -58, 12.5))
            .setUseWorldRendering(true)
            .setWorldScale(1f)
            .setGuiScale(32, 32)
            .setGuiPadding(24, 24)
            .setWorldRenderScaleWithDistance(false)
            .build();

    public TargetMarker targetMarker5 = new TargetMarker.Builder(
            Identifier.ofVanilla("hud/heart/poisoned_full"), () -> new Vec3d(0.5, -59, 17.5))
            .setUseWorldRendering(true)
            .setWorldScale(0.25f)
            .setGuiScale(8, 8)
            .setGuiPadding(8, 8)
            .build();

    public MoveQuicklyTargetMarker() {
        this.addTargetMarker(targetMarker);
//        this.addTargetMarker(targetMarker2);
//        this.addTargetMarker(targetMarker3);
//        this.addTargetMarker(targetMarker4);
//        this.addTargetMarker(targetMarker5);
    }
}

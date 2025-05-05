package net.superkat.flounderlibtest.render;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.render.TargetMarker;
import net.superkat.flounderlib.render.TargetMarkerRenderer;

public class MoveQuicklyTargetMarker extends TargetMarkerRenderer {

//    public static final MoveQuicklyTargetMarker INSTANCE = new MoveQuicklyTargetMarker();
    public static MoveQuicklyTargetMarker INSTANCE = new MoveQuicklyTargetMarker();

    public TargetMarker targetMarker = new TargetMarker.Builder(
            Identifier.ofVanilla("hud/heart/absorbing_full"), () -> new Vec3d(0.5, -60, 0.5))
            .build();

    public MoveQuicklyTargetMarker() {
        this.addTargetMarker(targetMarker);
    }
}

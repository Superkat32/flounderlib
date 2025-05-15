package net.superkat.flounderlibtest.render;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.render.marker.TargetMarker;
import net.superkat.flounderlib.render.marker.TargetMarkerRenderer;
import net.superkat.flounderlib.render.marker.TargetMarkerSettings;

public class MoveQuicklyTargetMarker extends TargetMarkerRenderer {

    public static final MoveQuicklyTargetMarker INSTANCE = new MoveQuicklyTargetMarker();

    public TargetMarker mainMarker;

    public MoveQuicklyTargetMarker() {
        this.mainMarker = new TargetMarker(Identifier.ofVanilla("hud/heart/absorbing_full"), () -> new Vec3d(0.5, -59, 0.5), new TargetMarkerSettings());
        addTargetMarker(mainMarker);
    }
}

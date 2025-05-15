package net.superkat.flounderlib.render.marker;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class TargetMarker {

    public Identifier icon;
    public Supplier<Vec3d> targetSupplier;
    public TargetMarkerSettings renderSettings;

    public float worldScale = 0.5f;
    public int guiScaleX = 16;
    public int guiScaleY = 16;

    public TargetMarker(Identifier icon, Supplier<Vec3d> targetSupplier, TargetMarkerSettings renderSettings) {
        this.icon = icon;
        this.targetSupplier = targetSupplier;
        this.renderSettings = renderSettings;
    }

    protected float applyWorldRenderDistanceScaling(float scale, float distance) {
        return scale * (distance * 0.05f + 0.25f);
    }

    public Identifier getIcon() {
        return icon;
    }

    public Vec3d getTarget() {
        return this.targetSupplier.get();
    }

    public TargetMarkerSettings getRenderSettings() {
        return renderSettings;
    }

    public boolean allowWorldRendering() {
        return this.getRenderSettings().getRenderMode().allowWorldRendering();
    }



    public void setWorldScale(float worldScale) {
        this.worldScale = worldScale;
    }

    public float getWorldScale() {
        return worldScale;
    }

    public void setGuiScale(int guiScaleX, int guiScaleY) {
        this.guiScaleX = guiScaleX;
        this.guiScaleY = guiScaleY;
    }

    public int getGuiScaleX() {
        return guiScaleX;
    }

    public int getGuiScaleY() {
        return guiScaleY;
    }

}

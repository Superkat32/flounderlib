package net.superkat.flounderlib.render;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class TargetMarker {
    public Identifier guiIcon, backgroundGuiIcon, arrowGuiIcon;
    public Supplier<Vec3d> targetSupplier;
    public float worldScale;
    public int guiScaleX, guiScaleY, guiPaddingX, guiPaddingY, guiHotbarPaddingY;
    public boolean smoothGuiRendering, useWorldRendering, worldRenderScaleWithDistance;

//    @ApiStatus.Internal
//    public int prevX = 0;
//    @ApiStatus.Internal
//    public int prevY = 0;

    public TargetMarker(
            Identifier guiIcon, Identifier backgroundGuiIcon, Identifier arrowGuiIcon,
            Supplier<Vec3d> targetSupplier,
            float worldScale,
            int guiScaleX, int guiScaleY, int guiPaddingX, int guiPaddingY, int guiHotbarPaddingY,
            boolean smoothGuiRendering, boolean useWorldRendering, boolean worldRenderScaleWithDistance
    ) {
        this.guiIcon = guiIcon;
        this.backgroundGuiIcon = backgroundGuiIcon;
        this.arrowGuiIcon = arrowGuiIcon;
        this.targetSupplier = targetSupplier;
        this.worldScale = worldScale;
        this.guiScaleX = guiScaleX;
        this.guiScaleY = guiScaleY;
        this.guiPaddingX = guiPaddingX;
        this.guiPaddingY = guiPaddingY;
        this.guiHotbarPaddingY = guiHotbarPaddingY;
        this.smoothGuiRendering = smoothGuiRendering;
        this.useWorldRendering = useWorldRendering;
        this.worldRenderScaleWithDistance = worldRenderScaleWithDistance;
    }

    public Identifier getGuiIcon() {
        return guiIcon;
    }

    public Identifier getBackgroundGuiIcon() {
        return backgroundGuiIcon;
    }

    public Identifier getArrowGuiIcon() {
        return arrowGuiIcon;
    }

    public Vec3d getTarget() {
        return targetSupplier.get();
    }

    public float getWorldScale() {
        return worldScale;
    }

    public int getGuiScaleX() {
        return guiScaleX;
    }

    public int getGuiScaleY() {
        return guiScaleY;
    }

    public int getGuiPaddingX() {
        return guiPaddingX;
    }

    public int getGuiPaddingY() {
        return guiPaddingY;
    }

    public int getGuiHotbarPaddingY() {
        return guiHotbarPaddingY;
    }

    public boolean isSmoothGuiRendering() {
        return smoothGuiRendering;
    }

    public boolean useWorldRendering() {
        return useWorldRendering;
    }

    public boolean isWorldRenderScaleWithDistance() {
        return worldRenderScaleWithDistance;
    }

    //    public int getPrevX() {
//        return prevX;
//    }
//
//    public void setPrevX(int prevX) {
//        this.prevX = prevX;
//    }
//
//    public int getPrevY() {
//        return prevY;
//    }
//
//    public void setPrevY(int prevY) {
//        this.prevY = prevY;
//    }

    public static class Builder {
        private Identifier guiIcon;
        private Supplier<Vec3d> target;

        private Identifier backgroundGuiIcon = null;
        private Identifier arrowGuiIcon = null;
        private float worldScale = 0.5f;
        private int guiScaleX = 16;
        private int guiScaleY = 16;
        private int guiPaddingX = 15;
        private int guiPaddingY = 15;
        private int guiHotbarPaddingY = 15;
        private boolean smoothGuiRendering = true;
        private boolean useWorldRendering = false;
        private boolean worldRenderScaleWithDistance = true;

        public Builder(Identifier guiIcon, Supplier<Vec3d> targetSupplier) {
            this.guiIcon = guiIcon;
            this.target = targetSupplier;
        }

        public Builder setGuiIcon(Identifier guiIcon) {
            this.guiIcon = guiIcon;
            return this;
        }

        public Builder setTarget(Supplier<Vec3d> target) {
            this.target = target;
            return this;
        }

        public Builder setBackgroundGuiIcon(Identifier backgroundGuiIcon) {
            this.backgroundGuiIcon = backgroundGuiIcon;
            return this;
        }

        public Builder setArrowGuiIcon(Identifier arrowGuiIcon) {
            this.arrowGuiIcon = arrowGuiIcon;
            return this;
        }

        public Builder setWorldScale(float worldScale) {
            this.worldScale = worldScale;
            return this;
        }

        public Builder setGuiScale(int guiScaleX, int guiScaleY) {
            this.guiScaleX = guiScaleX;
            this.guiScaleY = guiScaleY;
            return this;
        }

        public Builder setGuiPadding(int guiPaddingX, int guiPaddingY) {
            this.guiPaddingX = guiPaddingX;
            this.guiPaddingY = guiPaddingY;
            return this;
        }

        public Builder setGuiHotbarPaddingY(int guiHotbarPaddingY) {
            this.guiHotbarPaddingY = guiHotbarPaddingY;
            return this;
        }

        public Builder setSmoothGuiRendering(boolean smoothGuiRendering) {
            this.smoothGuiRendering = smoothGuiRendering;
            return this;
        }

        public Builder setUseWorldRendering(boolean useWorldRendering) {
            this.useWorldRendering = useWorldRendering;
            return this;
        }

        public Builder setWorldRenderScaleWithDistance(boolean worldRenderScaleWithDistance) {
            this.worldRenderScaleWithDistance = worldRenderScaleWithDistance;
            return this;
        }

        public TargetMarker build() {
            return new TargetMarker(
                    guiIcon, backgroundGuiIcon, arrowGuiIcon,
                    target,
                    worldScale,
                    guiScaleX, guiScaleY, guiPaddingX, guiPaddingY, guiHotbarPaddingY,
                    smoothGuiRendering, useWorldRendering, worldRenderScaleWithDistance
            );
        }

    }

}

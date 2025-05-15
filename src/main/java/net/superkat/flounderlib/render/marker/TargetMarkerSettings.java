package net.superkat.flounderlib.render.marker;

import net.minecraft.util.Identifier;

import java.util.function.Function;

public class TargetMarkerSettings {
    public Identifier guiBackground = Identifier.ofVanilla("advancements/challenge_frame_obtained");
//    public Identifier guiArrow = Identifier.ofVanilla("recipe_book/page_forward");
    public RenderMode renderMode = RenderMode.WORLD;

    public boolean scaleWithDistance = true;
//    public float guiArrowRotation = 0f;

    public float worldBackgroundPadding = 1.25f;
    public int guiBackgroundPaddingX = 4;
    public int guiBackgroundPaddingY = 4;

    public int guiPaddingX = 8;
    public int guiPaddingY = 8;
    public int guiHotbarPaddingY = 24;


//    public int guiArrowScaleX = 4;
//    public int guiArrowScaleY = 4;
//    public int guiArrowPaddingX = 4;
//    public int guiArrowPaddingY = 4;

    public Identifier getGuiBackground() {
        return guiBackground;
    }

    public void setGuiBackground(Identifier guiBackground) {
        this.guiBackground = guiBackground;
    }

    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }



    public boolean isScaleWithDistance() {
        return scaleWithDistance;
    }

    public void setScaleWithDistance(boolean scaleWithDistance) {
        this.scaleWithDistance = scaleWithDistance;
    }



    public float getWorldBackgroundPadding() {
        return worldBackgroundPadding;
    }

    public void setWorldBackgroundPadding(float worldBackgroundPadding) {
        this.worldBackgroundPadding = worldBackgroundPadding;
    }

    public int getGuiBackgroundPaddingX() {
        return guiBackgroundPaddingX;
    }

    public void setGuiBackgroundPaddingX(int guiBackgroundPaddingX) {
        this.guiBackgroundPaddingX = guiBackgroundPaddingX;
    }

    public int getGuiBackgroundPaddingY() {
        return guiBackgroundPaddingY;
    }

    public void setGuiBackgroundPaddingY(int guiBackgroundPaddingY) {
        this.guiBackgroundPaddingY = guiBackgroundPaddingY;
    }



    public int getGuiPaddingX() {
        return guiPaddingX;
    }

    public void setGuiPaddingX(int guiPaddingX) {
        this.guiPaddingX = guiPaddingX;
    }

    public int getGuiPaddingY() {
        return guiPaddingY;
    }

    public void setGuiPaddingY(int guiPaddingY) {
        this.guiPaddingY = guiPaddingY;
    }

    public int getGuiHotbarPaddingY() {
        return guiHotbarPaddingY;
    }

    public void setGuiHotbarPaddingY(int guiHotbarPaddingY) {
        this.guiHotbarPaddingY = guiHotbarPaddingY;
    }

    public enum RenderMode {
        /**
         * Uses a world renderer when the marker's position is in view.
         */
        WORLD(true),
        /**
         * Continues to use a GUI renderer even when the marker's position is in view, translating world space to screen space.<br>
         * This uses {@link net.minecraft.client.gui.DrawContext#drawGuiTexture(Function, Identifier, int, int, int, int)}, which means it uses integers for the position, causing a small amount of jitter when moving!
         */
        GUI(false),
        /**
         * Continues to use a GUI renderer even when the marker's position is in view, translating world space to screen space.<br>
         * This uses a custom GUI drawing method, allowing for floats to be used for the position instead of integers, allowing for smoother movement.
         */
        GUI_SMOOTH(false);

        final boolean allowWorldRendering;

        RenderMode(boolean allowWorldRendering) {
            this.allowWorldRendering = allowWorldRendering;
        }

        public boolean allowWorldRendering() {
            return this.allowWorldRendering;
        }
    }
}

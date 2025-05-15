package net.superkat.flounderlib.render.marker;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.api.render.FlounderRenderUtils;
import org.joml.Matrix4f;
import oshi.util.tuples.Pair;

import java.util.List;

public class TargetMarkerRenderer {

    public static final TargetMarkerRenderer INSTANCE = new TargetMarkerRenderer();

    public List<TargetMarker> markers = Lists.newArrayList();

    public void addTargetMarker(TargetMarker marker) {
        this.markers.add(marker);
    }

    public void removeTargetMarker(TargetMarker marker) {
        this.markers.remove(marker);
    }



    public void renderWorld(WorldRenderContext context) {
        for (TargetMarker marker : markers) {
            if(marker == null) continue;
            if(!marker.allowWorldRendering()) continue;
            renderWorldMarker(context, marker);
        }
    }

    public void renderWorldMarker(WorldRenderContext context, TargetMarker marker) {
        TargetMarkerSettings renderSettings = marker.getRenderSettings();

        Camera camera = context.camera();
        Vec3d cameraPos = camera.getPos();
        Vec3d target = marker.getTarget();
        Vec3d transPos = target.subtract(cameraPos);

        float scale = marker.getWorldScale();
        float distance = (float) cameraPos.distanceTo(target);

        if(renderSettings.isScaleWithDistance()) {
            scale = marker.applyWorldRenderDistanceScaling(scale, distance);
        }

        MatrixStack matrices = new MatrixStack();
        matrices.push();

        matrices.translate(transPos);
        matrices.multiply(camera.getRotation());
        matrices.scale(-scale, scale, scale);

        drawWorldBackground(marker, context, matrices);
        drawWorldIcon(marker, context, matrices);

        matrices.pop();
    }

    protected void drawWorldIcon(TargetMarker marker, WorldRenderContext context, MatrixStack matrices) {
        Identifier guiIcon = marker.getIcon();
        Sprite sprite = getSprite(guiIcon);
        VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.getGuiTexturedOverlay(sprite.getAtlasId()));

        int light = LightmapTextureManager.pack(15, 15);
        worldQuad(buffer, matrices, sprite, 1f, 1f, 1f, 1f, light);
    }

    protected void drawWorldBackground(TargetMarker marker, WorldRenderContext context, MatrixStack matrices) {
        TargetMarkerSettings renderSettings = marker.getRenderSettings();
        Identifier guiBackground = renderSettings.getGuiBackground();
        Sprite sprite = getSprite(guiBackground);
        VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.getGuiTexturedOverlay(sprite.getAtlasId()));

        matrices.push();

        float bgPadding = renderSettings.getWorldBackgroundPadding();
        matrices.scale(bgPadding, bgPadding, bgPadding);

        int light = LightmapTextureManager.pack(15, 15);
        worldQuad(buffer, matrices, sprite, 1f, 1f, 1f, 1f, light);

        matrices.pop();
    }

    protected void worldQuad(VertexConsumer buffer, MatrixStack matrices, Sprite sprite, float red, float green, float blue, float alpha, int light) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        buffer.vertex(matrix4f, -1, -1, 0)
                .color(red, green, blue, alpha).texture(sprite.getMaxU(), sprite.getMaxV()).light(light);

        buffer.vertex(matrix4f, -1, 1, 0)
                .color(red, green, blue, alpha).texture(sprite.getMaxU(), sprite.getMinV()).light(light);

        buffer.vertex(matrix4f, 1, 1, 0)
                .color(red, green, blue, alpha).texture(sprite.getMinU(), sprite.getMinV()).light(light);

        buffer.vertex(matrix4f, 1, -1, 0)
                .color(red, green, blue, alpha).texture(sprite.getMinU(), sprite.getMaxV()).light(light);
    }



    public void renderGui(DrawContext context, RenderTickCounter counter) {
        for (TargetMarker marker : markers) {
            if(marker == null) continue;
            renderGuiMarker(context, counter, marker);
        }
    }

    public void renderGuiMarker(DrawContext context, RenderTickCounter counter, TargetMarker marker) {
        TargetMarkerSettings renderSettings = marker.getRenderSettings();
        Vec3d target = marker.getTarget();
        int scaleX = marker.getGuiScaleX();
        int scaleY = marker.getGuiScaleY();
        int paddingX = renderSettings.getGuiPaddingX();
        int paddingY = renderSettings.getGuiPaddingY();
        int hotbarPadding = renderSettings.getGuiHotbarPaddingY();

        Vec3d projectedPos = FlounderRenderUtils.projectGameRenderer(target);
        if (marker.allowWorldRendering() && this.projectedPosOnScreen(projectedPos)) return;

        Pair<Float, Float> pair = getIconScreenCoordinates(context, scaleX, scaleY, projectedPos, paddingX, paddingY, hotbarPadding);
        float x = pair.getA();
        float y = pair.getB();

        drawGuiBackground(marker, context, counter, x - 2, y - 2, scaleX + 4, scaleY + 4);
        drawGuiIcon(marker, context, counter, x, y, scaleX, scaleY);
    }

    protected Pair<Float, Float> getIconScreenCoordinates(DrawContext context, int scaleX, int scaleY, Vec3d projectedPos, int paddingX, int paddingY, int hotbarPadding) {
        float centerX = (float) (context.getScaledWindowWidth() - scaleX) / 2;
        float centerY = (float) (context.getScaledWindowHeight() - scaleY) / 2;

        boolean behindCamera = projectedPos.getZ() > 1.0;
        boolean useHotbarPadding = behindCamera ? (projectedPos.getY() > 0) : (projectedPos.getY() < 0);

        float paddedX = paddingX / centerX;
        float paddedY = (paddingY + (useHotbarPadding ? hotbarPadding : 0)) / centerY;
        float absProjectedX = (float) Math.abs(projectedPos.getX());
        float absProjectedY = (float) Math.abs(projectedPos.getY());
        float clampedProjectedX = (float) MathHelper.clamp(projectedPos.getX(), -1f + paddedX, 1f - paddedX);
        float clampedProjectedY = (float) MathHelper.clamp(projectedPos.getY(), -1f + paddedY, 1f - paddedY);

        if(behindCamera) {
            if(absProjectedX > absProjectedY) {
                clampedProjectedX = (1f - paddedX) * (clampedProjectedX > 0 ? -1 : 1);
                clampedProjectedY = -clampedProjectedY;
            } else {
                clampedProjectedX = -clampedProjectedX;
                clampedProjectedY = (1f - paddedY) * (clampedProjectedY > 0 ? -1 : 1);
            }
        }


        float x = clampedProjectedX * centerX + centerX;
        float y = clampedProjectedY * -centerY + centerY;
//        float angle = (float) MathHelper.atan2(y - centerY, x - centerX);
        return new Pair<>(x, y);
    }

    protected void drawGuiIcon(TargetMarker marker, DrawContext context, RenderTickCounter counter, float x, float y, int width, int height) {
        TargetMarkerSettings renderSettings = marker.getRenderSettings();
        TargetMarkerSettings.RenderMode renderMode = renderSettings.getRenderMode();
        Identifier guiIcon = marker.getIcon();

        if(renderMode == TargetMarkerSettings.RenderMode.GUI_SMOOTH) {
            guiQuad(context, counter, guiIcon, x, y, width, height);
        } else {
            context.drawGuiTexture(RenderLayer::getGuiTextured, guiIcon, (int) x, (int) y, width, height);
        }
    }

    protected void drawGuiBackground(TargetMarker marker, DrawContext context, RenderTickCounter counter, float x, float y, int width, int height) {
        TargetMarkerSettings renderSettings = marker.getRenderSettings();
        TargetMarkerSettings.RenderMode renderMode = renderSettings.getRenderMode();
        Identifier guiIcon = renderSettings.getGuiBackground();

        int bgPaddingX = renderSettings.getGuiBackgroundPaddingX();
        int bgPaddingY = renderSettings.getGuiBackgroundPaddingY();
        x -= bgPaddingX / 2f;
        y -= bgPaddingY / 2f;
        width += bgPaddingX;
        height += bgPaddingY;

        if(renderMode == TargetMarkerSettings.RenderMode.GUI_SMOOTH) {
            guiQuad(context, counter, guiIcon, x, y, width, height);
        } else {
            context.drawGuiTexture(RenderLayer::getGuiTextured, guiIcon, (int) x, (int) y, width, height);
        }
    }

    protected void guiQuad(DrawContext context, RenderTickCounter counter, Identifier texture, float x, float y, int width, int height) {
        int color = -1;
        Sprite sprite = getSprite(texture);

        RenderLayer renderLayer = RenderLayer.getGuiTextured(sprite.getAtlasId());
        VertexConsumer vertexConsumer = context.vertexConsumers.getBuffer(renderLayer);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

        vertexConsumer.vertex(matrix4f, x, y, 0.0F).texture(sprite.getMinU(), sprite.getMinV()).color(color);
        vertexConsumer.vertex(matrix4f, x, y + height, 0.0F).texture(sprite.getMinU(), sprite.getMaxV()).color(color);
        vertexConsumer.vertex(matrix4f, x + width, y + height, 0.0F).texture(sprite.getMaxU(), sprite.getMaxV()).color(color);
        vertexConsumer.vertex(matrix4f, x + width, y, 0.0F).texture(sprite.getMaxU(), sprite.getMinV()).color(color);
    }

    protected boolean projectedPosOnScreen(Vec3d projectedPos) {
        return Math.abs(projectedPos.getX()) < 1 && Math.abs(projectedPos.getY()) < 1 && Math.abs(projectedPos.getZ()) < 1;
    }



    protected Sprite getSprite(Identifier icon) {
        return MinecraftClient.getInstance().getGuiAtlasManager().getSprite(icon);
    }
}

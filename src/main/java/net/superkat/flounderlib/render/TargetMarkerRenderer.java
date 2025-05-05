package net.superkat.flounderlib.render;

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
            if(!marker.useWorldRendering()) continue;
            renderWorldMarker(context, marker);
        }
    }

    public void renderWorldMarker(WorldRenderContext context, TargetMarker marker) {
        Camera camera = context.camera();
        Vec3d cameraPos = camera.getPos();
        Vec3d target = marker.getTarget();
        Vec3d transPos = target.subtract(cameraPos);

        Identifier guiIcon = marker.getGuiIcon();
        float scale = marker.getWorldScale();
        float distance = (float) cameraPos.distanceTo(target);

        if(marker.shouldWorldRenderScaleWithDistance()) {
            scale = applyWorldRenderDistanceScaling(scale, distance);
        }

        MatrixStack matrices = new MatrixStack();
        matrices.push();

        matrices.translate(transPos);
        matrices.multiply(camera.getRotation());
        matrices.scale(-scale, scale, scale);

        Sprite sprite = getSprite(guiIcon);
        VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.getGuiTexturedOverlay(sprite.getAtlasId()));
        drawWorldIcon(buffer, matrices, sprite);

        matrices.pop();
    }

    protected float applyWorldRenderDistanceScaling(float scale, float distance) {
        return scale * (distance * 0.05f + 0.25f);
    }

    protected void drawWorldIcon(VertexConsumer buffer, MatrixStack matrices, Sprite sprite) {
        int light = LightmapTextureManager.pack(15, 15);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        buffer.vertex(matrix4f, -1, -1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).light(light);

        buffer.vertex(matrix4f, -1, 1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).light(light);

        buffer.vertex(matrix4f, 1, 1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).light(light);

        buffer.vertex(matrix4f, 1, -1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).light(light);
    }

    protected void drawWorldBackground() {

    }



    public void renderGui(DrawContext context, RenderTickCounter counter) {
        for (TargetMarker marker : markers) {
            renderGuiMarker(context, counter, marker);
        }
    }

    public void renderGuiMarker(DrawContext context, RenderTickCounter counter, TargetMarker marker) {
        Vec3d target = marker.getTarget();
        Identifier guiIcon = marker.getGuiIcon();
        int scaleX = marker.getGuiScaleX();
        int scaleY = marker.getGuiScaleY();
        int paddingX = marker.getGuiPaddingX();
        int paddingY = marker.getGuiPaddingY();
        int hotbarPadding = marker.getGuiHotbarPaddingY();

        Vec3d projectedPos = FlounderRenderUtils.projectGameRenderer(target);
        if (marker.useWorldRendering() && projectedPosOnScreen(projectedPos)) return;

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
//        context.getMatrices().push();
//        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) Math.toDegrees(angle)), x, y, 0);

        drawGuiIcon(marker, context, counter, x, y, scaleX, scaleY);
//        context.getMatrices().pop();
    }

    protected void drawGuiIcon(TargetMarker marker, DrawContext context, RenderTickCounter counter, float x, float y, int width, int height) {
        Identifier guiIcon = marker.getGuiIcon();

        if(marker.smoothGuiRendering) {
            guiQuad(context, guiIcon, x, y, width, height);
        } else {
            context.drawGuiTexture(RenderLayer::getGuiTextured, guiIcon, (int) x, (int) y, width, height);
        }
    }

    protected void guiQuad(DrawContext context, Identifier texture, float x, float y, int width, int height) {
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

    protected void drawGuiBackground() {

    }

    protected void drawGuiArrow(TargetMarker marker, DrawContext context, float angle, float x, float y, int width, int height) {

    }

    protected boolean projectedPosOnScreen(Vec3d projectedPos) {
        return Math.abs(projectedPos.getX()) < 1 && Math.abs(projectedPos.getY()) < 1 && Math.abs(projectedPos.getZ()) < 1;
    }



    protected Sprite getSprite(Identifier icon) {
        return MinecraftClient.getInstance().getGuiAtlasManager().getSprite(icon);
    }
}

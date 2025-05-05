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
        float adjustedScale = scale * (distance * 0.05f + 0.25f);

        MatrixStack matrices = new MatrixStack();
        matrices.push();

        matrices.translate(transPos);
        matrices.multiply(camera.getRotation());
        matrices.scale(-adjustedScale, adjustedScale, adjustedScale);

        Sprite sprite = getSprite(guiIcon);
        VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.getGuiTexturedOverlay(sprite.getAtlasId()));
        int light = LightmapTextureManager.pack(15, 15);

        worldRenderQuad(buffer, matrices, sprite, light);

        matrices.pop();
    }

    protected void worldRenderQuad(VertexConsumer buffer, MatrixStack matrices, Sprite sprite, int light) {
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



    public void renderGui(DrawContext context, RenderTickCounter counter) {
        for (TargetMarker marker : markers) {
            renderGuiMarker(context, counter, marker);
        }
    }

    public void renderGuiMarker(DrawContext context, RenderTickCounter counter, TargetMarker marker) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();

        Vec3d target = marker.getTarget();
        Identifier guiIcon = marker.getGuiIcon();
        int scaleX = marker.getGuiScaleX();
        int scaleY = marker.getGuiScaleY();
        int paddingX = marker.getGuiPaddingX();
        int paddingY = marker.getGuiPaddingY();

        float centerX = (float) (context.getScaledWindowWidth() - scaleX) / 2;
        float centerY = (float) (context.getScaledWindowHeight() - scaleY) / 2;


//        Vec3d screenCoords = RenderUtils.worldSpaceToScreenSpace(target);
//        boolean onScreen = RenderUtils.screenSpaceCoordinateIsVisible(screenCoords);
//        int halfScaleX = scaleX / 2;
//        int halfScaleY = scaleY / 2;
//        int x = (int) MathHelper.clamp(screenCoords.getX() - halfScaleX, paddingX, context.getScaledWindowWidth() - paddingX * 2);
//        int y = (int) MathHelper.clamp(screenCoords.getY() - halfScaleY, paddingY, context.getScaledWindowHeight() - paddingY * 2);


//        float relativeYaw = getRelativeYaw(camera, target);
//        int extraX = (int) (relativeYaw * 2f);
//        extraX = MathHelper.clamp(extraX, -centerX + paddingX, centerX - paddingX);
//        int x = centerX + extraX;

        Vec3d projectedPos = FlounderRenderUtils.projectGameRenderer(target);
        if (marker.useWorldRendering() && projectedPosOnScreen(projectedPos)) return;

        boolean behindCamera = projectedPos.getZ() > 1.0;

        float absProjectedX = (float) Math.abs(projectedPos.getX());
        float absProjectedY = (float) Math.abs(projectedPos.getY());
        float clampedProjectedX = (float) MathHelper.clamp(projectedPos.getX(), -1f, 1f);
        float clampedProjectedY = (float) MathHelper.clamp(projectedPos.getY(), -1f, 1f);
        if(behindCamera) {
            if(absProjectedX > absProjectedY) {
                clampedProjectedX = 1f * (clampedProjectedX > 0 ? -1 : 1);
                clampedProjectedY = -clampedProjectedY;
            } else {
                clampedProjectedX = -clampedProjectedX;
                clampedProjectedY = 1f * (clampedProjectedY > 0 ? -1 : 1);
            }
        }

        float x = clampedProjectedX * centerX + centerX;
        float y = clampedProjectedY * -centerY + centerY;
//        float newX = clampedProjectedX * centerX + centerX;
//        float newY = clampedProjectedY * -centerY + centerY;
//        float prevX = marker.getPrevX();
//        float prevY = marker.getPrevY();

//        float diffX = newX - prevX;
//        float diffY = newY - prevY;

//        float x = newX;
//        float y = newY;
//        if(Math.abs(diffX) > 50 || Math.abs(diffY) > 50) {
//            float lerpAmount = (float) (client.world.getTime() % 80) / 80;
//            x = MathHelper.lerp(lerpAmount, newX, prevX);
//            y = MathHelper.lerp(lerpAmount, newY, prevY);
//            System.out.println(diffX + " - " + diffY);
//        }
//        marker.setPrevX((int) newX);
//        marker.setPrevY((int) newY);

        context.getMatrices().push();
        if(marker.smoothGuiRendering) {
            guiRenderQuad(context, guiIcon, x, y, scaleX, scaleY);
        } else {
            context.drawGuiTexture(RenderLayer::getGuiTextured, guiIcon, (int) x, (int) y, scaleX, scaleY);
        }
        context.getMatrices().pop();
    }

    protected void guiRenderQuad(DrawContext context, Identifier guiIcon, float x, float y, float width, float height) {
        int color = -1;
        Sprite sprite = getSprite(guiIcon);

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

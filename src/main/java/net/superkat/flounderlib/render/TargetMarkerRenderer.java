package net.superkat.flounderlib.render;

import me.x150.renderer.util.RenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class TargetMarkerRenderer {

    public static final TargetMarkerRenderer INSTANCE = new TargetMarkerRenderer();

    public void renderWorld(WorldRenderContext context, Identifier icon, Vec3d target) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = context.camera();
        Vec3d cameraPos = camera.getPos();
        Vec3d transPos = target.subtract(cameraPos);

        float scale = 1f;
        float distance = (float) cameraPos.distanceTo(target);
//        float adjustedScale = scale * (distance * 0.05f + 0.25f);
        float adjustedScale = scale + (1 / (distance * distance));

        MatrixStack matrices = new MatrixStack();
        matrices.push();

        matrices.translate(transPos);
        matrices.multiply(camera.getRotation());
        matrices.scale(-adjustedScale, adjustedScale, adjustedScale);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        Sprite sprite = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(icon);
        VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.getGuiTexturedOverlay(sprite.getAtlasId()));
        int light = LightmapTextureManager.pack(15, 15);

        buffer.vertex(matrix4f, -1, -1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).light(light);

        buffer.vertex(matrix4f, -1, 1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).light(light);

        buffer.vertex(matrix4f, 1, 1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).light(light);

        buffer.vertex(matrix4f, 1, -1, 0)
                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).light(light);

        matrices.pop();

        DrawContext drawContext = new DrawContext(client, (VertexConsumerProvider.Immediate) context.consumers());

        int centerX = drawContext.getScaledWindowWidth() / 2;
        int centerY = drawContext.getScaledWindowHeight() / 2;
        int scaleX = 16;
        int scaleY = 16;
        drawContext.drawGuiTexture(RenderLayer::getGuiTextured, icon, centerX, centerY, scaleX, scaleY);
        drawContext.draw();

//        MatrixStack guiMatrices = new MatrixStack();
//        guiMatrices.push();
//        int x = client.getWindow().getScaledWidth() / 2;
//        int y = client.getWindow().getScaledHeight() / 2;
//        int width = 16;
//        int height = 16;
//        guiMatrices.translate(0, 0, 200);
//
//        Matrix4f guiMatrix4f = guiMatrices.peek().getPositionMatrix();
//        buffer.vertex(guiMatrix4f, x, y, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).light(light);
//
//        buffer.vertex(guiMatrix4f, x, y + height, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).light(light);
//
//        buffer.vertex(guiMatrix4f, x + width, y + height, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).light(light);
//
//        buffer.vertex(guiMatrix4f, x + width, y, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).light(light);

//        guiMatrices.pop();
    }

    public void renderGui(DrawContext context, RenderTickCounter counter, Identifier guiIcon, Vec3d target) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        int scaleX = 16;
        int scaleY = 16;
        int paddingX = 15;
        int paddingY = 15;

        Vec3d screenCoords = RenderUtils.worldSpaceToScreenSpace(target);

//        MatrixStack matrixStack = new MatrixStack();
//        Quaternionf quaternionf = camera.getRotation().conjugate(new Quaternionf());
//        Matrix4f positionMatrix = new Matrix4f().rotation(quaternionf);
//        matrixStack.multiplyPositionMatrix(positionMatrix);
//        Matrix4f worldPositionMatrix = matrixStack.peek().getPositionMatrix();
//
//        Vec3d transPos = target.subtract(camera.getPos());
//        Vector4f transCoords = new Vector4f((float) transPos.getX(), (float) transPos.getY(), (float) transPos.getZ(), 1.f).mul(worldPositionMatrix);
//
//        Matrix4f matrixProj = new Matrix4f(RenderSystem.getProjectionMatrix());
//        Matrix4f matrixModel = new Matrix4f(RenderSystem.getModelViewMatrix());
//
//        int[] lastViewport = new int[4];
//        GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewport);
//
//        matrixProj.mul(matrixModel).project(transCoords.x(), transCoords.y(), transCoords.z(), lastViewport, target.toVector3f());
//        Vec3d screenCoords = new Vec3d(target.getX() / client.getWindow().getScaleFactor(), (client.getWindow().getHeight() - target.getY()) / client.getWindow().getScaleFactor(), target.z);

        int centerX = (int) MathHelper.clamp(screenCoords.getX(), paddingX, context.getScaledWindowWidth() - paddingX * 2);
        int centerY = (int) MathHelper.clamp(screenCoords.getY(), paddingY, context.getScaledWindowHeight() - paddingY * 2);
//        int centerX = (int) screenCoords.getX();
//        int centerY = (int) screenCoords.getY();
        context.getMatrices().push();
//        context.getMatrices().translate(0, 0, screenCoords.getZ());
//        int centerY = context.getScaledWindowHeight() / 2;

//        int centerX = (context.getScaledWindowWidth() - scaleX) / 2;
//        int centerY = (context.getScaledWindowHeight() - scaleY) / 2;
//
//        Vec3d cameraPos = camera.getPos();
//        Vec3d transPos = cameraPos.subtract(target);
//        float atan2 = (float) (MathHelper.atan2(transPos.getX(), -transPos.getZ()) * (180f / Math.PI));
//        float relativeYaw = MathHelper.subtractAngles(camera.getYaw(), atan2);
////        int extraX = (int) (relativeYaw * 173 / 2 / 60);
//        int extraX = (int) MathHelper.clamp(relativeYaw * Math.PI / 2, -centerX + paddingX, centerX - paddingX);

        context.drawGuiTexture(RenderLayer::getGuiTextured, guiIcon, centerX, centerY, scaleX, scaleY);
        context.getMatrices().pop();
    }

//    public void render(WorldRenderContext context, Identifier icon, Vec3d target) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        Camera camera = context.camera();
//        Vec3d cameraPos = camera.getPos();
//        Vec3d transPos = target.subtract(cameraPos);
//        Vec3d altTransPos = cameraPos.subtract(target);
//        Vec3d rotated = new Vec3d(-altTransPos.getZ(), altTransPos.getY(), altTransPos.getX());
//
//        float atan = (float) (MathHelper.atan2(rotated.getZ(), rotated.getX()) * (180f / Math.PI));
//        double relativeYaw = MathHelper.subtractAngles(camera.getYaw(), atan);
////        Vector4f transformedCoords = new Vector4f((float) transPos.getX(), (float) transPos.getY(), (float) transPos.getZ(), 1f).mul(context.matrixStack().peek().getPositionMatrix());
////
////        Matrix4f matrixProj = new Matrix4f(RenderSystem.getProjectionMatrix());
////        Matrix4f matrixModel = new Matrix4f(RenderSystem.getModelViewMatrix());
////        int[] lastViewport = new int[4];
////        GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewport);
////
////        matrixProj.mul(matrixModel).project(transformedCoords.x(), transformedCoords.y(), transformedCoords.z(), lastViewport, target.toVector3f());
////        Vec3d screenCoords = new Vec3d(target.x / client.getWindow().getScaleFactor(), (client.getWindow().getHeight() - target.y) / client.getWindow().getScaleFactor(), target.z);
//
//        MatrixStack matrices = new MatrixStack();
//        matrices.push();
//        matrices.multiply(new Quaternionf(camera.getRotation().z, -camera.getRotation().w, -camera.getRotation().x, camera.getRotation().y));
////        matrices.translate(transPos);
//        matrices.translate(relativeYaw, 0, 1);
//        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
//
//        Sprite sprite = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(icon);
//        VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.getGuiTexturedOverlay(sprite.getAtlasId()));
//        int light = LightmapTextureManager.pack(15, 15);
//
//        buffer.vertex(matrix4f, 0, 0, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).light(light);
//
//        buffer.vertex(matrix4f, 0, 1, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).light(light);
//
//        buffer.vertex(matrix4f, 1, 1, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).light(light);
//
//        buffer.vertex(matrix4f, 1, 0, 0)
//                .color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).light(light);
//    }

    public void drawBackgroundScreen() {

    }

    public void drawBackgroundWorld() {

    }

    public void drawIconScreen() {

    }

    public void drawIconWorld() {

    }

    public void drawArrowScreen() {

    }

    public void drawArrowWorld() {

    }

    public Identifier backgroundTexture() {
        return null;
    }

    public Identifier arrowTexture() {
        return null;
    }
}

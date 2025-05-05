package net.superkat.flounderlib.api.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FlounderRenderUtils {

    // ~~Stolen~~ Brought over from 1.21.5
    public static Vec3d projectGameRenderer(Vec3d sourcePos) {
        MinecraftClient client = MinecraftClient.getInstance();
        GameRenderer gameRenderer = client.gameRenderer;
        Camera camera = gameRenderer.getCamera();

        Matrix4f projectionMatrix = gameRenderer.getBasicProjectionMatrix(gameRenderer.getFov(camera, 0f, true));
        Quaternionf negativeCameraRotation = camera.getRotation().conjugate(new Quaternionf());
        Matrix4f negativeCameraRotMatrix = new Matrix4f().rotation(negativeCameraRotation);
        Matrix4f multipliedProjectCameraMatrix = projectionMatrix.mul(negativeCameraRotMatrix);
        Vec3d cameraPos = camera.getPos();
        Vec3d transPos = sourcePos.subtract(cameraPos);
        Vector3f projectedPos = multipliedProjectCameraMatrix.transformProject(transPos.toVector3f());
        return new Vec3d(projectedPos);
    }

}

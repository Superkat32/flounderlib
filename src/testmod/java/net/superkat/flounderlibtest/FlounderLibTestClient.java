package net.superkat.flounderlibtest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.Identifier;
import net.superkat.flounderlibtest.render.MoveQuicklyTargetMarker;

public class FlounderLibTestClient implements ClientModInitializer {

    public static final Identifier MOVE_QUICKLY_TARGET_MARKER_LAYER = Identifier.of(FlounderLibTest.MOD_ID, "move_quickly_target_marker_layer");

    @Override
    public void onInitializeClient() {
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerAfter(IdentifiedLayer.CHAT, MOVE_QUICKLY_TARGET_MARKER_LAYER, MoveQuicklyTargetMarker.INSTANCE::renderGui);
        });

        WorldRenderEvents.LAST.register(MoveQuicklyTargetMarker.INSTANCE::renderWorld);
    }
}

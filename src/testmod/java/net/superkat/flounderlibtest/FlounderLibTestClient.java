package net.superkat.flounderlibtest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.render.FlounderRenderApi;
import net.superkat.flounderlibtest.render.MoveQuicklyTargetMarker;
import net.superkat.flounderlibtest.render.hud.TestRenderedMinigameHud;
import net.superkat.flounderlibtest.testgames.TestRenderedMinigame;

public class FlounderLibTestClient implements ClientModInitializer {

    public static final Identifier MOVE_QUICKLY_TARGET_MARKER_LAYER = Identifier.of(FlounderLibTest.MOD_ID, "move_quickly_target_marker_layer");

    @Override
    public void onInitializeClient() {

        // Test minigame registrations
//        FlounderRenderApi.registerMinigameRenderer(
//                FlounderLibTest.TEST_RENDERED_MINIGAME,
//                TestRenderedMinigameRenderer::new,
//                TestRenderedMinigameRenderer::register
//        );

        FlounderRenderApi.registerMinigameHud(
                FlounderLibTest.TEST_RENDERED_MINIGAME,
                TestRenderedMinigame.GAME_LISTENER
        );

        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerBefore(IdentifiedLayer.SCOREBOARD, TestRenderedMinigameHud.ID, TestRenderedMinigameHud::render);
        });

        // Test rendering registrations
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerAfter(IdentifiedLayer.CHAT, MOVE_QUICKLY_TARGET_MARKER_LAYER, MoveQuicklyTargetMarker.INSTANCE::renderGui);
        });

        WorldRenderEvents.LAST.register(MoveQuicklyTargetMarker.INSTANCE::renderWorld);
    }
}

package net.superkat.flounderlibtest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;
import net.superkat.flounderlibtest.test.client.TestSyncedMinigameRenderer;

public class FlounderLibTestClient implements ClientModInitializer {

    public static final Identifier TEST_SYNCED_MINIGAME_LAYER = Identifier.of(FlounderLibTest.MOD_ID, "test_synced_minigame_layer");

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.BOSS_BAR, TEST_SYNCED_MINIGAME_LAYER, TestSyncedMinigameRenderer::renderSyncedMinigameHud);
    }
}

package net.superkat.flounderlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.duck.FlounderClientWorld;
import net.superkat.flounderlib.network.FlounderClientNetworkHandler;
import net.superkat.flounderlib.render.fun.RepoTextRenderer;

public class FlounderLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FlounderClientNetworkHandler.init();

        HudLayerRegistrationCallback.EVENT.register(layeredDrawerWrapper -> {
            layeredDrawerWrapper.attachLayerAfter(IdentifiedLayer.CHAT, Identifier.of(FlounderLib.MOD_ID, "repo_text_renderer"), RepoTextRenderer.INSTANCE);
        });

        ClientTickEvents.END_WORLD_TICK.register(clientWorld -> {
            FlounderClientWorld flWorld = (FlounderClientWorld) clientWorld;
            flWorld.flounderlib$getFlounderClientGameManager().tick();

            RepoTextRenderer.INSTANCE.tick();
        });
    }
}

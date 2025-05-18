package net.superkat.flounderlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;
import net.superkat.flounderlib.network.fun.packets.RepoTextS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderDataTrackerUpdateS2CPacket;
import net.superkat.flounderlib.render.fun.RepoTextRenderer;

import java.util.List;

public class FlounderLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FlounderDataTrackerUpdateS2CPacket.ID, (payload, context) -> {
            int minigameId = payload.minigameId();
            List<FlounderDataTracker.SerializedEntry<?>> values = payload.values();
            if(values.isEmpty()) return;

//            RepoText repoText = new RepoText(Text.of(String.valueOf(values.getFirst().value())));
//            RepoTextRenderer.INSTANCE.texts.add(repoText);
            RepoTextRenderer.INSTANCE.add(Text.of(String.valueOf(values.getFirst().value())));
//            System.out.println(values.getFirst().value());
            System.out.println("hi");
        });

        ClientPlayNetworking.registerGlobalReceiver(RepoTextS2CPacket.ID, (payload, context) -> {
            String text = payload.text();
            RepoTextRenderer.INSTANCE.add(Text.of(text));
        });

        HudLayerRegistrationCallback.EVENT.register(layeredDrawerWrapper -> {
            layeredDrawerWrapper.attachLayerAfter(IdentifiedLayer.CHAT, Identifier.of(FlounderLib.MOD_ID, "repo_text_renderer"), RepoTextRenderer.INSTANCE);
        });

        ClientTickEvents.END_WORLD_TICK.register(clientWorld -> {
            RepoTextRenderer.INSTANCE.tick();
        });
    }
}

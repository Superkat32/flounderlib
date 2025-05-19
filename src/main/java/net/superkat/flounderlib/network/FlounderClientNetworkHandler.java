package net.superkat.flounderlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;
import net.superkat.flounderlib.duck.FlounderClientWorld;
import net.superkat.flounderlib.minigame.FlounderClientGameManager;
import net.superkat.flounderlib.network.fun.packets.RepoTextS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderDataTrackerUpdateS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderGameCreationS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderGameDestroyS2CPacket;
import net.superkat.flounderlib.render.fun.RepoTextRenderer;

import java.util.List;

public class FlounderClientNetworkHandler {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameCreationS2CPacket.ID, FlounderClientNetworkHandler::onMinigameCreation);
        ClientPlayNetworking.registerGlobalReceiver(FlounderDataTrackerUpdateS2CPacket.ID, FlounderClientNetworkHandler::onMinigameTrackerUpdate);
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameDestroyS2CPacket.ID, FlounderClientNetworkHandler::onMinigameDestroy);

        ClientPlayNetworking.registerGlobalReceiver(RepoTextS2CPacket.ID, FlounderClientNetworkHandler::onRepoText);
    }

    public static void onMinigameCreation(FlounderGameCreationS2CPacket payload, ClientPlayNetworking.Context context) {
        int minigameId = payload.minigameId();
        IFlounderGame game = payload.game();

        if(game != null) {
            ClientWorld world = context.player().clientWorld;
            FlounderClientWorld flWorld = (FlounderClientWorld) world;
            FlounderClientGameManager manager = flWorld.flounderlib$getFlounderClientGameManager();
            manager.addGame(minigameId, game);
        }
    }

    public static void onMinigameTrackerUpdate(FlounderDataTrackerUpdateS2CPacket payload, ClientPlayNetworking.Context context) {
        int minigameId = payload.minigameId();
        List<FlounderDataTracker.SerializedEntry<?>> entries = payload.values();
        if(entries.isEmpty()) return;

        ClientWorld world = context.player().clientWorld;
        FlounderClientWorld flWorld = (FlounderClientWorld) world;
        FlounderClientGameManager manager = flWorld.flounderlib$getFlounderClientGameManager();
        IFlounderGame game = manager.getGames().get(minigameId);
        if(game instanceof SyncedFlounderGame syncedFlounderGame) {
            syncedFlounderGame.getFlounderDataTracker().writeUpdatedEntries(entries);

            for (FlounderDataTracker.SerializedEntry<?> serializedEntry : entries) {
                FlounderDataTracker.Entry<?> entry = syncedFlounderGame.getFlounderDataTracker().getEntries()[serializedEntry.entryId()];
                String string = serializedEntry.value() + "-" + entry.get();
                RepoTextRenderer.INSTANCE.add(Text.of(string));
            }
        }
    }

    public static void onMinigameDestroy(FlounderGameDestroyS2CPacket payload, ClientPlayNetworking.Context context) {
        int minigameId = payload.minigameId();

        ClientWorld world = context.player().clientWorld;
        FlounderClientWorld flWorld = (FlounderClientWorld) world;
        flWorld.flounderlib$getFlounderClientGameManager().removeGame(minigameId);
    }



    private static void onRepoText(RepoTextS2CPacket payload, ClientPlayNetworking.Context context) {
        String text = payload.text();
        RepoTextRenderer.INSTANCE.add(Text.of(text));
    }
}

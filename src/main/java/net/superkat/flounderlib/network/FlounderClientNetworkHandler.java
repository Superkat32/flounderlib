package net.superkat.flounderlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.superkat.flounderlib.network.fun.packets.RepoTextS2CPacket;
import net.superkat.flounderlib.render.fun.RepoTextRenderer;

public class FlounderClientNetworkHandler {

    public static void init() {
//        ClientPlayNetworking.registerGlobalReceiver(FlounderGameCreationS2CPacket.ID, FlounderClientNetworkHandler::onMinigameCreation);
//        ClientPlayNetworking.registerGlobalReceiver(FlounderDataTrackerUpdateS2CPacket.ID, FlounderClientNetworkHandler::onMinigameTrackerUpdate);
//        ClientPlayNetworking.registerGlobalReceiver(FlounderGameDestroyS2CPacket.ID, FlounderClientNetworkHandler::onMinigameDestroy);
//
//        ClientPlayNetworking.registerGlobalReceiver(FlounderGameUpdateS2CPacket.ID, FlounderClientNetworkHandler::onMinigameUpdate);

        ClientPlayNetworking.registerGlobalReceiver(RepoTextS2CPacket.ID, FlounderClientNetworkHandler::onRepoText);
    }

//    public static void onMinigameCreation(FlounderGameCreationS2CPacket payload, ClientPlayNetworking.Context context) {
//        int minigameId = payload.minigameId();
//        IFlounderGame game = payload.game();
//
//        if(game != null) {
//            ClientWorld world = context.player().clientWorld;
//            FlounderClientWorld flWorld = (FlounderClientWorld) world;
//            FlounderClientGameManager manager = flWorld.flounderlib$getFlounderClientGameManager();
//            manager.addGame(minigameId, game);
////            FlounderGameListenerRegistry.onMinigameAdd(game);
//        }
//    }
//
//    public static void onMinigameUpdate(FlounderGameUpdateS2CPacket payload, ClientPlayNetworking.Context context) {
//        int minigameId = payload.minigameId();
//        NbtCompound nbt = payload.nbt();
//
//        if(nbt == null) return;
//        ClientWorld world = context.player().clientWorld;
//        FlounderClientWorld flWorld = (FlounderClientWorld) world;
//        FlounderClientGameManager manager = flWorld.flounderlib$getFlounderClientGameManager();
//        IFlounderGame game = manager.getGames().get(minigameId);
//        if(game instanceof SyncedFlounderGame syncedGame) {
//            syncedGame.readSyncNbt(nbt);
//        }
//    }
//
//    public static void onMinigameTrackerUpdate(FlounderDataTrackerUpdateS2CPacket payload, ClientPlayNetworking.Context context) {
//        int minigameId = payload.minigameId();
//        List<FlounderDataTracker.SerializedEntry<?>> entries = payload.values();
//        if(entries.isEmpty()) return;
//
//        ClientWorld world = context.player().clientWorld;
//        FlounderClientWorld flWorld = (FlounderClientWorld) world;
//        FlounderClientGameManager manager = flWorld.flounderlib$getFlounderClientGameManager();
//        IFlounderGame game = manager.getGames().get(minigameId);
//        if(game instanceof DataTrackedSyncedFlounderGame syncedFlounderGame) {
//            syncedFlounderGame.getFlounderDataTracker().writeUpdatedEntries(entries);
//
//            for (FlounderDataTracker.SerializedEntry<?> serializedEntry : entries) {
//                FlounderDataTracker.Entry<?> entry = syncedFlounderGame.getFlounderDataTracker().getEntries()[serializedEntry.entryId()];
//                String string = serializedEntry.value() + "-" + entry.get();
//                RepoTextRenderer.INSTANCE.add(Text.of(string));
//            }
//        }
//    }
//
//    public static void onMinigameDestroy(FlounderGameDestroyS2CPacket payload, ClientPlayNetworking.Context context) {
//        int minigameId = payload.minigameId();
//
//        ClientWorld world = context.player().clientWorld;
//        FlounderClientWorld flWorld = (FlounderClientWorld) world;
//        flWorld.flounderlib$getFlounderClientGameManager().removeGame(minigameId);
//    }



    private static void onRepoText(RepoTextS2CPacket payload, ClientPlayNetworking.Context context) {
        String text = payload.text();
        RepoTextRenderer.INSTANCE.add(Text.of(text));
    }
}

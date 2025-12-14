package net.superkat.flounderlib.mixin.minigame.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.superkat.flounderlib.impl.minigame.client.FlounderClientGameManager;
import net.superkat.flounderlib.impl.minigame.duck.FlounderClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements FlounderClientLevel {

    public FlounderClientGameManager clientGameManager = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void flounderlib$createClientGameManager(ClientPacketListener networkHandler, ClientLevel.ClientLevelData properties, ResourceKey registryRef, Holder dimensionType, int loadDistance, int simulationDistance, LevelRenderer worldRenderer, boolean debugWorld, long seed, int seaLevel, CallbackInfo ci) {
        ClientLevel world = (ClientLevel) (Object) this;
        this.clientGameManager = new FlounderClientGameManager(world);
    }

    @Override
    public FlounderClientGameManager flounderlib$getClientGameManager() {
        return this.clientGameManager;
    }
}

package net.superkat.flounderlib.mixin.minigame.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.superkat.flounderlib.impl.minigame.duck.FlounderClientWorld;
import net.superkat.flounderlib.impl.minigame.sync.FlounderClientGameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements FlounderClientWorld {

    public FlounderClientGameManager clientGameManager = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void flounderlib$createClientGameManager(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties, RegistryKey registryRef, RegistryEntry dimensionType, int loadDistance, int simulationDistance, WorldRenderer worldRenderer, boolean debugWorld, long seed, int seaLevel, CallbackInfo ci) {
        ClientWorld world = (ClientWorld) (Object) this;
        this.clientGameManager = new FlounderClientGameManager(world);
    }

    @Override
    public FlounderClientGameManager flounderlib$getClientGameManager() {
        return this.clientGameManager;
    }
}

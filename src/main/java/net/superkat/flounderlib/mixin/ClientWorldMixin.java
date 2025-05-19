package net.superkat.flounderlib.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.superkat.flounderlib.duck.FlounderClientWorld;
import net.superkat.flounderlib.minigame.FlounderClientGameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements FlounderClientWorld {

    @Unique
    public FlounderClientGameManager flounderClientGameManager = null;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void flounderlib$createClientFlounderGameManager(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties, RegistryKey registryRef, RegistryEntry dimensionType, int loadDistance, int simulationDistance, WorldRenderer worldRenderer, boolean debugWorld, long seed, int seaLevel, CallbackInfo ci) {
        ClientWorld world = (ClientWorld) (Object) this;
        this.flounderClientGameManager = new FlounderClientGameManager(world);
    }

    @Override
    public FlounderClientGameManager flounderlib$getFlounderClientGameManager() {
        return flounderClientGameManager;
    }
}

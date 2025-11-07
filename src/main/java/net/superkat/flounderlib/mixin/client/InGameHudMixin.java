package net.superkat.flounderlib.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.superkat.flounderlib.duck.FlounderInGameHud;
import net.superkat.flounderlib.text.client.FlounderClientTextManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin implements FlounderInGameHud {
    @Unique
    public FlounderClientTextManager flounderTextManager = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void flounderlib$initFlounderTextManager(MinecraftClient client, CallbackInfo ci) {
        this.flounderTextManager = new FlounderClientTextManager(client);
    }

    @Inject(method = "clear", at = @At("TAIL"))
    private void flounderlib$clearFlounderTextManager(CallbackInfo ci) {
        this.flounderTextManager.clear();
    }

    @Override
    public FlounderClientTextManager flounderlib$getFlounderTextManager() {
        return this.flounderTextManager;
    }
}

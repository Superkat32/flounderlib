package net.superkat.flounderlib.mixin.hud.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.superkat.flounderlib.api.hud.v1.event.HudEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "tick(Z)V", at = @At("TAIL"))
    public void flounderlib$onEndHudTick(boolean paused, CallbackInfo ci) {
        InGameHud hud = (InGameHud) (Object) this;
        HudEvents.END_TICK.invoker().onHudEndTick(this.client, hud, paused);
    }

    @Inject(method = "clear", at = @At("TAIL"))
    public void flounderlib$onHudClear(CallbackInfo ci) {
        InGameHud hud = (InGameHud) (Object) this;
        HudEvents.HUD_CLEAR.invoker().onHudClear(this.client, hud);
    }
}

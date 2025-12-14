package net.superkat.flounderlib.mixin.hud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.superkat.flounderlib.api.hud.v1.event.client.GuiEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "tick(Z)V", at = @At("TAIL"))
    public void flounderlib$onEndHudTick(boolean paused, CallbackInfo ci) {
        Gui gui = (Gui) (Object) this;
        GuiEvents.END_TICK.invoker().onHudEndTick(this.minecraft, gui, paused);
    }

    @Inject(method = "onDisconnected", at = @At("TAIL"))
    public void flounderlib$onHudClear(CallbackInfo ci) {
        Gui gui = (Gui) (Object) this;
        GuiEvents.HUD_CLEAR.invoker().onHudClear(this.minecraft, gui);
    }
}

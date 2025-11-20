package net.superkat.flounderlib.event.hud;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;

public final class HudEvents {
    public static final Event<EndHudTick> END_TICK = EventFactory.createArrayBacked(EndHudTick.class, listeners -> (client, hud, paused) -> {
        for (EndHudTick listener : listeners) {
            listener.onHudEndTick(client, hud, paused);
        }
    });

    public static final Event<ClearHud> HUD_CLEAR = EventFactory.createArrayBacked(ClearHud.class, listeners -> (client, hud) -> {
        for (ClearHud listener : listeners) {
            listener.onHudClear(client, hud);
        }
    });

    @FunctionalInterface
    public interface EndHudTick {
        void onHudEndTick(MinecraftClient client, InGameHud hud, boolean paused);
    }

    @FunctionalInterface
    public interface ClearHud {
        void onHudClear(MinecraftClient client, InGameHud hud);
    }
}

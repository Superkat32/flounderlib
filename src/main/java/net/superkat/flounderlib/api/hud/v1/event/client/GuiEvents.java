package net.superkat.flounderlib.api.hud.v1.event.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public final class GuiEvents {
    public static final Event<EndGuiTick> END_TICK = EventFactory.createArrayBacked(EndGuiTick.class, listeners -> (client, hud, paused) -> {
        for (EndGuiTick listener : listeners) {
            listener.onHudEndTick(client, hud, paused);
        }
    });

    public static final Event<ClearGui> HUD_CLEAR = EventFactory.createArrayBacked(ClearGui.class, listeners -> (client, hud) -> {
        for (ClearGui listener : listeners) {
            listener.onHudClear(client, hud);
        }
    });

    @FunctionalInterface
    public interface EndGuiTick {
        void onHudEndTick(Minecraft client, Gui gui, boolean paused);
    }

    @FunctionalInterface
    public interface ClearGui {
        void onHudClear(Minecraft client, Gui gui);
    }
}

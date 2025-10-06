package net.superkat.flounderlib.api.gametype;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.FlounderableGame;

// goals:
// - singleton by default
// - not persistent by default
// FlounderGameType - singleton normally
// MultiFlounderGameType - allow multiple (MultitonFlounderGameType?)
// option to disallow other gametypes nearby
public record FlounderGameType<T extends FlounderableGame>(
        Identifier id,
        Codec<T> codec,
        int distance,
        int padding,
        boolean overlap,
        boolean singleton
) {
    public boolean persistent() {
        return this.codec != null;
    }
}

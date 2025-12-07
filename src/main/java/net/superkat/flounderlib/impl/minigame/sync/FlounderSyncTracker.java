package net.superkat.flounderlib.impl.minigame.sync;

import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;

import java.util.ArrayList;
import java.util.List;

public record FlounderSyncTracker<G extends FlounderableGame>(G game, List<FlSyncValue<G, ?, ?>> values) {
    public List<FlSyncValue.Packed<?>> updateAndGetPackedValues(boolean packAll) {
        List<FlSyncValue.Packed<?>> list = new ArrayList<>();

        for (FlSyncValue<G, ?, ?> value : values) {
            value.updateValue(this.game);

            if (!packAll && !value.isDirty()) continue;
            list.add(value.pack());
            value.setDirty(false);
        }
        return list;
    }
}

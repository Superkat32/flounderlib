package net.superkat.flounderlib.api.minigame;

import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;

public interface SyncedFlounderGame extends IFlounderGame {

    void initDataTracker(FlounderDataTracker.Builder builder);

    FlounderDataTracker getFlounderDataTracker();

    default FlounderDataTracker createDataTracker() {
        FlounderDataTracker.Builder builder = new FlounderDataTracker.Builder(this);
        this.initDataTracker(builder);
        return builder.build();
    }

    default void tickDataTracker(boolean onClient) {
        if(onClient) return;
        FlounderDataTracker dataTracker = this.getFlounderDataTracker();
        dataTracker.tick();
    }

    default void removeAllListeners() {
        FlounderDataTracker dataTracker = this.getFlounderDataTracker();
        dataTracker.removeAllListeners();
    }

}

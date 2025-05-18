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

    default void tickDataTracker() {
        FlounderDataTracker dataTracker = this.getFlounderDataTracker();
        dataTracker.tick();
    }

}

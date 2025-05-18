package net.superkat.flounderlib.api.sync;

import net.superkat.flounderlib.network.sync.FlTrackedDataHandler;

public record FlTrackedData<T>(int dataId, FlTrackedDataHandler<T> handler) {
}

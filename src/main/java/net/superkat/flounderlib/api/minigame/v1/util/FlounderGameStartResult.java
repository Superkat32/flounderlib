package net.superkat.flounderlib.api.minigame.v1.util;

public enum FlounderGameStartResult {
    SUCCESS(),
    FAILED_OVERLAP(),
    FAILED_SINGLETON(),
    FAILED();

    public boolean isSuccessful() {
        return this == SUCCESS;
    }

    public boolean isFailure() {
        return this != SUCCESS;
    }
}

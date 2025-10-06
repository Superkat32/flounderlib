package net.superkat.flounderlib.api.action;

public enum FlounderGameStartResult {
    SUCCESS(),
    FAILED();

    public boolean isSuccessful() {
        return this == SUCCESS;
    }

    public boolean isFailure() {
        return this != SUCCESS;
    }
}

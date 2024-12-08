package net.superkat.flounderlib;

import net.superkat.flounderlib.api.annotation.MinigameNbt;
import net.superkat.flounderlib.api.annotation.SyncedMinigameData;
import net.superkat.flounderlib.minigame.FlounderGame;

import java.util.UUID;

public class FakeMinigame extends FlounderGame {
    @MinigameNbt @SyncedMinigameData
    public int seconds = 1;

    @MinigameNbt(name = "currentPlayers")
    public int players = 2;

    @MinigameNbt
    public String name = "fakeMinigame";

    @MinigameNbt
    public UUID uuid = UUID.randomUUID();
}

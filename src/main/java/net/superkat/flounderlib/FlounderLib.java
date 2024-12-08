package net.superkat.flounderlib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.nbt.NbtCompound;
import net.superkat.flounderlib.nbt.MinigameNbtHandler;
import net.superkat.flounderlib.nbt.NbtSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLib implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("flounderlib");

	@Override
	public void onInitialize() {
        NbtSerializer.registerTest();
		FakeMinigame fakeMinigame = new FakeMinigame();
		fakeMinigame.players = 6;
		fakeMinigame.seconds = 10;
		fakeMinigame.ticks = 200;
        fakeMinigame.name = "eh?";

		NbtCompound nbtCompound = null;

        try {
			nbtCompound = MinigameNbtHandler.writeNbt(fakeMinigame, new NbtCompound());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        FakeMinigame newMinigame = new FakeMinigame();
        try {
            MinigameNbtHandler.readNbt(newMinigame, nbtCompound);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        };

        LOGGER.info("Hello Fabric world!");
	}
}
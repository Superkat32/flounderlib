package net.superkat.flounderlib;

import net.fabricmc.api.ModInitializer;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.superkat.flounderlib.api.annotation.MinigameNbt;
import net.superkat.flounderlib.api.annotation.nbt.FlounderNbtApi;
import net.superkat.flounderlib.nbt.MinigameNbtHandler;
import net.superkat.flounderlib.nbt.NbtSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FlounderLib implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
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
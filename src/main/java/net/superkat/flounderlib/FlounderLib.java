package net.superkat.flounderlib;

import net.fabricmc.api.ModInitializer;
import net.superkat.flounderlib.impl.minigame.FlounderMinigameInit;
import net.superkat.flounderlib.impl.text.FlounderTextInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlounderLib implements ModInitializer {
	public static final String MOD_ID = "flounderlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        FlounderMinigameInit.init();
        FlounderTextInit.init();
	}
}
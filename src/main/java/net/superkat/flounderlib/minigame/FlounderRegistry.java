package net.superkat.flounderlib.minigame;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;

public class FlounderRegistry {
    public static final Identifier FLOUNDER_GAME_TYPE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_game_type");
    public static final RegistryKey<Registry<FlounderGameType<?>>> FLOUNDER_GAME_TYPE_REGISTRY_KEY = RegistryKey.ofRegistry(FLOUNDER_GAME_TYPE_ID);
    public static final Registry<FlounderGameType<?>> GAME_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(FLOUNDER_GAME_TYPE_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType<T> type) {
        Registry.register(GAME_TYPE_REGISTRY, type.id(), type);
        return type;
    }

    public static Registry<FlounderGameType<?>> getRegistry() {
        return GAME_TYPE_REGISTRY;
    }
}

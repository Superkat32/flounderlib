package net.superkat.flounderlib.impl.minigame.game;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;

public class FlounderRegistry {
    public static final Identifier FLOUNDER_GAME_TYPE_ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_game_type");
    public static final ResourceKey<Registry<FlounderGameType<?>>> FLOUNDER_GAME_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(FLOUNDER_GAME_TYPE_ID);
    public static final Registry<FlounderGameType<?>> GAME_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(FLOUNDER_GAME_TYPE_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType<T> type) {
        Registry.register(GAME_TYPE_REGISTRY, type.id(), type);
        return type;
    }

    public static Registry<FlounderGameType<?>> getRegistry() {
        return GAME_TYPE_REGISTRY;
    }

    public static void init() {
        // NO-OP
    }
}

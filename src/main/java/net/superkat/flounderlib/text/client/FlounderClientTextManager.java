package net.superkat.flounderlib.text.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.FlounderText;
import net.superkat.flounderlib.api.text.FlounderTextApi;
import net.superkat.flounderlib.api.text.FlounderTextFactory;
import net.superkat.flounderlib.api.text.FlounderTextRenderer;
import net.superkat.flounderlib.api.text.builtin.RepoText;
import net.superkat.flounderlib.api.text.type.FlounderTextParams;
import net.superkat.flounderlib.api.text.type.FlounderTextType;

import java.util.Map;

public class FlounderClientTextManager {
    public static final Identifier FLOUNDER_TEXT_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_text");
    public static final RegistryKey<Registry<FlounderTextType<?>>> FLOUNDER_TEXT_REGISTRY_KEY = RegistryKey.ofRegistry(FLOUNDER_TEXT_ID);
    public static final Registry<FlounderTextType<?>> GAME_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(FLOUNDER_TEXT_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final FlounderTextType<FlounderTextParams.Default> REPO_TEXT = FlounderTextApi.register(RepoText.ID, FlounderTextParams.Default.CODEC, RepoText::new);

    public final MinecraftClient client;
    public final Map<Identifier, FlounderTextFactory<?>> textFactories = new Object2ObjectOpenHashMap<>();
    public final Map<Identifier, FlounderTextRenderer> textRenderers = new Object2ObjectOpenHashMap<>();

    public FlounderClientTextManager(MinecraftClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T extends FlounderTextParams> void add(Identifier id, T textParams) {
        FlounderTextType<T> type = (FlounderTextType<T>) getRegistry().get(id);
        if(type == null) return;

        FlounderTextFactory<T> factory = (FlounderTextFactory<T>) textFactories.putIfAbsent(id, type.factory());
        if(factory == null) return;

        FlounderText text = factory.createText(textParams);
        FlounderTextRenderer renderer = this.textRenderers.putIfAbsent(id, type.renderer());
        if(renderer == null) return;

        renderer.addText(text);
    }

    public void tick(boolean paused) {
        this.textRenderers.values().forEach(renderer -> renderer.tick(paused));
    }

    public void clear() {
        this.textRenderers.values().forEach(FlounderTextRenderer::clear);
//        this.textRenderers.clear();
    }

    public static <T extends FlounderTextParams> FlounderTextType<T> register(Identifier id, FlounderTextType<T> type) {
        Registry.register(GAME_TYPE_REGISTRY, id, type);
        return type;
    }

    public static Registry<FlounderTextType<?>> getRegistry() {
        return GAME_TYPE_REGISTRY;
    }

    public static void init() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, RepoText.ID, FlounderTextRenderer.DEFAULT_INSTANCE);

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            FlounderTextApi.getTextManager().tick(MinecraftClient.getInstance().isPaused());
        });
    }

}

package net.superkat.flounderlib.text;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextRenderer;
import net.superkat.flounderlib.api.text.core.FlounderTextType;
import net.superkat.flounderlib.event.hud.HudEvents;
import org.jetbrains.annotations.Nullable;

public class FlounderTextRegistry {
    public static final Identifier FLOUNDER_TEXT_TYPE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_text_type");
    public static final RegistryKey<Registry<FlounderTextType<?>>> FLOUNDER_TEXT_TYPE_REGISTRY_KEY = RegistryKey.ofRegistry(FLOUNDER_TEXT_TYPE_ID);
    public static final Registry<FlounderTextType<?>> TEXT_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(FLOUNDER_TEXT_TYPE_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final Codec<FlounderText> FLOUNDER_TEXT_CODEC = TEXT_TYPE_REGISTRY.getCodec().dispatch(FlounderText::getType, FlounderTextType::codec);

    public static <T extends FlounderText> FlounderTextType<T> register(FlounderTextType<T> flounderTextType) {
        return Registry.register(TEXT_TYPE_REGISTRY, flounderTextType.id(), flounderTextType);
    }

    public static void init() {
        HudEvents.END_TICK.register((client, hud, paused) -> {
            TEXT_TYPE_REGISTRY.stream().forEach(flounderTextType -> flounderTextType.renderer().tick(paused));
        });

        HudEvents.HUD_CLEAR.register((client, hud) -> {
            TEXT_TYPE_REGISTRY.stream().forEach(flounderTextType -> flounderTextType.renderer().clear());
        });
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends FlounderText> FlounderTextRenderer<T> getRenderer(Identifier id) {
        return (FlounderTextRenderer<T>) TEXT_TYPE_REGISTRY.get(id).renderer();
    }

    @SuppressWarnings("unchecked")
    public static <T extends FlounderText> MapCodec<T> getCodec(Identifier id) {
        return (MapCodec<T>) TEXT_TYPE_REGISTRY.get(id).codec();
    }

}

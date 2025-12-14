package net.superkat.flounderlib.impl.text.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;

public class FlounderTextRegistry {
    public static final Identifier FLOUNDER_TEXT_TYPE_ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "flounder_text_type");
    public static final ResourceKey<Registry<FlounderTextType<?>>> FLOUNDER_TEXT_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(FLOUNDER_TEXT_TYPE_ID);
    public static final Registry<FlounderTextType<?>> TEXT_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(FLOUNDER_TEXT_TYPE_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final Codec<FlounderText> FLOUNDER_TEXT_CODEC = TEXT_TYPE_REGISTRY.byNameCodec().dispatch(FlounderText::getFlounderTextType, FlounderTextType::codec);

    public static <T extends FlounderText> FlounderTextType<T> register(FlounderTextType<T> flounderTextType) {
        return Registry.register(TEXT_TYPE_REGISTRY, flounderTextType.id(), flounderTextType);
    }

    public static FlounderTextType<?> getType(Identifier id) {
        return TEXT_TYPE_REGISTRY.getValue(id);
    }

}

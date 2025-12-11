package net.superkat.flounderlib.impl.text.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;

public class FlounderTextRegistry {
    public static final Identifier FLOUNDER_TEXT_TYPE_ID = Identifier.of(FlounderLib.MOD_ID, "flounder_text_type");
    public static final RegistryKey<Registry<FlounderTextType<?>>> FLOUNDER_TEXT_TYPE_REGISTRY_KEY = RegistryKey.ofRegistry(FLOUNDER_TEXT_TYPE_ID);
    public static final Registry<FlounderTextType<?>> TEXT_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(FLOUNDER_TEXT_TYPE_REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final Codec<FlounderText> FLOUNDER_TEXT_CODEC = TEXT_TYPE_REGISTRY.getCodec().dispatch(FlounderText::getFlounderTextType, FlounderTextType::codec);

    public static <T extends FlounderText> FlounderTextType<T> register(FlounderTextType<T> flounderTextType) {
        return Registry.register(TEXT_TYPE_REGISTRY, flounderTextType.id(), flounderTextType);
    }

    public static FlounderTextType<?> getType(Identifier id) {
        return TEXT_TYPE_REGISTRY.get(id);
    }

}

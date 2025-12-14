package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;

public class ShakeyActionText extends FlounderText {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "shakey_action_text");
    public static final MapCodec<ShakeyActionText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec()
            ).apply(instance, ShakeyActionText::new)
    );

    public int bounceInTicks;
    public int fadeOutTicks;

    public int color;
    public int shadowColor;
    public float maxBounceScale;
    public float maxStretch;

    public ShakeyActionText(Component text) {
        super(text);

        this.maxTicks = 80;

        this.bounceInTicks = 16;
        this.fadeOutTicks = 4;

        this.color = ARGB.colorFromFloat(1f, 0.9f, 0.85f, 0.18f);
        this.shadowColor = ARGB.colorFromFloat(1f, 0.75f, 0.22f, 0f);
        this.maxBounceScale = 2f;
        this.maxStretch = 3f;
    }

    @Override
    public FlounderTextType<?> getFlounderTextType() {
        return BuiltinFlounderTexts.SHAKEY_ACTION_TEXT_TYPE;
    }
}

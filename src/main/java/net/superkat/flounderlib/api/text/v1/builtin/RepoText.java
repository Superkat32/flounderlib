package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;

public class RepoText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "repo_text");
    public static final MapCodec<RepoText> CODEC = createDefaultCodec(RepoText::new);

    public int yellowTicks;
    public int bounceInTicks;
    public float bounceAmountY;

    public RepoText(Text text) {
        super(text);
        int textLength = text.getLiteralString().length();
        this.maxTicks = Math.min(8 + (textLength), 30);

        this.yellowTicks = 4;
        this.bounceInTicks = 8;
        this.bounceAmountY = MathHelper.nextBetween(this.random, 3f, 7f);
    }

    @Override
    public FlounderTextType<?> getFlounderTextType() {
        return BuiltinFlounderTexts.REPO_TEXT_TYPE;
    }
}

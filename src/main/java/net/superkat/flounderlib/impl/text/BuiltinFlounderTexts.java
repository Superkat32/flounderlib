package net.superkat.flounderlib.impl.text;

import net.superkat.flounderlib.api.text.v1.FlounderTextApi;
import net.superkat.flounderlib.api.text.v1.builtin.ColoredObjectiveText;
import net.superkat.flounderlib.api.text.v1.builtin.RepoText;
import net.superkat.flounderlib.api.text.v1.builtin.ShakeyActionText;
import net.superkat.flounderlib.api.text.v1.builtin.SplatText;
import net.superkat.flounderlib.api.text.v1.builtin.TapeText;
import net.superkat.flounderlib.api.text.v1.builtin.WipeoutText;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;

public class BuiltinFlounderTexts {

    public static final FlounderTextType<RepoText> REPO_TEXT_TYPE = FlounderTextApi.register(RepoText.ID, RepoText.CODEC);

    public static final FlounderTextType<ColoredObjectiveText> COLORED_OBJECTIVE_TEXT_TYPE = FlounderTextApi.register(ColoredObjectiveText.ID, ColoredObjectiveText.CODEC);
    public static final FlounderTextType<SplatText> SPLAT_TEXT_TYPE = FlounderTextApi.register(SplatText.ID, SplatText.CODEC);
    public static final FlounderTextType<WipeoutText> WIPEOUT_TEXT_TYPE = FlounderTextApi.register(WipeoutText.ID, WipeoutText.CODEC);
    public static final FlounderTextType<TapeText> TAPE_TEXT_TYPE = FlounderTextApi.register(TapeText.ID, TapeText.CODEC);

    public static final FlounderTextType<ShakeyActionText> SHAKEY_ACTION_TEXT_TYPE = FlounderTextApi.register(ShakeyActionText.ID, ShakeyActionText.CODEC);

    public static void init() {
        // NO-OP
    }

}

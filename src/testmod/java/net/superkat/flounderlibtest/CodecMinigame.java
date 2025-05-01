package net.superkat.flounderlibtest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.minigame.FlounderGame;
import org.jetbrains.annotations.NotNull;

public class CodecMinigame extends FlounderGame {

    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "codec_test");

    public static final Codec<CodecMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.pos),
                    Codec.STRING.fieldOf("test").forGetter(game -> game.test),
                    Codec.BOOL.fieldOf("booleanyay").forGetter(game -> game.myBool)
            ).apply(instance, CodecMinigame::new)
    );

    public BlockPos pos;
    public String test = "";
    public boolean myBool = false;

    public CodecMinigame(BlockPos pos) {
        this.pos = pos;
    }

    public CodecMinigame(int ticks, BlockPos pos, String test, boolean myBool) {
        this(pos);
        this.ticks = ticks;
        this.test = test;
        this.myBool = myBool;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.ticks == 200) {
            this.test = "what's up homie buddie";
        }

        if(this.ticks == 100) {
            this.myBool = false;
        }
    }

    @Override
    public boolean shouldRemove() {
        return this.ticks >= 300;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return ID;
    }
}

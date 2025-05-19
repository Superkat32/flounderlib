package net.superkat.flounderlibtest.testgames;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

public class TestMinigame extends FlounderGame {
    public static final Identifier TEST_MINIGAME_ID = Identifier.of(FlounderLibTest.MOD_ID, "fake_minigame");
    public static final Codec<TestMinigame> CODEC = NbtCompound.CODEC.xmap(
            TestMinigame::new,
            game -> game.toNbt(new NbtCompound())
    );

    public final BlockPos pos;

    // The main constructor when we want to start this minigame
    public TestMinigame(BlockPos startPos) {
        this.pos = startPos;
    }

    // The persistent constructor when the minigame is reloaded upon world rejoin
    public TestMinigame(NbtCompound compound) {
        this.ticks = compound.getInt("ticks", 0);
        this.pos = compound.get("pos", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.ticks >= 300) {
            FlounderLibTest.LOGGER.info("Game ended!");
            this.invalidate();
        }
    }

    public NbtCompound toNbt(NbtCompound compound) {
        compound.putInt("ticks", this.ticks);
        compound.put("pos", BlockPos.CODEC, this.pos);

        compound.putString("test", "What's up homie buddy");
        compound.putString("test1", "What's up homie buddy");
        compound.putString("test2", "What's up homie buddy");
        compound.putString("test3", "What's up homie buddy");
        compound.putString("test4", "What's up homie buddy");
        compound.putString("test5", "What's up homie buddy");
        compound.putString("test6", "What's up homie buddy");
        compound.putString("test7", "What's up homie buddy");
        compound.putString("test8", "What's up homie buddy");
        compound.putString("test9", "What's up homie buddy");
        compound.putString("test10", "What's up homie buddy");
        compound.putString("test11", "What's up homie buddy");
        compound.putString("test12", "What's up homie buddy");
        compound.putString("test13", "What's up homie buddy");
        compound.putString("test14", "What's up homie buddy");
        compound.putString("test15", "What's up homie buddy");
        compound.putString("test16", "What's up homie buddy");
        compound.putString("test17", "What's up homie buddy");
        return compound;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return TEST_MINIGAME_ID;
    }
}

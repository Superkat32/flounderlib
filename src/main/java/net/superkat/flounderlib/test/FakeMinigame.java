package net.superkat.flounderlib.test;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.gametype.PersistentGame;
import net.superkat.flounderlib.minigame.FlounderGame;

public class FakeMinigame extends FlounderGame implements PersistentGame {

    public static final Codec<FakeMinigame> CODEC = NbtCompound.CODEC.xmap(
            FakeMinigame::new,
            game -> game.toNbt(new NbtCompound())
    );

    public FakeMinigame(World world, BlockPos pos) {

    }

    public FakeMinigame(NbtCompound compound) {
        this.ticks = compound.getInt("ticks", 0);
    }

    @Override
    public boolean shouldRemove() {
        return this.ticks >= 300;
    }

    public NbtCompound toNbt(NbtCompound compound) {
        compound.putInt("ticks", this.ticks);

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
    public Codec<FakeMinigame> getCodec() {
        return CODEC;
    }

    @Override
    public Identifier getIdentifier() {
        return FlounderLib.FAKE_MINIGAME_ID;
    }
}

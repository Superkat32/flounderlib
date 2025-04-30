package net.superkat.flounderlib.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface FlounderGameFactory<T extends IFlounderGame> {
    T create(World world, BlockPos pos);
}

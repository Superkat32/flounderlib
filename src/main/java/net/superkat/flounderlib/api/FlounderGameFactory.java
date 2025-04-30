package net.superkat.flounderlib.api;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface FlounderGameFactory<T extends IFlounderGame> {
    T create(ServerWorld world, BlockPos pos);
}

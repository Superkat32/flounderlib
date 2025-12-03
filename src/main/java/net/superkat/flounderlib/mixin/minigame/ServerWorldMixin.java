package net.superkat.flounderlib.mixin.minigame;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.superkat.flounderlib.impl.minigame.duck.FlounderWorld;
import net.superkat.flounderlib.impl.minigame.game.FlounderGameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements FlounderWorld {

    @Shadow public abstract PersistentStateManager getPersistentStateManager();

    @Unique
    public FlounderGameManager flounderGameManager = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void flounderlib$createFlounderGameManager(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List spawners, boolean shouldTickTime, RandomSequencesState randomSequencesState, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        this.flounderGameManager = this.getPersistentStateManager().getOrCreate(FlounderGameManager.getPersistentStateType(world));
    }

    @Override
    public FlounderGameManager flounderlib$getFlounderGameManager() {
        return this.flounderGameManager;
    }

}

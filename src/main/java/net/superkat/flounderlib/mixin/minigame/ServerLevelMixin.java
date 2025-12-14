package net.superkat.flounderlib.mixin.minigame;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.superkat.flounderlib.impl.minigame.duck.FlounderLevel;
import net.superkat.flounderlib.impl.minigame.game.FlounderGameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements FlounderLevel {

    @Shadow public abstract DimensionDataStorage getDataStorage();

    @Unique
    public FlounderGameManager flounderGameManager = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void flounderlib$createFlounderGameManager(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, LevelStem levelStem, boolean bl, long l, List<CustomSpawner> list, boolean bl2, RandomSequences randomSequences, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        this.flounderGameManager = this.getDataStorage().computeIfAbsent(FlounderGameManager.getPersistentStateType(level));
    }

    @Override
    public FlounderGameManager flounderlib$getFlounderGameManager() {
        return this.flounderGameManager;
    }

}

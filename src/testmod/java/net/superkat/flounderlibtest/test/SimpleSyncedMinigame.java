package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.minigame.v1.game.SyncedFlounderGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

public class SimpleSyncedMinigame extends SyncedFlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "simple_synced_flounder_game");

    public static final Codec<SimpleSyncedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos),
                    Codec.BOOL.fieldOf("aBoolean").forGetter(game -> game.aBoolean)
            ).apply(instance, SimpleSyncedMinigame::new)
    );

    public static final FlounderStateSyncer<SimpleSyncedMinigame, SyncState> STATE_SYNCER = FlounderStateSyncer.create(SimpleSyncedMinigame.class, SyncState::new)
            .addInteger(SimpleSyncedMinigame::getMinigameId, (syncState, value) -> syncState.id = value)
            .addInteger(SimpleSyncedMinigame::getTicks, (syncState, value) -> syncState.ticks = value)
            .addBlockPos(SimpleSyncedMinigame::getCenterPos, (syncState, value) -> syncState.pos = value)
            .addBoolean(game -> game.aBoolean, (syncState, value) -> syncState.aBoolean = value);

    public boolean aBoolean = false;

    public SimpleSyncedMinigame(BlockPos centerPos) {
        super(centerPos);
    }

    public SimpleSyncedMinigame(int ticks, BlockPos centerPos, boolean aBoolean) {
        super(ticks, centerPos);
        this.aBoolean = aBoolean;
    }

    @Override
    public void tick() {
        super.tick();
        this.markDirty();

        if(this.ticks % 100 == 0) {
            this.aBoolean = !this.aBoolean;
        }

        if(this.ticks >= 500) {
            this.invalidate();
        }
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        super.addPlayer(player);

        // Send the player a joining message
        player.sendMessage(Text.literal("Joined minigame! - " + this.getCenterPos().toShortString()).formatted(Formatting.GREEN), true);
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        super.removePlayer(player);

        // Send the player a leaving message
        player.sendMessage(Text.literal("Left minigame!").formatted(Formatting.RED), true);
    }

    @Override
    public void invalidate() {
        // Send all in the minigame players a message that the game has ended
        for (ServerPlayerEntity player : this.getPlayers()) {
            player.sendMessage(Text.literal("Minigame ended!"), true);
        }

        super.invalidate();
    }

    @Override
    public @NotNull FlounderGameType<?> getGameType() {
        return FlounderLibTest.SIMPLE_SYNCED_MINIGAME;
    }

    public static class SyncState implements FlounderSyncState {
        public int id = 0;
        public int ticks = 0;
        public BlockPos pos = BlockPos.ORIGIN;
        public boolean aBoolean = false;
    }
}

package net.superkat.flounderlibtest.testgames;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestRenderedMinigame extends FlounderGame implements SyncedFlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_rendered_minigame");

    public static final Codec<TestRenderedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    Uuids.CODEC.fieldOf("playerId").forGetter(game -> game.playerUuid)
            ).apply(instance, TestRenderedMinigame::new)
    );

    public static final PacketCodec<RegistryByteBuf, TestRenderedMinigame> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, game -> game.ticks,
            Uuids.PACKET_CODEC, game -> game.playerUuid,
            TestRenderedMinigame::new
    );

    public final FlounderDataTracker dataTracker = this.createDataTracker();
    public final UUID playerUuid;

    public TestRenderedMinigame(PlayerEntity player) {
        this.playerUuid = player.getUuid();
    }

    public TestRenderedMinigame(int ticks, UUID uuid) {
        this.ticks = ticks;
        this.playerUuid = uuid;
    }

    @Override
    public void initialize(World world, int minigameId) {
        super.initialize(world, minigameId);

        if(this.world.isClient) return;
        this.dataTracker.addPlayerListener((ServerPlayerEntity) this.world.getPlayerByUuid(this.playerUuid));
    }

    @Override
    public void initDataTracker(FlounderDataTracker.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();

        if(this.ticks >= 300) {
            this.invalidate();
        }
    }

    @Override
    public FlounderDataTracker getFlounderDataTracker() {
        return this.dataTracker;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return ID;
    }
}

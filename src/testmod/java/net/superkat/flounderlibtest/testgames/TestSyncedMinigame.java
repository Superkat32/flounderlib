package net.superkat.flounderlibtest.testgames;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.api.sync.FlTrackedData;
import net.superkat.flounderlib.api.sync.FlTrackedDataHandlers;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestSyncedMinigame extends FlounderGame implements SyncedFlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_synced_minigame");

    public static final Codec<TestSyncedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("playerUuid").forGetter(game -> game.player)
            ).apply(instance, TestSyncedMinigame::new)
    );

    public static final PacketCodec<RegistryByteBuf, TestSyncedMinigame> PACKET_CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, game -> game.player, TestSyncedMinigame::new
    );

    public static final FlTrackedData<Boolean> MY_BOOLEAN = FlounderDataTracker.registerData(TestSyncedMinigame.class, FlTrackedDataHandlers.BOOLEAN);
    public static final FlTrackedData<Integer> MY_INT = FlounderDataTracker.registerData(TestSyncedMinigame.class, FlTrackedDataHandlers.INTEGER);
    public static final FlTrackedData<String> MY_STRING = FlounderDataTracker.registerData(TestSyncedMinigame.class, FlTrackedDataHandlers.STRING);

    public final UUID player;
    public final FlounderDataTracker dataTracker = this.createDataTracker();

    // The main constructor when we want to start this minigame
    public TestSyncedMinigame(ServerPlayerEntity player) {
        this.player = player.getUuid();
    }

    // The persistent constructor when the minigame is reloaded upon world rejoin
    // This also happens to be the packet constructor because the packet codec uses this constructor
    public TestSyncedMinigame(UUID player) {
        this.player = player;
    }

    // Defining specific data which should be synced between all listening client(s)
    @Override
    public void initDataTracker(FlounderDataTracker.Builder builder) {
        builder.add(MY_BOOLEAN, false);
        builder.add(MY_INT, 0);
        builder.add(MY_STRING, "e");
    }

    @Override
    public void initialize(World world, int minigameId) {
        super.initialize(world, minigameId);
        if(!this.world.isClient) {
            this.dataTracker.addPlayerListener((ServerPlayerEntity) this.world.getPlayerByUuid(this.player));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.tickDataTracker(this.world.isClient());

        if(this.ticks % 80 == 0) {
            boolean myBool = this.dataTracker.get(MY_BOOLEAN);
            FlounderLibTest.LOGGER.info(String.valueOf(myBool));
            this.dataTracker.set(MY_BOOLEAN, !myBool);

            myBool = this.dataTracker.get(MY_BOOLEAN);
            FlounderLibTest.LOGGER.info(String.valueOf(myBool));
        }

        if(this.ticks == 200) {
            FlounderLibTest.LOGGER.info("what's up home homie buddy");
            this.dataTracker.set(MY_INT, 200);
        }

        if(this.ticks == 290) {
            this.dataTracker.set(MY_STRING, "What's up homie buddy?");
        }

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

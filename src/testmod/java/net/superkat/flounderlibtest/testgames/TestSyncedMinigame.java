package net.superkat.flounderlibtest.testgames;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.api.sync.FlDataSyncer;
import net.superkat.flounderlib.api.sync.FlounderDataSyncer;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestSyncedMinigame extends FlounderGame implements SyncedFlounderGame<TestSyncedMinigame> {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_synced_minigame");

    public static final Codec<TestSyncedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("playerUuid").forGetter(game -> game.playerUuid),
                    Codec.BOOL.fieldOf("test_bool").forGetter(game -> game.testBool),
                    Codec.INT.fieldOf("test_int").forGetter(game -> game.testInt),
                    Codec.FLOAT.fieldOf("test_float").forGetter(game -> game.testFloat)
            ).apply(instance, TestSyncedMinigame::new)
    );

    public static final PacketCodec<ByteBuf, TestSyncedMinigame> PACKET_CODEC = PacketCodecs.unlimitedCodec(CODEC);
//    public static final PacketCodec<RegistryByteBuf, TestSyncedMinigame> TEST = PacketCodec.tuple(
//            PacketCodecs.BOOLEAN, game -> game.testBool, TestSyncedMinigame::new
//    );

    public static final FlounderDataSyncer<TestSyncedMinigame> DATA_SYNCER = new FlounderDataSyncer<>(
            FlDataSyncer.tuple(Codec.BOOL, TestSyncedMinigame::isTestBool, TestSyncedMinigame::setTestBool),
            FlDataSyncer.tuple(Codec.INT, TestSyncedMinigame::getTestInt, TestSyncedMinigame::setTestInt),
            FlDataSyncer.tuple(Codec.FLOAT, TestSyncedMinigame::getTestFloat, TestSyncedMinigame::setTestFloat)
    );

    public boolean testBool;
    public int testInt;
    public float testFloat;
    public final UUID playerUuid;

    public TestSyncedMinigame(UUID playerUuid, boolean testBool, int testInt, float testFloat) {
        this.playerUuid = playerUuid;
        this.testBool = testBool;
        this.testInt = testInt;
        this.testFloat = testFloat;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.ticks >= 300) {
            this.invalidate();
        }
    }

    public boolean isTestBool() {
        return testBool;
    }

    public void setTestBool(boolean testBool) {
        this.testBool = testBool;
    }

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public float getTestFloat() {
        return testFloat;
    }

    public void setTestFloat(float testFloat) {
        this.testFloat = testFloat;
    }

    @Override
    public FlounderDataSyncer<TestSyncedMinigame> getFlounderDataSyncer() {
        return DATA_SYNCER;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return ID;
    }
}

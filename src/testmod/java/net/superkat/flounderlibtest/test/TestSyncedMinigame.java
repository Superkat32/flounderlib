package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.api.minigame.v1.game.SyncedFlounderGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.api.text.v1.FlounderTextApi;
import net.superkat.flounderlib.api.text.v1.builtin.ColoredObjectiveText;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestSyncedMinigame extends SyncedFlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_synced_minigame");

    public static final Codec<TestSyncedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos),
                    Codec.BOOL.fieldOf("myBoolean").forGetter(game -> game.myBoolean),
                    Codec.INT.fieldOf("myInteger").forGetter(game -> game.myInteger),
                    Codec.STRING.fieldOf("myString").forGetter(game -> game.myString),
                    Vec3d.CODEC.fieldOf("myVec3d").forGetter(game -> game.myVec3d),
                    TextCodecs.CODEC.fieldOf("myText").forGetter(game -> game.myText)
            ).apply(instance, TestSyncedMinigame::new)
    );

    public static final FlounderStateSyncer<TestSyncedMinigame, SyncState> STATE_SYNCER = FlounderStateSyncer.create(TestSyncedMinigame.class, SyncState::new)
            .addInteger(TestSyncedMinigame::getTicks, SyncState::setTicks)
            .addBoolean(game -> game.myBoolean, (syncState, value) -> syncState.myBoolean = value)
            .addInteger(game -> game.myInteger, (syncState, value) -> syncState.myInteger = value)
            .addString(game -> game.myString, (syncState, value) -> syncState.myString = value)
            .addVec3d(game -> game.myVec3d, (syncState, value) -> syncState.myVec3d = value)
            .addText(game -> game.myText, (syncState, value) -> syncState.myText = value);

    public boolean myBoolean = true;
    public int myInteger = 0;
    public String myString = "Aha!";
    public Vec3d myVec3d = Vec3d.ZERO;
    public Text myText = Text.translatable("item.minecraft.spyglass");

    public TestSyncedMinigame(BlockPos centerPos) {
        super(centerPos);
    }

    public TestSyncedMinigame(int ticks, BlockPos centerPos, Boolean myBoolean, int myInteger, String myString, Vec3d myVec3d, Text myText) {
        super(ticks, centerPos);
        this.myBoolean = myBoolean;
        this.myInteger = myInteger;
        this.myString = myString;
        this.myVec3d = myVec3d;
        this.myText = myText;
    }

    @Override
    public void tick() {
        super.tick();

        if(this.ticks % 100 == 0) {
            this.myBoolean = !this.myBoolean;
        }
        if(this.ticks % 80 == 0) {
            this.myInteger += 1;
        }
        if(this.ticks % 60 == 0) {
            this.myString = this.myText.getString();
        }
        if(this.ticks % 40 == 0) {
            this.myVec3d = this.myVec3d.add(1);
        }
        if(this.ticks % 20 == 0) {
            this.myText = Text.literal(NameGenerator.name(UUID.randomUUID()));

            // Send all players the message (to ensure it syncs correctly)
            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(this.myText);
            }

        }

        this.markDirty();
        if(this.ticks >= 500) {
            this.invalidate();
        }
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        super.addPlayer(player);

        // Send the player a joining message
        FlounderTextApi.send(new ColoredObjectiveText(Text.of("Joined minigame!"), Colors.LIGHT_YELLOW), player);
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        super.removePlayer(player);
        FlounderTextApi.send(new ColoredObjectiveText(Text.of("Left minigame!"), Colors.PURPLE), player);
    }

    @Override
    public void invalidate() {
        // Send all in the minigame players a message that the game has ended
        for (ServerPlayerEntity player : this.getPlayers()) {
            player.sendMessage(Text.literal("Minigame ended!"), true);
            FlounderTextApi.send(new ColoredObjectiveText(Text.of("Minigame ended!"), Colors.CYAN), player);
        }
        super.invalidate();
    }

    @Override
    public @NotNull FlounderGameType<?> getGameType() {
        return FlounderLibTest.TEST_SYNCED_MINIGAME_TYPE;
    }

    public static class SyncState implements FlounderSyncState {
        public int ticks = 0;
        public boolean myBoolean = true;
        public int myInteger = 0;
        public String myString = "Aha!";
        public Vec3d myVec3d = Vec3d.ZERO;
        public Text myText = Text.translatable("item.minecraft.spyglass");

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }
    }
}

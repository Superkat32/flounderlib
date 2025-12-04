package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.api.minigame.v1.game.SyncedFlounderGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlDataKey;
import net.superkat.flounderlib.impl.minigame.sync.FlounderSyncState;
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

    public static final FlDataKey<Integer> TICKS_KEY = FlDataKey.ofInt();
    public static final FlDataKey<Boolean> MY_BOOLEAN_KEY = FlDataKey.ofBoolean();
    public static final FlDataKey<Integer> MY_INTEGER_KEY = FlDataKey.ofInt();
    public static final FlDataKey<String> MY_STRING_KEY = FlDataKey.ofString();
    public static final FlDataKey<Vec3d> MY_VEC_3D_KEY = FlDataKey.ofVec3d();
    public static final FlDataKey<Text> MY_TEXT_KEY = FlDataKey.ofText();

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
    public void addDataValues(FlounderSyncState.Builder builder) {
        builder.addKey(TICKS_KEY, () -> this.ticks);
        builder.addKey(MY_BOOLEAN_KEY, () -> this.myBoolean);
        builder.addKey(MY_INTEGER_KEY, () -> this.myInteger);
        builder.addKey(MY_STRING_KEY, () -> this.myString);
        builder.addKey(MY_VEC_3D_KEY, () -> this.myVec3d);
        builder.addKey(MY_TEXT_KEY, () -> this.myText);
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
        return FlounderLibTest.TEST_SYNCED_MINIGAME;
    }
}

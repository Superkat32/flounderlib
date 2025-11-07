package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.sync.FlounderSyncData;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestSyncedMinigame extends SyncedFlounderGame<TestSyncedMinigame.Data> {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_synced_minigame");
    public static final Codec<TestSyncedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos),
                    Codec.STRING.fieldOf("title").forGetter(game -> game.title)
            ).apply(instance, TestSyncedMinigame::new)
    );

    public String title;

    public TestSyncedMinigame(BlockPos centerPos, String title) {
        super(centerPos);
        this.title = title;
    }

    public TestSyncedMinigame(int ticks, BlockPos centerPos, String title) {
        super(ticks, centerPos);
        this.title = title;
    }

    @Override
    public void tick() {
        super.tick();

        if(this.ticks % 20 == 0) {
            if(this.ticks != 20) {
                this.title = Text.literal(NameGenerator.name(UUID.randomUUID())).toString();
            }

            // Send all players the message
            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(Text.of(this.title));
            }

            this.markDirty();
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
    public Data createData() {
        return new Data(this.ticks, this.title);
    }

    @Override
    public @NotNull FlounderGameType<?> getGameType() {
        return FlounderLibTest.TEST_SYNCED_MINIGAME;
    }

    public record Data(int ticks, String title) implements FlounderSyncData {
        public static final PacketCodec<RegistryByteBuf, Data> DATA_PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.INTEGER, data -> data.ticks,
                PacketCodecs.STRING, data -> data.title,
                Data::new
        );
    }
}

package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

public class SimpleTestMinigame extends FlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "simple_test");
    public static final Codec<SimpleTestMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos)
            ).apply(instance, SimpleTestMinigame::new)
    );

    public SimpleTestMinigame(BlockPos centerPos) {
        super(centerPos);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.ticks >= 300) {
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
        return FlounderLibTest.SIMPLE_TEST_TYPE;
    }
}

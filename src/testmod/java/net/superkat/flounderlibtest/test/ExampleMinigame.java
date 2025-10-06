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
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ExampleMinigame extends FlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "example_minigame");
    public static final Codec<ExampleMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos),
                    Codec.BOOL.fieldOf("myBoolean").forGetter(game -> game.myBoolean),
                    Codec.INT.fieldOf("myInteger").forGetter(game -> game.myInteger),
                    Codec.STRING.fieldOf("myString").forGetter(game -> game.myString),
                    Vec3d.CODEC.fieldOf("myVec3d").forGetter(game -> game.myVec3d),
                    TextCodecs.CODEC.fieldOf("myText").forGetter(game -> game.myText)
            ).apply(instance, ExampleMinigame::new)
    );

    public boolean myBoolean = true;
    public int myInteger = 0;
    public String myString = "Aha!";
    public Vec3d myVec3d = Vec3d.ZERO;
    public Text myText = Text.translatable("item.minecraft.spyglass");

    // The main constructor for when we want to first start the minigame
    public ExampleMinigame(BlockPos centerPos) {
        super(centerPos);
    }

    // The persistent constructor for when the minigame is reloaded upon world rejoin
    public ExampleMinigame(int ticks, BlockPos centerPos, Boolean myBoolean, int myInteger, String myString, Vec3d myVec3d, Text myText) {
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

        // Every second, send the player a randomly generated message
        if(this.ticks % 20 == 0) {
            // Let the first message be the spyglass item translation, then generate random messages
            if(this.ticks != 20) {
                this.myText = Text.literal(NameGenerator.name(UUID.randomUUID()));
            }

            // Send all players the message
            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(this.myText);
            }

        }

        // After 300 ticks, or 15 seconds, end the minigame by invalidating it
        if (this.ticks >= 300) {
            this.invalidate();
        }
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        super.addPlayer(player);

        // Send the player a joining message
        player.sendMessage(Text.literal("Joined minigame!").formatted(Formatting.GREEN), true);
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
        return FlounderLibTest.EXAMPLE_MINIGAME_TYPE;
    }
}

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
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerBlockPos),
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

    public ExampleMinigame(BlockPos centerBlockPos) {
        super(centerBlockPos);
    }

    public ExampleMinigame(int ticks, BlockPos centerBlockPos, Boolean myBoolean, int myInteger, String myString, Vec3d myVec3d, Text myText) {
        super(ticks, centerBlockPos);
        this.myBoolean = myBoolean;
        this.myInteger = myInteger;
        this.myString = myString;
        this.myVec3d = myVec3d;
        this.myText = myText;
    }

    @Override
    public void tick() {
        super.tick();

        if(this.ticks % 20 == 0) {
            if(this.ticks != 20) {
                this.myText = Text.literal(NameGenerator.name(UUID.randomUUID()));
            }

            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(this.myText);
            }

        }

        if (this.ticks >= 300) {
            this.invalidate();
        }
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        super.addPlayer(player);

        player.sendMessage(Text.literal("Joined minigame!").formatted(Formatting.GREEN), true);
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        super.removePlayer(player);

        player.sendMessage(Text.literal("Left minigame!").formatted(Formatting.RED), true);
    }

    @Override
    public void invalidate() {
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

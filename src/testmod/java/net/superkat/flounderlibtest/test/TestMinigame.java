package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.CommonColors;
import net.minecraft.world.phys.Vec3;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.text.v1.FlounderTextApi;
import net.superkat.flounderlib.api.text.v1.builtin.ColoredObjectiveText;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestMinigame extends FlounderGame {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLibTest.MOD_ID, "test_minigame");
    public static final Codec<TestMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos),
                    Codec.BOOL.fieldOf("myBoolean").forGetter(game -> game.myBoolean),
                    Codec.INT.fieldOf("myInteger").forGetter(game -> game.myInteger),
                    Codec.STRING.fieldOf("myString").forGetter(game -> game.myString),
                    Vec3.CODEC.fieldOf("myVec3d").forGetter(game -> game.myVec3d),
                    ComponentSerialization.CODEC.fieldOf("myText").forGetter(game -> game.myText)
            ).apply(instance, TestMinigame::new)
    );

    public boolean myBoolean = true;
    public int myInteger = 0;
    public String myString = "Aha!";
    public Vec3 myVec3d = Vec3.ZERO;
    public Component myText = Component.translatable("item.minecraft.spyglass");

    // The main constructor for when we want to first start the minigame
    public TestMinigame(BlockPos centerPos) {
        super(centerPos);
    }

    // The persistent constructor for when the minigame is reloaded upon world rejoin
    public TestMinigame(int ticks, BlockPos centerPos, Boolean myBoolean, int myInteger, String myString, Vec3 myVec3d, Component myText) {
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
                this.myText = Component.literal(DebugEntityNameGenerator.getEntityName(UUID.randomUUID()));
            }

            // Send all players the message
            for (ServerPlayer player : this.getPlayers()) {
                player.sendSystemMessage(this.myText);
            }

        }

        // After 300 ticks, or 15 seconds, end the minigame by invalidating it
        if (this.ticks >= 300) {
            this.invalidate();
        }
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        super.addPlayer(player);

        // Send the player a joining message
        player.displayClientMessage(Component.literal("Joined minigame!").withStyle(ChatFormatting.GREEN), true);
        FlounderTextApi.send(new ColoredObjectiveText(Component.nullToEmpty("Joined minigame!"), CommonColors.SOFT_YELLOW), player);
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        super.removePlayer(player);

        // Send the player a leaving message
        player.displayClientMessage(Component.literal("Left minigame!").withStyle(ChatFormatting.RED), true);
        FlounderTextApi.send(new ColoredObjectiveText(Component.nullToEmpty("Left minigame!"), CommonColors.DARK_PURPLE), player);
    }

    @Override
    public void invalidate() {
        // Send all in the minigame players a message that the game has ended
        for (ServerPlayer player : this.getPlayers()) {
            player.displayClientMessage(Component.literal("Minigame ended!"), true);
            FlounderTextApi.send(new ColoredObjectiveText(Component.nullToEmpty("Minigame ended!"), CommonColors.HIGH_CONTRAST_DIAMOND), player);
        }
        super.invalidate();
    }

    @Override
    public @NotNull FlounderGameType<?> getGameType() {
        return FlounderLibTest.TEST_MINIGAME_TYPE;
    }
}

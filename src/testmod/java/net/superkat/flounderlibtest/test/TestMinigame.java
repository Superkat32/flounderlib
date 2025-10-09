package net.superkat.flounderlibtest.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

public class TestMinigame extends FlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_minigame");

    public static final Codec<TestMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(game -> game.ticks),
                    BlockPos.CODEC.fieldOf("pos").forGetter(game -> game.centerPos),
                    Codec.BOOL.fieldOf("test").forGetter(game -> game.test)
            ).apply(instance, TestMinigame::new)
    );

    public boolean test;

    public TestMinigame(BlockPos pos) {
        super(pos);
    }

    public TestMinigame(int ticks, BlockPos pos, boolean test) {
        super(pos);
        this.ticks = ticks;
        this.test = test;
    }

    @Override
    public void init(ServerWorld world, int minigameId) {
        super.init(world, minigameId);
//        this.test = this.world.getRandom().nextBoolean();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.ticks >= 500) {
            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(Text.literal("Minigame ended!").formatted(Formatting.ITALIC));
            }
            this.invalidate();
        } else if(this.ticks % 10 == 0) {
            for (ServerPlayerEntity player : this.getPlayers()) {
                player.sendMessage(Text.literal("In minigame " + this.getMinigameId()), true);
            }
            this.world.spawnParticles(ParticleTypes.END_ROD, this.centerPos.getX(), this.centerPos.getY() + 1, this.centerPos.getZ(), 1, 0, 0, 0, 0);
            this.spawnBorderParticles();
        }
    }

    public void spawnBorderParticles() {
        float range = this.playerSearchDistance();
        float rangeTimesALotOfDigits = MathHelper.ceil((float) Math.PI * range * range) / 50f;
        for (int i = 0; i < rangeTimesALotOfDigits; i++) {
            float h = this.world.getRandom().nextFloat() * (float) (Math.PI * 2);
            float k = MathHelper.sqrt(this.world.getRandom().nextFloat()) * range;
            double x = this.centerPos.getX() + (double)(MathHelper.cos(h) * range);
            double y = this.centerPos.getY();
            double z = this.centerPos.getZ() + (double)(MathHelper.sin(h) * range);
            this.world.spawnParticles(this.getBorderParticle(), x, y + 1, z, 1, 0, 0, 0, 0);
        }
    }

    public ParticleEffect getBorderParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    @Override
    public @NotNull FlounderGameType<?> getGameType() {
        return FlounderLibTest.TEST_MINIGAME_TYPE;
    }
}

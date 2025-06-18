package net.superkat.flounderlibtest.games;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.superkat.flounderlib.api.minigame.DataTrackedSyncedFlounderGame;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.api.sync.FlTrackedData;
import net.superkat.flounderlib.api.sync.FlTrackedDataHandlers;
import net.superkat.flounderlib.api.sync.FlounderDataTracker;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MoveQuicklyGame extends FlounderGame implements DataTrackedSyncedFlounderGame {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "move_quickly_game");

    public static final Codec<MoveQuicklyGame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("playerUuid").forGetter(game -> game.playerUuid),
                    BlockPos.CODEC.fieldOf("startPos").forGetter(game -> game.start),
                    BlockPos.CODEC.fieldOf("endPos").forGetter(game -> game.end),
                    Codec.INT.fieldOf("ticksLeft").forGetter(game -> game.ticksRemaining),
                    Codec.INT.fieldOf("completedRounds").forGetter(game -> game.completedRounds),
                    Codec.BOOL.fieldOf("ended").forGetter(game -> game.ended),
                    Codec.INT.fieldOf("ticksSinceEnd").forGetter(game -> game.ticksSinceEnd)
            ).apply(instance, MoveQuicklyGame::new)
    );

    public static final PacketCodec<RegistryByteBuf, MoveQuicklyGame> PACKET_CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, game -> game.playerUuid,
            BlockPos.PACKET_CODEC, game -> game.start,
            BlockPos.PACKET_CODEC, game -> game.end,
            PacketCodecs.INTEGER, game -> game.ticksRemaining,
            PacketCodecs.INTEGER, game -> game.completedRounds,
            PacketCodecs.BOOLEAN, game -> game.ended,
            PacketCodecs.INTEGER, game -> game.ticksSinceEnd,
            MoveQuicklyGame::new
    );

    public static final FlTrackedData<BlockPos> TARGET_POS = FlounderDataTracker.registerData(MoveQuicklyGame.class, FlTrackedDataHandlers.BLOCK_POS);

    public FlounderDataTracker dataTracker = this.createDataTracker();

    public final UUID playerUuid;
    public BlockPos start;
    public BlockPos end;
    public int ticksRemaining = 0;
    public int completedRounds = 0;
    public boolean ended = false;
    public int ticksSinceEnd = 0;

    public int distance = 0;
    public int ticksSinceWithinRange = 0;
    public int ticksWithoutPlayer = 0;

    public MoveQuicklyGame(ServerWorld world, ServerPlayerEntity player) {
        this.world = world;
        this.playerUuid = player.getUuid();

        this.startRound();
        this.dataTracker = createDataTracker();
    }

    public MoveQuicklyGame(UUID playerUuid, BlockPos start, BlockPos end, int ticksRemaining, int completedRounds, boolean ended, int ticksSinceEnd) {
        this.playerUuid = playerUuid;
        this.start = start;
        this.end = end;
        this.ticksRemaining = ticksRemaining;
        this.completedRounds = completedRounds;
        this.ended = ended;
        this.ticksSinceEnd = ticksSinceEnd;
    }

    @Override
    public void initDataTracker(FlounderDataTracker.Builder builder) {
        builder.add(TARGET_POS, this.end);
    }

    public void startRound() {
        PlayerEntity player = getPlayer();
        if(player == null) return;

        this.start = player.getBlockPos();

        Random random = world.getRandom();
        int x = this.start.getX() + random.nextBetween(-100, 100);
        int z = this.start.getZ() + random.nextBetween(-100, 100);
        int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z) + 1;
        this.end = new BlockPos(x, y, z);
        this.dataTracker.set(TARGET_POS, this.end);

        player.playSoundToPlayer(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 1f, 1f);
        this.calcDistance();

        this.ticksRemaining = (int) (40 + (this.distance * 8.5));

        this.ticksSinceWithinRange = 0;
    }

    @Override
    public void tick() {
        super.tick();

        PlayerEntity player = getPlayer();
        if(player == null) {
            ticksWithoutPlayer++;
            if(ticksWithoutPlayer >= 500) {
                this.invalidate();
            }
            return;
        };

        this.ticksRemaining--;
        if(!this.ended && this.ticks % 10 == 0) {
            this.calcDistance();
        }

        if(!this.ended && this.ticks % 5 == 0) {
            BlockPos target = this.dataTracker.get(TARGET_POS);
            this.world.addParticleClient(
                    ParticleTypes.TRIAL_SPAWNER_DETECTION,
                    target.getX() + this.world.getRandom().nextGaussian(),
                    target.getY() + this.world.getRandom().nextGaussian(),
                    target.getZ() + this.world.getRandom().nextGaussian(),
                    0,
                    0.5,
                    0
            );
//            this.world.spawnParticles(
//                    getPlayer(),
//                    ParticleTypes.TRIAL_SPAWNER_DETECTION,
//                    false, true, //don't force, but are important
//                    this.end.getX(), this.end.getY(), this.end.getZ(),
//                    this.world.getRandom().nextBetween(1, 7),
//                    0.5, 0.5, 0.5,
//                    0
//            );

            if(withinDistance()) {
                this.ticksSinceWithinRange++;
                if(this.ticksSinceWithinRange >= 10) {
                    this.ticksRemaining -= 5;
                    if(this.ticksSinceWithinRange >= 20) {
                        this.ticksRemaining -= 20;
                    }
                    if(this.ticksRemaining < 0) ticksRemaining = 0;
                    calcDistance();
                }
            }
        }

        if(this.world.isClient) return;

        if(this.ticksRemaining <= 0) {
            if(this.withinDistance()) {
                getPlayer().playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE);
                completedRounds++;
                if(completedRounds >= 3) {
                    this.ended = true;
                    if(ticksSinceEnd == 0) {
                        getPlayer().playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);
                        showTitle(Text.literal("You win!").formatted(Formatting.GREEN, Formatting.BOLD));
                    }
                } else {
                    startRound();
                    calcDistance();
                }
            } else {
                this.ended = true;
                if(ticksSinceEnd == 0) {
                    getPlayer().playSoundToPlayer(SoundEvents.ITEM_TRIDENT_RIPTIDE_1.value(), SoundCategory.PLAYERS, 1f, 0.2f);
                    showTitle(Text.literal("Game over!").formatted(Formatting.RED, Formatting.BOLD));
                }
            }

        }

        if(ended) {
            ticksSinceEnd++;
            if(ticksSinceEnd >= 80) {
                this.invalidate();
            }
        }

    }

    public void showTitle(Text title) {
        if(this.world.isClient) return;

        ServerPlayerEntity player = (ServerPlayerEntity) this.getPlayer();
        if(player == null) return;

        player.networkHandler.sendPacket(new TitleS2CPacket(title));
    }

    public void calcDistance() {
        PlayerEntity player = this.getPlayer();
        this.distance = (int) Math.sqrt(player.getBlockPos().getSquaredDistance(this.dataTracker.get(TARGET_POS)));
        int secondsLeft = this.ticksRemaining / 20;

        boolean withinDistance = withinDistance();
        boolean danger = !withinDistance && secondsLeft <= 10 && ticksRemaining > 0;
        player.sendMessage(Text.literal(this.distance + "m" + " - " + secondsLeft).formatted(danger ? Formatting.BOLD : Formatting.WHITE, withinDistance ? Formatting.GREEN : Formatting.RED), true);
    }

    public boolean withinDistance() {
        return this.distance <= 5;
    }

    // The player can be null either because they have died (even upon respawning, a new entity is created),
    // or they have left the game.
    // Null player entities must be accounted for, otherwise you'll have lots of issues!
    @Nullable
    public PlayerEntity getPlayer() {
        return this.world.getPlayerByUuid(this.playerUuid);
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


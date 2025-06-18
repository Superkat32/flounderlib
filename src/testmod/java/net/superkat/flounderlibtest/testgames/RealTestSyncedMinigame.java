package net.superkat.flounderlibtest.testgames;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;
import net.superkat.flounderlib.network.sync.packets.FlounderGameCreationS2CPacket;
import net.superkat.flounderlibtest.FlounderLibTest;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class RealTestSyncedMinigame extends FlounderGame implements SyncedFlounderGame {

    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "real_test_synced_minigame");

    public static final Codec<RealTestSyncedMinigame> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("playerUuid").forGetter(game -> game.player)
            ).apply(instance, RealTestSyncedMinigame::new)
    );

    public static final MapCodec<RealTestSyncedMinigame> MAP_CODEC = MapCodec.assumeMapUnsafe(CODEC);

    public static final PacketCodec<RegistryByteBuf, RealTestSyncedMinigame> PACKET_CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, game -> game.player,
            RealTestSyncedMinigame::new
    );

    public boolean dirty = false;
    public UUID player;

    public RealTestSyncedMinigame(UUID player) {
        this.player = player;
        this.addPlayer(player);
    }

    @Override
    public void initialize(World world, int minigameId) {
        super.initialize(world, minigameId);
        if(world.isClient) return;

        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        if(player == null) return;

        FlounderGameCreationS2CPacket creationPacket = new FlounderGameCreationS2CPacket(minigameId, this);
        this.sendPacket(player.networkHandler, creationPacket);

        this.sync(Set.of(player));
    }

    @Override
    public void tick() {
        super.tick();

        if(this.ticks % 50 == 0 && !this.world.isClient) {
            this.player = UUID.randomUUID();
            this.sync(this.getPlayers());
        }

        if(this.ticks >= 300) {
            this.invalidate();
        }
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return ID;
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void writeSyncNbt(NbtCompound nbt) {
        nbt.copyFromCodec(MAP_CODEC, this);
    }

    @Override
    public void readSyncNbt(NbtCompound nbt) {
        Optional<RealTestSyncedMinigame> decoded = nbt.decode(MAP_CODEC);
        if(decoded.isEmpty()) return;

        RealTestSyncedMinigame game = decoded.get();
    }

//    @Override
//    public MapCodec<RealTestSyncedMinigame> getMapCodec() {
//        return MAP_CODEC;
//    }

//    @Override
//    public PacketCodec<RegistryByteBuf, RealTestSyncedMinigame> getPacketCodec() {
//        return PACKET_CODEC;
//    }

}

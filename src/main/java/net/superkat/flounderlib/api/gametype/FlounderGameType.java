package net.superkat.flounderlib.api.gametype;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.IFlounderGame;
import org.jetbrains.annotations.Nullable;

public record FlounderGameType<T extends IFlounderGame>(
        Identifier id,
        @Nullable Codec<T> codec,
        @Nullable PacketCodec<RegistryByteBuf, T> packetCodec,
        int searchDistance
) {

    public boolean isPersistent() {
        return this.codec != null;
    }

    public boolean shouldSync() {
        return this.packetCodec != null;
    }

}

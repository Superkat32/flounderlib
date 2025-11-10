package net.superkat.flounderlib.api.text.core;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.FlounderTextApi;

public record FlounderTextType<T extends FlounderText>(Identifier id, MapCodec<T> codec, FlounderTextRenderer<T> renderer) {
    public void send(ServerPlayerEntity player, T text) {
        FlounderTextApi.sendText(player, this.id, text);
    }
}

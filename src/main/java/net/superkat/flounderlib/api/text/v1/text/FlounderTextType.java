package net.superkat.flounderlib.api.text.v1.text;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.v1.FlounderTextApi;
import net.superkat.flounderlib.api.text.v1.text.client.FlounderTextRenderer;

public record FlounderTextType<T extends FlounderText>(Identifier id, MapCodec<T> codec, FlounderTextRenderer<T> renderer) {
    public void send(ServerPlayerEntity player, T text) {
        FlounderTextApi.sendText(player, this.id, text);
    }
}

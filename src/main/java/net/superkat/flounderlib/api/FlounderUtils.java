package net.superkat.flounderlib.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.TeleportTarget;

import java.util.List;

/**
 * Helper methods for minigames to work with.<br><br>
 * Spawning/removing multiple entities in an area, spawning an entity from a list of entities, teleporting multiple players, rewarding multiple players items, and syncing data to multiple players can also be done here.
 *
 * @see FlounderApi
 */
public class FlounderUtils {

    public static void teleportPlayersToRespawn(List<ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            if(player == null || player.isDead()) continue;
            teleportPlayerToRespawn(player);
        }
    }

    public static void teleportPlayerToRespawn(ServerPlayerEntity player) {
        TeleportTarget teleportTarget = player.getRespawnTarget(false, TeleportTarget.NO_OP);
        player.teleportTo(teleportTarget);
    }

}

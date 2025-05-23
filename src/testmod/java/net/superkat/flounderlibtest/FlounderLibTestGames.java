package net.superkat.flounderlibtest;

import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlibtest.games.MoveQuicklyGame;

public class FlounderLibTestGames {

    public static final FlounderGameType<MoveQuicklyGame> MOVE_QUICKLY_GAME = FlounderApi.createPersistentSynced(
            MoveQuicklyGame.ID,
            MoveQuicklyGame.CODEC,
            MoveQuicklyGame.PACKET_CODEC
    );

    public static void onInit() {

    }

}

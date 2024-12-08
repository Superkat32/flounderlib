package net.superkat.flounderlib.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically sync data between the server and all participating clients. <br>
 * Helpful for using minigame data in a minigame hud renderer.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SyncedMinigameData {

}

package net.superkat.flounderlib.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically handle a minigame's stored NBT. <br>
 * Any field with this annotation will be stored upon the world's closing, and will be restored upon the world's reloading. <br>
 * This should be used for handling information such as the time remaining, tasks left to do, etc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinigameNbt {
    String name() default "";
}

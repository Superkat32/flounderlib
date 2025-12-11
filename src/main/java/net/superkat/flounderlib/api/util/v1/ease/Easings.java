package net.superkat.flounderlib.api.util.v1.ease;

import net.minecraft.util.math.MathHelper;

/**
 * An interface with various functions for easing, all taking advantage of any Minecraft-specific {@link MathHelper} functions/values.<br><br>
 *
 * I use easings from this website so often with animations that I figured I'd just go on ahead and provide this.
 *
 * <ul>
 *     <li>{@link Easings#easeInSine(float)}, {@link Easings#easeOutSine(float)}, {@link Easings#easeInOutSine(float)}</li>
 *     <li>{@link Easings#easeInQuad(float)}, {@link Easings#easeOutQuad(float)}, {@link Easings#easeInOutQuad(float)}</li>
 *     <li>{@link Easings#easeInCubic(float)}, {@link Easings#easeOutCubic(float)}, {@link Easings#easeInOutCubic(float)}</li>
 *     <li>{@link Easings#easeInQuart(float)}, {@link Easings#easeOutQuart(float)}, {@link Easings#easeInOutQuart(float)}</li>
 *     <li>{@link Easings#easeInQuint(float)}, {@link Easings#easeOutQuint(float)}, {@link Easings#easeInOutQuint(float)}</li>
 *     <li>{@link Easings#easeInExpo(float)}, {@link Easings#easeOutExpo(float)}, {@link Easings#easeInOutExpo(float)}</li>
 *     <li>{@link Easings#easeInCirc(float)}, {@link Easings#easeOutCirc(float)}, {@link Easings#easeInOutCirc(float)}</li>
 *     <li>{@link Easings#easeInBack(float)}, {@link Easings#easeOutBack(float)}, {@link Easings#easeInOutBack(float)}</li>
 *     <li>{@link Easings#easeInElastic(float)}, {@link Easings#easeOutElastic(float)}, {@link Easings#easeInOutElastic(float)}</li>
 *     <li>{@link Easings#easeInBounce(float)}, {@link Easings#easeOutBounce(float)}, {@link Easings#easeInOutBounce(float)}</li>
 * </ul>
 *
 * @see <a href="https://easings.net/">https://easings.net/</a>
 */
public interface Easings {

    /**
     * @see <a href="https://easings.net/#easeInSine">https://easings.net/#easeInSine</a>
     */
    static float easeInSine(float delta) {
        return 1 - MathHelper.cos((delta * MathHelper.PI) / 2);
    }

    /**
     * @see <a href="https://easings.net/#easeOutSine">https://easings.net/#easeOutSine</a>
     */
    static float easeOutSine(float delta) {
        return MathHelper.sin((delta * MathHelper.PI) / 2);
    }

    /**
     * @see <a href="https://easings.net/#easeInOutSine">https://easings.net/#easeInOutSine</a>
     */
    static float easeInOutSine(float delta) {
        return -(MathHelper.cos(MathHelper.PI * delta) - 1) / 2;
    }



    /**
     * @see <a href="https://easings.net/#easeInQuad">https://easings.net/#easeInQuad</a>
     */
    static float easeInQuad(float delta) {
        return delta * delta;
    }

    /**
     * @see <a href="https://easings.net/#easeOutQuad">https://easings.net/#easeOutQuad</a>
     */
    static float easeOutQuad(float delta) {
        return 1 - (1 - delta) * (1 - delta);
    }

    /**
     * @see <a href="https://easings.net/#easeInOutQuad">https://easings.net/#easeInOutQuad</a>
     */
    static float easeInOutQuad(float delta) {
        return delta < 0.5 ? 2 * delta * delta : (float) (1 - Math.pow(-2 * delta + 2, 2) / 2);
    }



    /**
     * @see <a href="https://easings.net/#easeInCubic">https://easings.net/#easeInCubic</a>
     */
    static float easeInCubic(float delta) {
        return delta * delta * delta;
    }

    /**
     * @see <a href="https://easings.net/#easeOutCubic">https://easings.net/#easeOutCubic</a>
     */
    static float easeOutCubic(float delta) {
        return (float) (1 - Math.pow(1 - delta, 3));
    }

    /**
     * @see <a href="https://easings.net/#easeInOutCubic">https://easings.net/#easeInOutCubic</a>
     */
    static float easeInOutCubic(float delta) {
        return delta < 0.5 ? 4 * delta * delta * delta : (float) (1 - Math.pow(-2 * delta + 2, 3) / 2);
    }



    /**
     * @see <a href="https://easings.net/#easeInQuart">https://easings.net/#easeInQuart</a>
     */
    static float easeInQuart(float delta) {
        return delta * delta * delta * delta;
    }

    /**
     * @see <a href="https://easings.net/#easeOutQuart">https://easings.net/#easeOutQuart</a>
     */
    static float easeOutQuart(float delta) {
        return (float) (1 - Math.pow(1 - delta, 4));
    }

    /**
     * @see <a href="https://easings.net/#easeInOutQuart">https://easings.net/#easeInOutQuart</a>
     */
    static float easeInOutQuart(float delta) {
        return delta < 0.5 ? 8 * delta * delta * delta * delta : (float) (1 - Math.pow(-2 * delta + 2, 4) / 2);
    }



    /**
     * @see <a href="https://easings.net/#easeInQuint">https://easings.net/#easeInQuint</a>
     */
    static float easeInQuint(float delta) {
        return delta * delta * delta * delta * delta;
    }

    /**
     * @see <a href="https://easings.net/#easeOutQuint">https://easings.net/#easeOutQuint</a>
     */
    static float easeOutQuint(float delta) {
        return (float) (1 - Math.pow(1 - delta, 5));
    }

    /**
     * @see <a href="https://easings.net/#easeInOutQuint">https://easings.net/#easeInOutQuint</a>
     */
    static float easeInOutQuint(float delta) {
        return delta < 0.5 ? 16 * delta * delta * delta * delta * delta : (float) (1 - Math.pow(-2 * delta + 2, 5) / 2);
    }



    /**
     * @see <a href="https://easings.net/#easeInExpo">https://easings.net/#easeInExpo</a>
     */
    static float easeInExpo(float delta) {
        return delta == 0 ? 0 : (float) Math.pow(2, 10 * delta - 10);
    }

    /**
     * @see <a href="https://easings.net/#easeOutExpo">https://easings.net/#easeOutExpo</a>
     */
    static float easeOutExpo(float delta) {
        return delta == 1 ? 1 : (float) (1 - Math.pow(2, -10 * delta));
    }

    /**
     * @see <a href="https://easings.net/#easeInOutExpo">https://easings.net/#easeInOutExpo</a>
     */
    static float easeInOutExpo(float delta) {
        return delta == 0
                ? 0
                : (float) (delta == 1
                ? 1
                : delta < 0.5 ? Math.pow(2, 20 * delta - 10) / 2
                : (2 - Math.pow(2, -20 * delta + 10)) / 2);
    }



    /**
     * @see <a href="https://easings.net/#easeInCirc">https://easings.net/#easeInCirc</a>
     */
    static float easeInCirc(float delta) {
        return 1 - MathHelper.sqrt((float) (1 - Math.pow(delta, 2)));
    }

    /**
     * @see <a href="https://easings.net/#easeOutCirc">https://easings.net/#easeOutCirc</a>
     */
    static float easeOutCirc(float delta) {
        return MathHelper.sqrt((float) (1 - Math.pow(delta - 1, 2)));
    }

    /**
     * @see <a href="https://easings.net/#easeInOutCirc">https://easings.net/#easeInOutCirc</a>
     */
    static float easeInOutCirc(float delta) {
        return delta < 0.5
                ? (1 - MathHelper.sqrt((float) (1 - Math.pow(2 * delta, 2)))) / 2
                : (MathHelper.sqrt((float) (1 - Math.pow(-2 * delta + 2, 2))) + 1) / 2;
   }



    /**
     * @see <a href="https://easings.net/#easeInBack">https://easings.net/#easeInBack</a>
     */
   static float easeInBack(float delta) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;

       return c3 * delta * delta * delta - c1 * delta * delta;
   }

    /**
     * @see <a href="https://easings.net/#easeOutBack">https://easings.net/#easeOutBack</a>
     */
   static float easeOutBack(float delta) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;

       return (float) (1 + c3 * Math.pow(delta - 1, 3) + c1 * Math.pow(delta - 1, 2));
   }

    /**
     * @see <a href="https://easings.net/#easeInOutBack">https://easings.net/#easeInOutBack</a>
     */
   static float easeInOutBack(float delta) {
        float c1 = 1.70158f;
        float c2 = c1 * 1.525f;

       return (float) (delta < 0.5
                      ? (Math.pow(2 * delta, 2) * ((c2 + 1) * 2 * delta - c2)) / 2
                      : (Math.pow(2 * delta - 2, 2) * ((c2 + 1) * (delta * 2 - 2) + c2) + 2) / 2);
   }



    /**
     * @see <a href="https://easings.net/#easeInElastic">https://easings.net/#easeInElastic</a>
     */
   static float easeInElastic(float delta) {
        float c4 = (2 * MathHelper.PI) / 3;

       return delta == 0
               ? 0
               : (float) (delta == 1
               ? 1
               : -Math.pow(2, 10 * delta - 10) * MathHelper.sin((float) ((delta * 10 - 10.75) * c4)));
   }

    /**
     * @see <a href="https://easings.net/#easeOutElastic">https://easings.net/#easeOutElastic</a>
     */
   static float easeOutElastic(float delta) {
        float c4 = (2 * MathHelper.PI) / 3;

       return delta == 0
               ? 0
               : (float) (delta == 1
               ? 1
               : Math.pow(2, -10 * delta) * MathHelper.sin((float) ((delta * 10 - 0.75) * c4)) + 1);
   }

    /**
     * @see <a href="https://easings.net/#easeInOutElastic">https://easings.net/#easeInOutElastic</a>
     */
   static float easeInOutElastic(float delta) {
        float c5 = (2 * MathHelper.PI) / 4.5f;

       return delta == 0
               ? 0
               : (float) (delta == 1
               ? 1
               : delta < 0.5
               ? -(Math.pow(2, 20 * delta - 10) * MathHelper.sin((float) ((20 * delta - 11.125) * c5))) / 2
               : (Math.pow(2, -20 * delta + 10) * MathHelper.sin((float) ((20 * delta - 11.125) * c5))) / 2 + 1);
   }



    /**
     * @see <a href="https://easings.net/#easeInBounce">https://easings.net/#easeInBounce</a>
     */
   static float easeInBounce(float delta) {
        return 1 - easeOutBounce(1 - delta);
   }

    /**
     * @see <a href="https://easings.net/#easeOutBounce">https://easings.net/#easeOutBounce</a>
     */
   static float easeOutBounce(float delta) {
        float n1 = 7.5625f;
        float d1 = 2.75f;

       if (delta < 1 / d1) {
           return n1 * delta * delta;
       } else if (delta < 2 / d1) {
           return n1 * (delta -= 1.5f / d1) * delta + 0.75f;
       } else if (delta < 2.5 / d1) {
           return n1 * (delta -= 2.25f / d1) * delta + 0.9375f;
       } else {
           return n1 * (delta -= 2.625f / d1) * delta + 0.984375f;
       }
   }

    /**
     * @see <a href="https://easings.net/#easeInOutBounce">https://easings.net/#easeInOutBounce</a>
     */
   static float easeInOutBounce(float delta) {
       return delta < 0.5
               ? (1 - easeOutBounce(1 - 2 * delta)) / 2
               : (1 + easeOutBounce(2 * delta - 1)) / 2;
   }
}

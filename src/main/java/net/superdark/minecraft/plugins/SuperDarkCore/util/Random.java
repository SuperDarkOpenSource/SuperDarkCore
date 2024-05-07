package net.superdark.minecraft.plugins.SuperDarkCore.util;

public class Random
{
    /**
     * Gets a random float value from [min, max]
     * @param min minimum float value, inclusive.
     * @param max maximum float value, inclusive.
     * @return a float value between min and max.
     */
    static public float RandomRange(float min, float max)
    {
        java.util.Random r = new java.util.Random();
        r.setSeed(System.nanoTime());
        return min + r.nextFloat() * (max - min);
    }
}

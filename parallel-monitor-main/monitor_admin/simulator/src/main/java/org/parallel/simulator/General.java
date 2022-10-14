package org.parallel.simulator;

public abstract class General {
    public static double getDoubleWith2(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    public static double getMaxRandom(double max) {
        return getDoubleWith2(Math.round(max * 0.9 + Math.random() * (max - max * 0.9)));
    }

    public static double getMinRandom(double max) {
        return getDoubleWith2(max * 0.05 + Math.random() * (max * 0.1 - max * 0.05));
    }
}

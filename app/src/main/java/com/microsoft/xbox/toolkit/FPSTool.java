package com.microsoft.xbox.toolkit;

public class FPSTool {
    private static final int FPS_MAX = 60;
    private static FPSTool instance = new FPSTool();
    private int FPSSum = 0;
    private int minFPS = 0;
    private int samples = 0;

    public static FPSTool getInstance() {
        return instance;
    }

    public void addFPS(int var1) {
        if(var1 <= 60) {
            ++this.samples;
            this.FPSSum += var1;
            this.minFPS = Math.min(this.minFPS, var1);
        }
    }

    public void clearFPS() {
        this.samples = 0;
        this.FPSSum = 0;
        this.minFPS = Integer.MAX_VALUE;
    }

    public int getAverageFPS() {
        return (int)((float)this.FPSSum / (float)this.samples);
    }

    public int getMinFPS() {
        return this.minFPS;
    }
}

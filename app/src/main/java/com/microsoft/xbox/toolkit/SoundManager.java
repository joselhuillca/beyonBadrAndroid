package com.microsoft.xbox.toolkit;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.badr.nwes.beyondbadr.puzzle.PuzzleActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SoundManager {
    private static final int MAX_STREAM_SIZE = 14;
    private static final int NO_LOOP = 0;
    private AudioManager audioManager;
    private Context context;
    private boolean isEnabled;
    private ArrayList recentlyPlayedResourceIds;
    private HashMap resourceSoundIdMap;
    private SoundPool soundPool;

    private SoundManager() {
        this.resourceSoundIdMap = new HashMap();
        this.recentlyPlayedResourceIds = new ArrayList();
        this.isEnabled = false;
        boolean var1;
        if(Thread.currentThread() == ThreadManager.UIThread) {
            var1 = true;
        } else {
            var1 = false;
        }

        // XLEAssert.assertTrue("You must access sound manager on UI thread.", var1);
        this.context = PuzzleActivity.Instance.getApplicationContext();
        this.soundPool = new SoundPool(14, 3, 0);
        this.audioManager = (AudioManager)this.context.getSystemService(Context.AUDIO_SERVICE);
    }


    public static SoundManager getInstance() {
        return SoundManager.SoundManagerHolder.instance;
    }

    public void clearMostRecentlyPlayedResourceIds() {
    }

    public Integer[] getMostRecentlyPlayedResourceIds() {
        return new Integer[0];
    }

    public void loadSound(int var1) {
        if(!this.resourceSoundIdMap.containsKey(Integer.valueOf(var1))) {
            int var2 = this.soundPool.load(this.context, var1, 1);
            this.resourceSoundIdMap.put(Integer.valueOf(var1), Integer.valueOf(var2));
        }

    }

    public void playSound(int var1) {
        if(this.isEnabled) {
            if(!this.resourceSoundIdMap.containsKey(Integer.valueOf(var1))) {
                int var3 = this.soundPool.load(this.context, var1, 1);
                XLELog.Warning("SoundManager", "Loading sound right before playing. The sound might not be ready to playback right away.");
                this.resourceSoundIdMap.put(Integer.valueOf(var1), Integer.valueOf(var3));
                var1 = var3;
            } else {
                var1 = ((Integer)this.resourceSoundIdMap.get(Integer.valueOf(var1))).intValue();
            }

            float var2 = (float)this.audioManager.getStreamMaxVolume(3);
            var2 = (float)this.audioManager.getStreamVolume(3) / var2;
            XLELog.Diagnostic("SoundManager", String.format("Playing sound id %d with volume %f", new Object[]{Integer.valueOf(var1), Float.valueOf(var2)}));
            this.soundPool.play(var1, var2, var2, 1, 0, 1.0F);
        }
    }

    public void setEnabled(boolean var1) {
        if(this.isEnabled != var1) {
            this.isEnabled = var1;
        }

    }

    private static class SoundManagerHolder {
        public static final SoundManager instance = new SoundManager();
    }
}


/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.app.Application;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.BassBoost;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import android.os.Build;
import android.util.Log;


//This class returns instances of all the effects and have a lifecycle of the Application

public class EffectInstance extends Application{
    private static volatile Equalizer equalizerInstance;
    private static volatile BassBoost bassBoostInstance;
    private static volatile Virtualizer virtualizerInstance;
    private static volatile LoudnessEnhancer loudnessEnhancerInstance;
    public static int max = Integer.MAX_VALUE;

    private static final String TAG = "EffectInstance";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: EFFECTINSTANCE CREATED");
    }


    static Equalizer getEqualizerInstance(){
        if (equalizerInstance == null) {
            try {
                equalizerInstance = new Equalizer(max,0);
            } catch (RuntimeException e) {
                equalizerInstance = null;
                e.printStackTrace();
            }

        }
        return equalizerInstance;
    }
    static BassBoost getBassBoostInstance(){
        if (bassBoostInstance == null) {
            try {
                bassBoostInstance = new BassBoost(max,0);
            } catch (RuntimeException e) {
                bassBoostInstance = null;
                e.printStackTrace();
            }
        }
        return bassBoostInstance;
    }
    static Virtualizer getVirtualizerInstance(){
        if (virtualizerInstance == null) {
            try {
                virtualizerInstance = new Virtualizer(max,0);
            } catch (RuntimeException e) {
                virtualizerInstance = null;
                e.printStackTrace();
            }
        }
        return virtualizerInstance;
    }
    static LoudnessEnhancer getLoudnessEnhancerInstance(){
        if (loudnessEnhancerInstance == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    loudnessEnhancerInstance = new LoudnessEnhancer(0);
                } catch (RuntimeException e) {
                    loudnessEnhancerInstance = null;
                    e.printStackTrace();
                }
            }
        }
        return loudnessEnhancerInstance;
    }
}

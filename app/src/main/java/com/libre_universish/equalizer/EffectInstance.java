/*
 * Created by Jazib (Jazib Khan) (Jazib Khan) on 2/10/2018.
 * Created by Jazib on 2/10/2018.
 * (Version 1.1 stated “Created by Jazib on 2/11/2018.” One of the subsequent versions stated “Created by Jazib (Jazib Khan) (Jazib Khan) on 2/10/2018.” Therefore, the dates were changed to the earlier date of “2/10/2018.”)
 * 
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (codeberg and GitHub: universish)
 * Modified by Saffet Yavuz (universish) on 01/02/2026 (dd/mm/yyyy)
 * 
 * The original package ID `com.jazibkhan.equalizer` was assigned by Jazib Khan.
 * The current package ID `com.libre_universish.equalizer` has been updated by Saffet Yavuz.
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
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
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

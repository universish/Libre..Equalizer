
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.app.Application;
import android.content.Context;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;



public class PreferenceUtil {
    private static final String TAG = "PreferenceUtil";
    private static final String SPINNER_POS = "spinnerpos";
    private static final String EQSWITCH = "eqswitch";
    private static final String BBSWITCH = "bbswitch";
    private static final String VIRSWITCH = "virswitch";
    private static final String LOUDSWITCH = "loudswitch";
    private static final String BBSLIDER = "bbslider";
    private static final String VIRSLIDER = "virslider";
    private static final String LOUDSLIDER = "loudslider";
    private static final String SLIDER0 = "slider0";
    private static final String SLIDER1 = "slider1";
    private static final String SLIDER2 = "slider2";
    private static final String SLIDER3 = "slider3";
    private static final String SLIDER4 = "slider4";
    private static final String DARK_THEME = "dark_theme";
    private static final String IS_CUSTOM_SELECTED = "is_custom_selected";
    private static final String IS_PURCHASED = "is_purchased";


    private static PreferenceUtil sInstance;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private PreferenceUtil(final Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(context.getApplicationContext());
        }
        return sInstance;
    }


    public void setSpinnerPos(final int value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(SPINNER_POS, value);
        editor.apply();
    }

    public final int getSpinnerPos() {
        return mPref.getInt(SPINNER_POS, 0);
    }

    public void setBBSlider(final int value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(BBSLIDER, value);
        editor.apply();
    }

    public final int getBBSlider() {
        return mPref.getInt(BBSLIDER, 0);
    }

    public void setVirSlider(final int value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(VIRSLIDER, value);
        editor.apply();
    }

    public final int getVirSlider() {
        return mPref.getInt(VIRSLIDER, 0);
    }

    public void setLoudSlider(final float value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putFloat(LOUDSLIDER, value);
        editor.apply();
    }

    public final float getLoudSlider() {
        return mPref.getFloat(LOUDSLIDER, 0);
    }

    public void setEqSlider(final int value, int pos) {
        final SharedPreferences.Editor editor = mPref.edit();
        switch (pos) {
            case 0:
                editor.putInt(SLIDER0, value);
                break;
            case 1:
                editor.putInt(SLIDER1, value);
                break;
            case 2:
                editor.putInt(SLIDER2, value);
                break;
            case 3:
                editor.putInt(SLIDER3, value);
                break;
            case 4:
                editor.putInt(SLIDER4, value);
                break;
        }
        editor.apply();
    }

    public final int getEqSlider(int pos) {
        switch (pos) {
            case 0:
                return mPref.getInt(SLIDER0, 0);
            case 1:
                return mPref.getInt(SLIDER1, 0);
            case 2:
                return mPref.getInt(SLIDER2, 0);
            case 3:
                return mPref.getInt(SLIDER3, 0);
            case 4:
                return mPref.getInt(SLIDER4, 0);
        }
        return 0;
    }

    public void setEqSwitch(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(EQSWITCH, value);
        editor.apply();
    }

    public final boolean getEqSwitch() {
        return mPref.getBoolean(EQSWITCH, false);
    }

    public void setBBSwitch(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(BBSWITCH, value);
        editor.apply();
    }

    public final boolean getBBSwitch() {
        return mPref.getBoolean(BBSWITCH, false);
    }

    public void setVirSwitch(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(VIRSWITCH, value);
        editor.apply();
    }

    public final boolean getVirSwitch() {
        return mPref.getBoolean(VIRSWITCH, false);
    }

    public void setLoudSwitch(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(LOUDSWITCH, value);
        editor.apply();
    }

    public final boolean getIsCustomSelected() {
        return mPref.getBoolean(IS_CUSTOM_SELECTED, false);
    }

    public void setIsCustomSelected(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_CUSTOM_SELECTED, value);
        editor.apply();
    }


    public final boolean getIsPurchased() {
        return mPref.getBoolean(IS_PURCHASED, false);
    }

    public void setIsPurchased(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_PURCHASED, value);
        editor.apply();
    }



    public final boolean getLoudSwitch() {
        return mPref.getBoolean(LOUDSWITCH, false);
    }

    public void setDarkTheme(final boolean value) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(DARK_THEME, value);
        editor.apply();
    }

    public final boolean getDarkTheme() {
        return mPref.getBoolean(DARK_THEME, true);
    }

    public final boolean hasKey() {
        return mPref.contains(SPINNER_POS);
    }
}

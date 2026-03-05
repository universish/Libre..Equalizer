
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.LiveData;
import android.media.audiofx.BassBoost;
import android.app.Dialog;
import android.arch.lifecycle.AndroidViewModel;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class EqualizerViewModel extends AndroidViewModel{
    private static final String TAG = "EqualizerViewModel";
    private PreferenceUtil preferenceUtil;
    private Equalizer equalizer;
    private BassBoost bassBoost;
    private Virtualizer virtualizer;
    private LoudnessEnhancer loudnessEnhancer;
    private Integer virSlider;
    private Integer bBSlider;
    private Float loudSlider;
    private int slider[];
    private int spinnerPos;
    private Boolean virSwitch;
    private Boolean bBSwitch;
    private Boolean loudSwitch;
    private Boolean eqSwitch;
    private Boolean isCustomSelected;
    private Boolean isPurchased;
    private boolean darkTheme;
    private MutableLiveData<Boolean> isPresetClicked;

    private CustomPresetRepository mRepository;
    private LiveData<List<CustomPreset>> mAllEntry;
    private LiveData<List<String>> mPresetName;

    public EqualizerViewModel(@NonNull Application application) {
        super(application);

        mRepository = new CustomPresetRepository(application);
        mAllEntry = mRepository.getAllEntry();
        mPresetName = mRepository.getPresetName();
        isPresetClicked = new MutableLiveData<>();
        isPresetClicked.setValue(Boolean.FALSE);

        preferenceUtil = PreferenceUtil.getInstance(application);
        equalizer = EffectInstance.getEqualizerInstance();
        bassBoost = EffectInstance.getBassBoostInstance();
        virtualizer = EffectInstance.getVirtualizerInstance();
        loudnessEnhancer = EffectInstance.getLoudnessEnhancerInstance();
        slider = new int[5];
        for(int i=0;i<5;i++) {
            slider[i]=(preferenceUtil.getEqSlider(i));
        }
        virSlider=preferenceUtil.getVirSlider();
        bBSlider=(preferenceUtil.getBBSlider());
        loudSlider=(preferenceUtil.getLoudSlider());
        spinnerPos=(preferenceUtil.getSpinnerPos());
        virSwitch=(preferenceUtil.getVirSwitch());
        bBSwitch=(preferenceUtil.getBBSwitch());
        loudSwitch=(preferenceUtil.getLoudSwitch());
        eqSwitch=(preferenceUtil.getEqSwitch());
        isCustomSelected=(preferenceUtil.getIsCustomSelected());
        isPurchased = preferenceUtil.getIsPurchased();
        darkTheme=(preferenceUtil.getDarkTheme());

    }

    public MutableLiveData<Boolean> getIsPresetClicked() {
        return isPresetClicked;
    }

    public void setIsPresetClicked(boolean isPresetClicked) {
        this.isPresetClicked.setValue(isPresetClicked);
    }


    LiveData<List<CustomPreset>> getAllEntry() {
        return mAllEntry;
    }

    LiveData<List<String>> getPresetName() {
        return mPresetName;
    }

    public void insert(CustomPreset entry) {
        mRepository.insert(entry);
    }
    public void delete(CustomPreset entry) {
        mRepository.delete(entry);
    }
    public void update(CustomPreset entry) {
        mRepository.update(entry);
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public BassBoost getBassBoost() {
        return bassBoost;
    }

    public Virtualizer getVirtualizer() {
        return virtualizer;
    }

    public LoudnessEnhancer getLoudnessEnhancer() {
        return loudnessEnhancer;
    }

    public int getVirSlider() {
        return virSlider;
    }

    public void setVirSlider(int virSlider) {
        this.virSlider=(virSlider);
        preferenceUtil.setVirSlider(virSlider);
    }

    public int getBBSlider() {
        return bBSlider;
    }

    public void setBBSlider(int bBSlider) {
        this.bBSlider=(bBSlider);
        preferenceUtil.setBBSlider(bBSlider);
    }

    public float getLoudSlider() {
        return loudSlider;
    }

    public void setLoudSlider(float loudSlider) {
        this.loudSlider=(loudSlider);
        preferenceUtil.setLoudSlider(loudSlider);
    }

    public int getSlider(int pos) {
        return slider[pos];
    }

    public void setSlider(int slider, int pos) {
        this.slider[pos]=(slider);
        preferenceUtil.setEqSlider(slider,pos);
    }

    public int getSpinnerPos() {
        return spinnerPos;
    }

    public void setSpinnerPos(int spinnerPos) {
        this.spinnerPos=(spinnerPos);
        preferenceUtil.setSpinnerPos(spinnerPos);
    }

    public boolean getVirSwitch() {
        return virSwitch;
    }

    public void setVirSwitch(boolean virSwitch) {
        this.virSwitch=(virSwitch);
        preferenceUtil.setVirSwitch(virSwitch);
    }

    public boolean getbBSwitch() {
        return bBSwitch;
    }

    public void setbBSwitch(boolean bBSwitch) {
        this.bBSwitch=(bBSwitch);
        preferenceUtil.setBBSwitch(bBSwitch);
    }

    public boolean getLoudSwitch() {
        return loudSwitch;
    }

    public void setLoudSwitch(boolean loudSwitch) {
        this.loudSwitch=(loudSwitch);
        preferenceUtil.setLoudSwitch(loudSwitch);
    }

    public boolean getEqSwitch() {
        return eqSwitch;
    }

    public void setEqSwitch(boolean eqSwitch) {
        this.eqSwitch=(eqSwitch);
        preferenceUtil.setEqSwitch(eqSwitch);
    }

    public boolean getIsCustomSelected() {
        return isCustomSelected;
    }

    public void setIsCustomSelected(boolean isCustomSelected) {
        this.isCustomSelected=(isCustomSelected);
        preferenceUtil.setIsCustomSelected(isCustomSelected);
    }

    public boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased=isPurchased;
        preferenceUtil.setIsPurchased(isPurchased);
    }

    public boolean getDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme=(darkTheme);
    }
}

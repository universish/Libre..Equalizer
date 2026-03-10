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

import com.libre_universish.equalizer.R;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;


@Entity(tableName = "custom_preset")
public class CustomPreset {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "preset_name")
    private String presetName;
    @ColumnInfo(name = "vir_slider")
    private int virSlider;
    @ColumnInfo(name = "bb_slider")
    private int bbSlider;
    @ColumnInfo(name = "loud_slider")
    private float loudSlider;
    @ColumnInfo(name = "slider")
    private int[] slider;
    @ColumnInfo(name = "spinner_pos")
    private int spinnerPos;
    @ColumnInfo(name = "vir_switch")
    private Boolean virSwitch;
    @ColumnInfo(name = "bb_switch")
    private Boolean bbSwitch;
    @ColumnInfo(name = "loud_switch")
    private Boolean loudSwitch;
    @ColumnInfo(name = "eq_switch")
    private Boolean eqSwitch;
    @ColumnInfo(name = "is_custom_selected")
    private Boolean isCustomSelected;

    public CustomPreset(String presetName, int virSlider, int bbSlider, float loudSlider, int[] slider, int spinnerPos, Boolean virSwitch, Boolean bbSwitch, Boolean loudSwitch, Boolean eqSwitch, Boolean isCustomSelected) {
        this.presetName = presetName;
        this.virSlider = virSlider;
        this.bbSlider = bbSlider;
        this.loudSlider = loudSlider;
        this.slider = slider;
        this.spinnerPos = spinnerPos;
        this.virSwitch = virSwitch;
        this.bbSwitch = bbSwitch;
        this.loudSwitch = loudSwitch;
        this.eqSwitch = eqSwitch;
        this.isCustomSelected = isCustomSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public int getVirSlider() {
        return virSlider;
    }

    public void setVirSlider(int virSlider) {
        this.virSlider = virSlider;
    }

    public int getBbSlider() {
        return bbSlider;
    }

    public void setBbSlider(int bbSlider) {
        this.bbSlider = bbSlider;
    }

    public float getLoudSlider() {
        return loudSlider;
    }

    public void setLoudSlider(float loudSlider) {
        this.loudSlider = loudSlider;
    }

    public int[] getSlider() {
        return slider;
    }

    public void setSlider(int[] slider) {
        this.slider = slider;
    }

    public int getSpinnerPos() {
        return spinnerPos;
    }

    public void setSpinnerPos(int spinnerPos) {
        this.spinnerPos = spinnerPos;
    }

    public Boolean getVirSwitch() {
        return virSwitch;
    }

    public void setVirSwitch(Boolean virSwitch) {
        this.virSwitch = virSwitch;
    }

    public Boolean getBbSwitch() {
        return bbSwitch;
    }

    public void setBbSwitch(Boolean bbSwitch) {
        this.bbSwitch = bbSwitch;
    }

    public Boolean getLoudSwitch() {
        return loudSwitch;
    }

    public void setLoudSwitch(Boolean loudSwitch) {
        this.loudSwitch = loudSwitch;
    }

    public Boolean getEqSwitch() {
        return eqSwitch;
    }

    public void setEqSwitch(Boolean eqSwitch) {
        this.eqSwitch = eqSwitch;
    }

    public Boolean getCustomSelected() {
        return isCustomSelected;
    }

    public void setCustomSelected(Boolean customSelected) {
        isCustomSelected = customSelected;
    }
}

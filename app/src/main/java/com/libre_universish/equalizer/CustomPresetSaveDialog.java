
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.arch.lifecycle.ViewModel;
import android.media.audiofx.Equalizer;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.libre_universish.equalizer.EqualizerViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomPresetSaveDialog extends DialogFragment {
    EqualizerViewModel equalizerViewModel;
    EditText presetNameEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_save_preset, null);
        presetNameEditText = view.findViewById(R.id.preset_name);
        presetNameEditText.requestFocus();
        builder.setView(view);
        builder.setTitle(getString(R.string.save_as_preset)).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(presetNameEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(),getString(R.string.please_enter_preset),Toast.LENGTH_SHORT).show();
                }
                else{
                    savePreset();
                    Toast.makeText(getContext(),getString(R.string.preset_saved_successfully),Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        Dialog d = builder.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return d;
    }

    void savePreset() {
        equalizerViewModel = ViewModelProviders.of(getActivity()).get(EqualizerViewModel.class);
        String presetName = presetNameEditText.getText().toString();
        int virSlider = equalizerViewModel.getVirSlider();
        int bbSlider = equalizerViewModel.getBBSlider();
        float loudSlider = equalizerViewModel.getLoudSlider();
        int[] slider = new int[5];
        for (int i = 0; i < 5; i++) {
            slider[i] = equalizerViewModel.getSlider(i);
        }
        int spinnerPos = equalizerViewModel.getSpinnerPos();
        Boolean virSwitch = equalizerViewModel.getVirSwitch();
        Boolean bbSwitch = equalizerViewModel.getbBSwitch();
        Boolean loudSwitch = equalizerViewModel.getLoudSwitch();
        Boolean eqSwitch = equalizerViewModel.getEqSwitch();
        Boolean isCustomSelected = equalizerViewModel.getIsCustomSelected();
        CustomPreset customPreset = new CustomPreset(
                presetName, virSlider, bbSlider, loudSlider, slider, spinnerPos,
                virSwitch, bbSwitch, loudSwitch, eqSwitch, isCustomSelected);
        equalizerViewModel.insert(customPreset);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        equalizerViewModel = ViewModelProviders.of(getActivity()).get(EqualizerViewModel.class);
//
//    }
}

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

import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import androidx.lifecycle.ViewModel;
import android.media.audiofx.Equalizer;
import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import com.libre_universish.equalizer.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

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
        equalizerViewModel = new ViewModelProvider(getActivity()).get(EqualizerViewModel.class);
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

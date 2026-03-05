
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
import java.util.List;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.EditText;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.LiveData;
import android.media.audiofx.Equalizer;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class CustomPresetDialog extends DialogFragment {
    int position = 0;
    ArrayList<String> eqPresetName;
    ArrayList<CustomPreset> eqPreset;
    EqualizerViewModel equalizerViewModel;
    ArrayAdapter<String> adapter;
    boolean deleteClicked = false;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        eqPreset = new ArrayList<>();
        eqPresetName = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, eqPresetName );

        builder.setTitle(getString(R.string.load_preset)).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(deleteClicked){
                    CustomPreset preset = eqPreset.get(which);
                    equalizerViewModel.delete(preset);
                    Toast.makeText(getContext(),getString(R.string.preset_deleted_successfully),Toast.LENGTH_SHORT).show();
                }
                else{
                    //Implementing click using LiveData because I don't like interfaces.
                    CustomPreset preset = eqPreset.get(which);
                    equalizerViewModel.setVirSlider(preset.getVirSlider());
                    equalizerViewModel.setBBSlider(preset.getBbSlider());
                    equalizerViewModel.setLoudSlider(preset.getLoudSlider());
                    for(int i=0;i<5;i++){
                        equalizerViewModel.setSlider(preset.getSlider()[i],i);
                    }
                    equalizerViewModel.setIsCustomSelected(preset.getCustomSelected());
                    equalizerViewModel.setLoudSwitch(preset.getLoudSwitch());
                    equalizerViewModel.setbBSwitch(preset.getBbSwitch());
                    equalizerViewModel.setVirSwitch(preset.getVirSwitch());
                    equalizerViewModel.setEqSwitch(preset.getEqSwitch());
                    equalizerViewModel.setSpinnerPos(preset.getSpinnerPos());

                    equalizerViewModel.setIsPresetClicked(true);
                    Log.d(TAG, "onClick: "+ preset.getPresetName());
                }
            }
        }).setNeutralButton(getString(R.string.delete_preset), null);

        
        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        equalizerViewModel = ViewModelProviders.of(getActivity()).get(EqualizerViewModel.class);
        equalizerViewModel.getAllEntry().observe(getActivity(), new Observer<List<CustomPreset>>() {
            @Override
            public void onChanged(@Nullable List<CustomPreset> customPresets) {
                if(customPresets==null||customPresets.isEmpty()){
                    try{
                        Toast.makeText(getActivity(),getString(R.string.please_save_preset),Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.d(TAG, "onChanged: "+e.toString());
                    }

                    dismiss();
                }

                for (CustomPreset customPreset : customPresets){
                    adapter.add(customPreset.getPresetName());
                }
                eqPreset.addAll(customPresets);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            final Button neutralButton = (Button) d.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    d.setTitle(getString(R.string.delete_preset));
                    neutralButton.setText(getString(R.string.cancel));
                    if(deleteClicked)
                        d.dismiss();

                    deleteClicked = true;
                }
            });
        }
    }

}

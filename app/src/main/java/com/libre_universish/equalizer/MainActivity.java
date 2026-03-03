/**
 * Created by Jazib on 2/11/2018.
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import androidx.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    Switch enabled = null;
    Switch enableBass, enableVirtual, enableLoud;
    Spinner spinner;
    ArrayList<String> eqPreset;
    int spinnerPos = 0;
    boolean dontcall = false;
    boolean canPreset;
    LinearLayout presetView, loudnessView;

    Equalizer eq = null;
    BassBoost bb = null;
    Virtualizer virtualizer = null;
    LoudnessEnhancer loudnessEnhancer = null;

    int min_level = 0;
    int max_level = 100;

    static final int MAX_SLIDERS = 5; // Must match the XML layout
    SeekBar sliders[] = new SeekBar[MAX_SLIDERS];
    ArcSeekBar bassSlider, virtualSlider, loudSlider;
    TextView slider_labels[] = new TextView[MAX_SLIDERS];
    int num_sliders = 0;
    boolean canEnable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // apply theme preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("dark_theme", true)){
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        enabled = findViewById(R.id.switchEnable);
        if (enabled != null) {
            enabled.setChecked(true);
        }

        sliders[0] = findViewById(R.id.mySeekBar0);
        slider_labels[0] = findViewById(R.id.centerFreq0);
        sliders[1] = findViewById(R.id.mySeekBar1);
        slider_labels[1] = findViewById(R.id.centerFreq1);
        sliders[2] = findViewById(R.id.mySeekBar2);
        slider_labels[2] = findViewById(R.id.centerFreq2);
        sliders[3] = findViewById(R.id.mySeekBar3);
        slider_labels[3] = findViewById(R.id.centerFreq3);
        sliders[4] = findViewById(R.id.mySeekBar4);
        slider_labels[4] = findViewById(R.id.centerFreq4);

        // optional audio effects controls; layout does not currently include them
        // variables remain null so related code is no-op
        bassSlider = null;
        virtualSlider = null;
        enableBass = null;
        enableVirtual = null;

        // no initialization since the views are absent




        eq = new Equalizer (0, 0);
        if (eq != null)
        {
            int num_bands = eq.getNumberOfBands();
            num_sliders = num_bands;
            short r[] = eq.getBandLevelRange();
            min_level = r[0];
            max_level = r[1];
            for (int i = 0; i < num_sliders && i < MAX_SLIDERS; i++)
            {
                int freq_range = eq.getCenterFreq((short)i);
                sliders[i].setOnSeekBarChangeListener(this);
                slider_labels[i].setText (milliHzToString(freq_range));
            }
        }

        bb = new BassBoost (0, 0);
        virtualizer = new Virtualizer (0, 0);


        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        if(!myPreferences.contains("initial")) {
            myEditor.putBoolean("initial", true);
            myEditor.putBoolean("eqswitch", true);
            myEditor.putBoolean("bbswitch", true);
            myEditor.putBoolean("virswitch", true);
            myEditor.putInt("bbslider", (int)bb.getRoundedStrength());
            myEditor.putInt("virslider", (int)virtualizer.getRoundedStrength());
            myEditor.putInt("slider0", 100 * eq.getBandLevel((short)0) / (max_level - min_level) + 50);
            myEditor.putInt("slider1", 100 * eq.getBandLevel((short)1) / (max_level - min_level) + 50);
            myEditor.putInt("slider2", 100 * eq.getBandLevel((short)2) / (max_level - min_level) + 50);
            myEditor.putInt("slider3", 100 * eq.getBandLevel((short)3) / (max_level - min_level) + 50);
            myEditor.putInt("slider4", 100 * eq.getBandLevel((short)4) / (max_level - min_level) + 50);
            myEditor.commit();
        }

        updateUI();



        if (virtualSlider != null) {
            virtualSlider.setOnProgressChangedListener(new ProgressListener() {
                @Override
                public void invoke(int j) {
                    if (virtualizer != null) {
                        virtualizer.setStrength((short)j);
                        saveChanges();
                    }
                }
            });
        }

        if (bassSlider != null) {
            bassSlider.setOnProgressChangedListener(new ProgressListener() {
                @Override
                public void invoke(int i) {
                    if (bb != null) {
                        Log.d("WOW", "level bass slider*************************** "+(short)i );
                        bb.setStrength((short)i);
                        Log.d("WOW", "set progress actual bass level *************************** "+bb.getRoundedStrength() );
                        saveChanges();
                    }
                }
            });
        }
        if (virtualizer != null && enableVirtual != null && virtualSlider != null)
        {
            enableVirtual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(enableVirtual.isChecked()){
                        virtualizer.setEnabled(true);
                        virtualSlider.setEnabled(true);
                        virtualSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                        saveChanges();
                    }
                    else{
                        virtualizer.setEnabled(false);
                        virtualSlider.setEnabled(false);
                        virtualSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
                        saveChanges();
                    }
                }
            });
        }
        if (bb != null && enableBass != null && bassSlider != null)
        {
            enableBass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(enableBass.isChecked()){
                        bb.setEnabled(true);
                        bassSlider.setEnabled(true);
                        bassSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                        saveChanges();
                    }
                    else{
                        bb.setEnabled(false);
                        bassSlider.setEnabled(false);
                        bassSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
                        saveChanges();
                    }
                }
            });
        }
        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(enabled.isChecked()){
                    eq.setEnabled(true);
                    saveChanges();
                    for(int i=0;i<5;i++){
                        sliders[i].setEnabled(true);
                    }
                }
                else {eq.setEnabled(false);
                        saveChanges();
                    for(int i=0;i<5;i++){
                        sliders[i].setEnabled(false);
                    }
                }
                if(enabled.isChecked()||enableBass.isChecked()||enableVirtual.isChecked()){

                }
                else{

                }



            }
        });
    }


    public String milliHzToString (int milliHz)
    {
        if (milliHz < 1000) return "";
        if (milliHz < 1000000)
            return "" + (milliHz / 1000) + "Hz";
        else
            return "" + (milliHz / 1000000) + "kHz";
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int level, boolean b) {

        if (eq != null)
        {
            int new_level=min_level+(max_level-min_level)*level / 100;

            for (int i=0;i<num_sliders;i++)
            {
                if (sliders[i]==seekBar)
                {
                    eq.setBandLevel((short)i,(short)new_level);
                    saveChanges();
                    break;
                }
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_support) {
            Intent intent = new Intent(MainActivity.this, SupportActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateUI (){
        applyChanges();
        boolean anyEnabled = enabled != null && enabled.isChecked();
        if (enableBass != null && enableBass.isChecked()) {
            anyEnabled = true;
        }
        if (enableVirtual != null && enableVirtual.isChecked()) {
            anyEnabled = true;
        }
        // anyEnabled used for potential UI changes later

        if (enableBass != null) {
            if(enableBass.isChecked()){
                if (bassSlider != null) {
                    bassSlider.setEnabled(true);
                    bassSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                }
                if (bb != null) bb.setEnabled(true);
            } else {
                if (bassSlider != null) {
                    bassSlider.setEnabled(false);
                    bassSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
                }
                if (bb != null) bb.setEnabled(false);
            }
        }
        if (enableVirtual != null) {
            if(enableVirtual.isChecked()){
                if (virtualizer != null) virtualizer.setEnabled(true);
                if (virtualSlider != null) {
                    virtualSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                    virtualSlider.setEnabled(true);
                }
            } else {
                if (virtualizer != null) virtualizer.setEnabled(false);
                if (virtualSlider != null) {
                    virtualSlider.setEnabled(false);
                    virtualSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
                }
            }
        }

        if (enabled != null && enabled.isChecked()){
            for(int i=0;i<5;i++)
                sliders[i].setEnabled(true);
            if (eq != null) eq.setEnabled(true);
        }
        else{
            for(int i=0;i<5;i++)
                sliders[i].setEnabled(false);
            if (eq != null) eq.setEnabled(false);
        }
        updateSliders();
        updateBassBoost();
        updateVirtualizer();
        updateLoudness();
        if (spinner != null) {
            spinner.setSelection(spinnerPos);
        }

    }

    public void updateSliders ()
    {
        for (int i = 0; i < num_sliders; i++)
        {
            int level;
            if (eq != null)
                level = eq.getBandLevel ((short)i);
            else
                level = 0;
            int pos = 100 * level / (max_level - min_level) + 50;
            sliders[i].setProgress (pos);
        }
    }

    public void updateBassBoost ()
    {
        if (bassSlider != null) {
            if (bb != null)
                bassSlider.setProgress (bb.getRoundedStrength());
            else
                bassSlider.setProgress(0);
        }
    }

    public void updateVirtualizer ()
    {
        if (virtualSlider != null) {
            if (virtualizer != null)
                virtualSlider.setProgress (virtualizer.getRoundedStrength());
            else
                virtualSlider.setProgress (0);
        }
    }

    public void updateLoudness() {
        if (loudSlider != null) {
            if (loudnessEnhancer != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    loudSlider.setProgress((int) loudnessEnhancer.getTargetGain());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                loudSlider.setProgress(0);
            }
        }
    }

    public void saveChanges() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putBoolean("initial", true);
        myEditor.putBoolean("eqswitch", enabled != null && enabled.isChecked());
        myEditor.putBoolean("bbswitch", enableBass != null && enableBass.isChecked());
        myEditor.putBoolean("virswitch", enableVirtual != null && enableVirtual.isChecked());
        myEditor.putBoolean("loudswitch", enableLoud != null && enableLoud.isChecked());
        myEditor.putInt("spinnerpos", spinnerPos);
        try {
            if (bb != null)
                myEditor.putInt("bbslider", (int) bb.getRoundedStrength());
            if (virtualizer != null)
                myEditor.putInt("virslider", (int) virtualizer.getRoundedStrength());
            if (loudnessEnhancer != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                myEditor.putFloat("loudslider", loudnessEnhancer.getTargetGain());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if ((spinnerPos == eqPreset.size() - 1) && !dontcall) {
            myEditor.putInt("slider0", 100 * eq.getBandLevel((short) 0) / (max_level - min_level) + 50);
            myEditor.putInt("slider1", 100 * eq.getBandLevel((short) 1) / (max_level - min_level) + 50);
            myEditor.putInt("slider2", 100 * eq.getBandLevel((short) 2) / (max_level - min_level) + 50);
            myEditor.putInt("slider3", 100 * eq.getBandLevel((short) 3) / (max_level - min_level) + 50);
            myEditor.putInt("slider4", 100 * eq.getBandLevel((short) 4) / (max_level - min_level) + 50);
        }
        myEditor.apply();
    }

    public void applyChanges() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        spinnerPos = myPreferences.getInt("spinnerpos", 0);
        if (enabled != null)
            enabled.setChecked(myPreferences.getBoolean("eqswitch", true));
        if (enableBass != null)
            enableBass.setChecked(myPreferences.getBoolean("bbswitch", true));
        if (enableVirtual != null)
            enableVirtual.setChecked(myPreferences.getBoolean("virswitch", true));
        if (enableLoud != null)
            enableLoud.setChecked(myPreferences.getBoolean("loudswitch", false));
        if (bb != null)
            bb.setStrength((short) myPreferences.getInt("bbslider", 0));
        if (virtualizer != null)
            virtualizer.setStrength((short) myPreferences.getInt("virslider", 0));
        if (loudnessEnhancer != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            loudnessEnhancer.setTargetGain((int) myPreferences.getFloat("loudslider", 0f));
    }

    public void disableEvery() {
        Toast.makeText(this, R.string.disableOther,
                Toast.LENGTH_LONG).show();
        if (spinner != null) spinner.setEnabled(false);
        if (enabled != null) enabled.setChecked(false);
        if (enableVirtual != null) enableVirtual.setChecked(false);
        if (enableBass != null) enableBass.setChecked(false);
        if (enableLoud != null) enableLoud.setChecked(false);
        canEnable = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && loudnessEnhancer != null)
            loudnessEnhancer.setEnabled(false);
        if (loudSlider != null) {
            loudSlider.setEnabled(false);
            loudSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
        }
        if (virtualizer != null) virtualizer.setEnabled(false);
        if (virtualSlider != null) {
            virtualSlider.setEnabled(false);
            virtualSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
        }
        if (bassSlider != null) {
            bassSlider.setEnabled(false);
            bassSlider.setProgressColor(ContextCompat.getColor(getBaseContext(), R.color.progress_gray));
        }
        if (bb != null) bb.setEnabled(false);
        for (int i = 0; i < 5; i++)
            sliders[i].setEnabled(false);
        if (eq != null) eq.setEnabled(false);
    }

    public void disablePreset() {
        if (presetView != null)
            presetView.setVisibility(View.GONE);
        canPreset = false;
    }

    public void initialize(){
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        if (!myPreferences.contains("initial")) {
            myEditor.putBoolean("initial", true);
            myEditor.putBoolean("eqswitch", false);
            myEditor.putBoolean("bbswitch", false);
            myEditor.putBoolean("virswitch", false);
            myEditor.putInt("bbslider", (int) bb.getRoundedStrength());
            myEditor.putBoolean("loudswitch", false);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && loudnessEnhancer!=null)
                myEditor.putFloat("loudslider",  loudnessEnhancer.getTargetGain());
            myEditor.putInt("virslider", (int) virtualizer.getRoundedStrength());
            myEditor.putInt("slider0", 100 * eq.getBandLevel((short) 0) / (max_level - min_level) + 50);
            myEditor.putInt("slider1", 100 * eq.getBandLevel((short) 1) / (max_level - min_level) + 50);
            myEditor.putInt("slider2", 100 * eq.getBandLevel((short) 2) / (max_level - min_level) + 50);
            myEditor.putInt("slider3", 100 * eq.getBandLevel((short) 3) / (max_level - min_level) + 50);
            myEditor.putInt("slider4", 100 * eq.getBandLevel((short) 4) / (max_level - min_level) + 50);
            myEditor.putInt("spinnerpos", 0);
            myEditor.apply();
        }
    }

    public void serviceChecker(){
        if ((enabled != null && enabled.isChecked()) ||
            (enableBass != null && enableBass.isChecked()) ||
            (enableVirtual != null && enableVirtual.isChecked()) ||
            (enableLoud != null && enableLoud.isChecked())) {
            Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        } else {
            Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);
        }
    }


}

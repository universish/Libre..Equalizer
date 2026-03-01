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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import androidx.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    Switch enabled = null;
    Switch enableBass, enableVirtual;

    Equalizer eq = null;
    BassBoost bb = null;
    Virtualizer virtualizer = null;

    int min_level = 0;
    int max_level = 100;

    static final int MAX_SLIDERS = 5; // Must match the XML layout
    SeekBar sliders[] = new SeekBar[MAX_SLIDERS];
    ArcSeekBar bassSlider,virtualSlider;
    TextView slider_labels[] = new TextView[MAX_SLIDERS];
    int num_sliders = 0;


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

    public void saveChanges(){
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putBoolean("initial", true);
        myEditor.putBoolean("eqswitch", enabled.isChecked());
        myEditor.putBoolean("bbswitch", enableBass.isChecked());
        myEditor.putBoolean("virswitch", enableVirtual.isChecked());
        Log.d("WOW", "actual bass level *************************** "+bb.getRoundedStrength());
        Log.d("WOW", "actual vir level *************************** "+ virtualizer.getRoundedStrength() );

        myEditor.putInt("bbslider", (int)bb.getRoundedStrength());
        myEditor.putInt("virslider", (int)virtualizer.getRoundedStrength());
        myEditor.putInt("slider0", 100 * eq.getBandLevel((short)0) / (max_level - min_level) + 50);
        myEditor.putInt("slider1", 100 * eq.getBandLevel((short)1) / (max_level - min_level) + 50);
        myEditor.putInt("slider2", 100 * eq.getBandLevel((short)2) / (max_level - min_level) + 50);
        myEditor.putInt("slider3", 100 * eq.getBandLevel((short)3) / (max_level - min_level) + 50);
        myEditor.putInt("slider4", 100 * eq.getBandLevel((short)4) / (max_level - min_level) + 50);
        myEditor.commit();
    }
    public void applyChanges(){
        SharedPreferences myPreferences
            = PreferenceManager.getDefaultSharedPreferences(this);
        enabled.setChecked(myPreferences.getBoolean("eqswitch",true));
        enableBass.setChecked(myPreferences.getBoolean("bbswitch",true));
        enableVirtual.setChecked(myPreferences.getBoolean("virswitch",true));
        eq.setBandLevel((short)0,(short)(min_level+(max_level-min_level)*myPreferences.getInt("slider0",0) / 100));
        eq.setBandLevel((short)1,(short)(min_level+(max_level-min_level)*myPreferences.getInt("slider1",0) / 100));
        eq.setBandLevel((short)2,(short)(min_level+(max_level-min_level)*myPreferences.getInt("slider2",0) / 100));
        eq.setBandLevel((short)3,(short)(min_level+(max_level-min_level)*myPreferences.getInt("slider3",0) / 100));
        eq.setBandLevel((short)4,(short)(min_level+(max_level-min_level)*myPreferences.getInt("slider4",0) / 100));
        bb.setStrength((short)myPreferences.getInt("bbslider",0));
        virtualizer.setStrength((short)myPreferences.getInt("virslider",0));
        Log.d("WOW", "bass level *************************** "+(short)myPreferences.getInt("bbslider",0) );
        Log.d("WOW", "virtualizer level *************************** "+(short)myPreferences.getInt("virslider",0) );
    }


}

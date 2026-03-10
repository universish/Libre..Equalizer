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
import android.content.Intent;
import android.widget.Toast;
import android.view.MenuItem;
import android.view.Menu;
import android.graphics.Color;
import android.widget.Button;
import android.app.Service;
import android.content.SharedPreferences;
import android.app.Application;
import androidx.lifecycle.ViewModel;
import android.media.audiofx.BassBoost;
import android.content.Context;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import com.libre_universish.equalizer.R;
import android.media.audiofx.AudioEffect;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, Switch.OnCheckedChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Listener that receives only progress updates.  Used by the bass slider
     * to prevent redundant writes to the audio effect (which caused audible
     * clicks when the same value was repeatedly set).
     */
    private interface ProgressListener {
        void valueChanged(int progress);
    }

    /**
     * Utility method to bind a ProgressListener to an arbitrary SeekBar.
     * The named method mirrors the example snippet the user provided.
     */
    private void setOnProgressChangedListener(SeekBar seekBar, ProgressListener listener) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                listener.valueChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) { }

            @Override
            public void onStopTrackingTouch(SeekBar sb) { }
        });
    }

    EqualizerViewModel equalizerViewModel;
    static final int MAX_SLIDERS = 5; // Must match the XML layout
    private static final String TAG = "MainActivity";
    private static final int USER_SLOT_COUNT = 6;            // number of fixed user‑editable presets
    private static final String USER_SLOT_PREFIX = "User ";
    Equalizer equalizer = null;
    BassBoost bassBoost = null;
    Virtualizer virtualizer = null;
    LoudnessEnhancer loudnessEnhancer = null;
    Switch enableEq = null;
    Switch enableBass, enableVirtual, enableLoud;
    Spinner spinner;
    int minLevel = 0;
    int maxLevel = 100;
    SeekBar sliders[] = new SeekBar[MAX_SLIDERS];
    SeekBar bassSlider, virtualSlider, loudSlider, systemVolSeekBar;
    TextView sliderLabels[] = new TextView[MAX_SLIDERS];
    TextView loudSliderText, systemVolTextView;
    int numSliders = 0;
    ArrayList<String> eqPreset;
    int spinnerPos = 0;
    boolean canPreset;
    // tracking for preset index offsets
    private int builtInCount = 0;
    private int customCount = 0;
    private int userSlotStart = 0;
    private int customIndex = -1; // position of the automatic "Custom" entry

    // automatic preset helpers ------------------------------------------------
    private void loadAutoCustom() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        for (int j = 0; j < 5; j++) {
            int val = prefs.getInt("auto_slider" + j, minLevel);
            equalizer.setBandLevel((short) j, (short) val);
            sliders[j].setProgress((val - minLevel) * 100 / (maxLevel - minLevel));
            equalizerViewModel.setSlider(val, j);
        }
        enableEq.setChecked(prefs.getBoolean("auto_eq", true));
        enableBass.setChecked(prefs.getBoolean("auto_bass", true));
        enableVirtual.setChecked(prefs.getBoolean("auto_vir", true));
        enableLoud.setChecked(prefs.getBoolean("auto_loud", true));
        equalizerViewModel.setEqSwitch(enableEq.isChecked());
        equalizerViewModel.setbBSwitch(enableBass.isChecked());
        equalizerViewModel.setVirSwitch(enableVirtual.isChecked());
        equalizerViewModel.setLoudSwitch(enableLoud.isChecked());
    }

    private void saveAutoCustom() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        for (int j = 0; j < 5; j++) {
            edit.putInt("auto_slider" + j, equalizerViewModel.getSlider(j));
        }
        edit.putBoolean("auto_eq", enableEq.isChecked());
        edit.putBoolean("auto_bass", enableBass.isChecked());
        edit.putBoolean("auto_vir", enableVirtual.isChecked());
        edit.putBoolean("auto_loud", enableLoud.isChecked());
        edit.apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // apply previously chosen language
        LocaleHelpers.applySavedLocale(this);
        equalizerViewModel = new ViewModelProvider(this).get(EqualizerViewModel.class);

        if (equalizerViewModel.getDarkTheme()) {
            setTheme(R.style.AppTheme_Dark);
        } else setTheme(R.style.AppTheme);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// //         // // RateThisApp.onCreate(this);
// //         // // RateThisApp.showRateDialogIfNeeded(this);

        loudSliderText = findViewById(R.id.volTextView);
        enableEq = findViewById(R.id.switchEnable);
        enableEq.setChecked(true);
        spinner = findViewById(R.id.spinner);
        sliders[0] = findViewById(R.id.mySeekBar0);
        sliderLabels[0] = findViewById(R.id.centerFreq0);
        sliders[1] = findViewById(R.id.mySeekBar1);
        sliderLabels[1] = findViewById(R.id.centerFreq1);
        sliders[2] = findViewById(R.id.mySeekBar2);
        sliderLabels[2] = findViewById(R.id.centerFreq2);
        sliders[3] = findViewById(R.id.mySeekBar3);
        sliderLabels[3] = findViewById(R.id.centerFreq3);
        sliders[4] = findViewById(R.id.mySeekBar4);
        sliderLabels[4] = findViewById(R.id.centerFreq4);
        // use 0–100 UI range for band sliders; actual level is calculated
        // from minLevel/maxLevel read from the hardware device.
        for (SeekBar s : sliders) {
            s.setMax(100);
        }
        bassSlider = findViewById(R.id.bassSeekBar);
        virtualSlider = findViewById(R.id.virtualSeekBar);
        enableBass = findViewById(R.id.bassSwitch);
        enableVirtual = findViewById(R.id.virtualSwitch);
        enableBass = findViewById(R.id.bassSwitch);
        enableVirtual = findViewById(R.id.virtualSwitch);
        enableLoud = findViewById(R.id.volSwitch);
        loudSlider = findViewById(R.id.volSeekBar);

        // system volume controls
        systemVolSeekBar = findViewById(R.id.systemVolSeekBar);
        systemVolTextView = findViewById(R.id.systemVolTextView);
        // configure new SeekBars
        bassSlider.setMax(1000);
        virtualSlider.setMax(1000);
        // Android docs and audio experts recommend a 0–1000 millibel range for
        // LoudnessEnhancer target gain.  Using 10 000 was arbitrary and caused
        // excessive precision; reduce to 1000 for sane hardware/software
        // compatibility and to match the actual effect range.
        loudSlider.setMax(1000);
        enableLoud.setChecked(true);
        enableBass.setChecked(true);
        enableVirtual.setChecked(true);

        // setup system-volume buttons beside effect sliders
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        View.OnClickListener volCtl = v -> {
            int step = 1; // 1% increment
            if (v.getId() == R.id.volUpBass && bassSlider != null) {
                bassSlider.setProgress(Math.min(bassSlider.getMax(), bassSlider.getProgress() + step));
            } else if (v.getId() == R.id.volDownBass && bassSlider != null) {
                bassSlider.setProgress(Math.max(0, bassSlider.getProgress() - step));
            } else if (v.getId() == R.id.volUpLoud && loudSlider != null) {
                loudSlider.setProgress(Math.min(loudSlider.getMax(), loudSlider.getProgress() + step));
            } else if (v.getId() == R.id.volDownLoud && loudSlider != null) {
                loudSlider.setProgress(Math.max(0, loudSlider.getProgress() - step));
            } else if (v.getId() == R.id.volUpVirtual && virtualSlider != null) {
                virtualSlider.setProgress(Math.min(virtualSlider.getMax(), virtualSlider.getProgress() + step));
            } else if (v.getId() == R.id.volDownVirtual && virtualSlider != null) {
                virtualSlider.setProgress(Math.max(0, virtualSlider.getProgress() - step));
            }
        };
        Button b;
        b = findViewById(R.id.volUpBass); if (b!=null) b.setOnClickListener(volCtl);
        b = findViewById(R.id.volDownBass); if (b!=null) b.setOnClickListener(volCtl);
        b = findViewById(R.id.volUpLoud); if (b!=null) b.setOnClickListener(volCtl);
        b = findViewById(R.id.volDownLoud); if (b!=null) b.setOnClickListener(volCtl);
        b = findViewById(R.id.volUpVirtual); if (b!=null) b.setOnClickListener(volCtl);
        b = findViewById(R.id.volDownVirtual); if (b!=null) b.setOnClickListener(volCtl);

        // system volume controls
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        systemVolSeekBar.setMax(maxVolume);
        systemVolSeekBar.setProgress(currentVolume);

        View.OnClickListener systemVolCtl = v -> {
            if (audioManager == null) return;
            int direction = (v.getId() == R.id.systemVolUp) ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_SHOW_UI);
            int newVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            systemVolSeekBar.setProgress(newVolume);
        };
        Button systemVolUp = findViewById(R.id.systemVolUp);
        Button systemVolDown = findViewById(R.id.systemVolDown);
        if (systemVolUp != null) systemVolUp.setOnClickListener(systemVolCtl);
        if (systemVolDown != null) systemVolDown.setOnClickListener(systemVolCtl);

        systemVolSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        eqPreset = new ArrayList<>();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eqPreset);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // color customization removed; using default SeekBar styling

        equalizer = equalizerViewModel.getEqualizer();
        bassBoost = equalizerViewModel.getBassBoost();
        virtualizer = equalizerViewModel.getVirtualizer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            loudnessEnhancer = equalizerViewModel.getLoudnessEnhancer();
        else {
            enableLoud.setChecked(false);
            loudSlider.setVisibility(View.GONE);
            enableLoud.setVisibility(View.GONE);
            loudSliderText.setVisibility(View.GONE);
        }
        final int[] builtInCountHolder = new int[1];
        try {
            numSliders = equalizer.getNumberOfBands();
            short r[] = equalizer.getBandLevelRange();
            minLevel = r[0];
            maxLevel = r[1];

            for (int i = 0; i < numSliders && i < MAX_SLIDERS; i++) {
                int freq_range = equalizer.getCenterFreq((short) i);
                sliders[i].setOnSeekBarChangeListener(this);
                sliderLabels[i].setText(milliHzToString(freq_range));
            }
            short noOfPresets = equalizer.getNumberOfPresets();
            for (short i = 0; i < noOfPresets; i++) {
                eqPreset.add(equalizer.getPresetName(i));
            }
            // remember how many built‑in presets we got
            builtInCountHolder[0] = eqPreset.size();
            // load localized list of fallback presets from resources
            String[] customNames = getResources().getStringArray(R.array.custom_preset_names);
            customPresetPcts = new ArrayList<>();
            for (int k = 0; k < customNames.length && k < RAW_PCTS.length; k++) {
                String name = customNames[k];
                if (!eqPreset.contains(name)) {
                    eqPreset.add(name);
                    customPresetPcts.add(RAW_PCTS[k]);
                }
            }
            // remember where user slots will begin (after any duplicates removed)
            userSlotStart = eqPreset.size();
            // insert six fixed user slots between custom names and the manual entry
            for (int u = 1; u <= USER_SLOT_COUNT; u++) {
                String name = USER_SLOT_PREFIX + u;
                if (!eqPreset.contains(name)) {
                    eqPreset.add(name);
                }
            }
            // the last entry remains the editable "Custom" slot
            if (!eqPreset.contains("Custom")) {
                eqPreset.add("Custom");
            }
            // record offsets for later use
            builtInCount = builtInCountHolder[0];
            customIndex = eqPreset.size() - 1;
            spinner.setAdapter(spinnerAdapter);
        }catch(Exception e){
            equalizer=EffectInstance.getEqualizerInstance();
            minLevel = 0;
            maxLevel = 0;
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total = eqPreset.size();
                int customStart = builtInCount;
                int userStart = userSlotStart;
                if (i < customStart) {
                    // built‑in
                    try {
                        equalizer.usePreset((short) i);
                        equalizerViewModel.setSpinnerPos(i);
                        equalizerViewModel.setIsCustomSelected(false);
                        for (int j = 0; j < 5; j++) {
                            int level = (equalizer.getBandLevel((short) j) - minLevel) * 100 / (maxLevel - minLevel);
                            sliders[j].setProgress(level);
                        }
                    } catch (Throwable e) {
                        disablePreset();
                        spinner.setVisibility(View.GONE);
                    }
                } else if (i < userStart) {
                    // custom fallback preset
                    applyCustomPreset(i - customStart);
                    equalizerViewModel.setSpinnerPos(i);
                    equalizerViewModel.setIsCustomSelected(false);
                } else if (i < userStart + USER_SLOT_COUNT) {
                    // user‑defined slot
                    loadUserSlot(i - userStart);
                    equalizerViewModel.setSpinnerPos(i);
                    equalizerViewModel.setIsCustomSelected(true); // editable
                } else {
                    // custom manual
                    equalizerViewModel.setIsCustomSelected(true);
                    equalizerViewModel.setSpinnerPos(i);
                    // load saved automatic values when entering custom slot
                    loadAutoCustom();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // replace arc listener with SeekBar listener registered on this activity
        virtualSlider.setOnSeekBarChangeListener(this);
        // bass slider gets its own progress-only listener so that we can
        // avoid repeated writes of the same value (tık‑tık click noise).
        setOnProgressChangedListener(bassSlider, new ProgressListener() {
            @Override
            public void valueChanged(int progress) {
                try {
                    short current = bassBoost.getRoundedStrength();
                    if (current != (short) progress) {
                        equalizerViewModel.setBBSlider(progress);
                        bassBoost.setStrength((short) progress);
                    }
                } catch (Throwable e) {
                    Log.d(TAG, "invoke: bassSlider Error");
                    e.printStackTrace();
                }
                if (spinner.getSelectedItemPosition() == customIndex) {
                    saveAutoCustom();
                }
            }
        });
        // attach a dedicated progress listener to handle loudness safely
        setOnProgressChangedListener(loudSlider, new ProgressListener() {
            @Override
            public void valueChanged(int progress) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        if (loudnessEnhancer.getTargetGain() != progress)
                            equalizerViewModel.setLoudSlider(progress);
                        Log.d(TAG, "invoke: slider value " + loudnessEnhancer.getTargetGain());
                        loudnessEnhancer.setTargetGain(progress);
                    } catch (Throwable e) {
                        Log.d(TAG, "invoke: loudSlider Error");
                        e.printStackTrace();
                    }
                }
            }
        });


        enableVirtual.setOnCheckedChangeListener(this);
        enableBass.setOnCheckedChangeListener(this);
        enableLoud.setOnCheckedChangeListener(this);
        enableEq.setOnCheckedChangeListener(this);
        setupPresetInterface();

        // make sure background service state matches the switch settings
        serviceChecker();

        // observe preset clicks to refresh spinner list
        equalizerViewModel.getIsPresetClicked().observe(this, new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == Boolean.TRUE) {
                    Log.d(TAG, "onChanged: clicked");
                    setupPresetInterface();
                    equalizerViewModel.setIsPresetClicked(false);
                }
            }
        });
    }

    void setupPresetInterface(){
        spinner.setSelection(equalizerViewModel.getSpinnerPos());
        enableEq.setChecked(equalizerViewModel.getEqSwitch());
        enableBass.setChecked(equalizerViewModel.getbBSwitch());
        enableLoud.setChecked(equalizerViewModel.getLoudSwitch());
        enableVirtual.setChecked(equalizerViewModel.getVirSwitch());
        bassSlider.setProgress(equalizerViewModel.getBBSlider());
        virtualSlider.setProgress(equalizerViewModel.getVirSlider());
        loudSlider.setProgress((int) equalizerViewModel.getLoudSlider());
        for (int i = 0; i < 5; i++) {
            int level = (equalizerViewModel.getSlider(i) - minLevel) * 100 / (maxLevel - minLevel);
            sliders[i].setProgress(level);
        }
    }


    // dynamic container holding percentage arrays for each fallback name actually
    // inserted into eqPreset.  This replaces the old fixed 2‑D array to ensure
    // indexes stay aligned when duplicates are skipped.
    private List<int[]> customPresetPcts;

    // raw percentages for each entry in the resource array.  order must match
    // the <string-array name="custom_preset_names"/> defined in values/arrays.xml
    private static final int[][] RAW_PCTS = {
        {20, 10, -10, 5, 15},     // Rock (V-shape)
        {15, 5, 0, 10, 10},       // Pop
        {12, 5, 5, 10, 10},       // Jazz
        {15, 10, 0, 5, 10},       // Classical
        {30, 15, -5, 5, 20},      // HipHop
        {40, 20, 0, 0, 0},        // Bass Boost
        {0, 0, 0, 20, 40},        // Treble Boost
        {10, 5, 5, 10, 15},       // Acoustic
        {25, 10, -5, 15, 20},     // Electronic
        {0, 0, 0, 0, 0},          // Flat
        {-10, 5, 20, 10, -5},     // Vocal (slight mid boost)
        {25, 15, -20, 15, 25},    // Metal (Scooped mids)
        {15, 10, 0, 10, 15},      // Dance (neutral)
        {15, 5, -5, 10, 20},      // Reggae
        {10, 5, 5, 5, 10},        // Country
        {10, 10, 5, 5, 10},       // Blues
        {5, 5, 10, 5, 5},         // Folk
        {15, 10, 0, 10, 15},      // Latin
        {25, 10, 0, 10, 15},      // R&B
        {10, 5, 5, 5, 10},        // Ambient
        {15, 10, -5, 10, 15},     // Funk
        {20, 5, 5, 5, 20},        // Party
        {10, 5, 0, 5, 10},        // Soft
        {25, 15, -10, 10, 20},    // Hard Rock
        {15, 10, -5, 10, 15},     // Punk
        {45, 25, -10, 10, 30},    // Dubstep
        {30, 15, 0, 15, 25},      // EDM
        {10, 5, 5, 10, 10},       // Indie
        {-15, 0, 25, 15, -10},    // Vocal Boost
        {15, 10, 15, 10, 5},      // Turkish Folk Song (Türkü)
        {20, 10, -5, 10, 20},     // Turkish Rock
        {20, 15, 10, 15, 20},     // Anatolian Rock
        {15, 10, 5, 10, 15},      // Turkish Pop
        {10, 10, 10, 10, 10},     // Turkish Slow Pop
        {15, 10, 5, 10, 10},      // Turkish Slow Rock
        {30, 20, -15, 15, 30},    // Turkish Metal
        {15, 15, 20, 15, 10},     // Turkish Emotional Music
        {10, 15, 15, 10, 5},      // Turkish Folk
        {35, 15, 10, 15, 25},     // Arabesque
        {35, 15, -5, 10, 20},     // Turkish Rap
        {15, 10, 5, 15, 20},      // Greek Taverna
        {10, 10, 15, 20, 15},     // Greek Rebetiko
        {15, 10, 5, 10, 15},      // Greek Pop
        {20, 10, 0, 10, 20},      // Greek Rock
        {15, 10, 15, 15, 15},     // Greek Folk
        {20, 10, 10, 20, 15},     // Laiko
        {5, 5, 15, 30, 20},       // Bouzouki (Solo focus)
        {10, 10, 10, 10, 10},     // Greek Slow Pop
        {30, 20, -10, 15, 30},    // Greek Metal
    };
    private void applyCustomPreset(int index) {
        if (customPresetPcts == null || index < 0 || index >= customPresetPcts.size()) return;
        int[] p = customPresetPcts.get(index);
        for (int j = 0; j < 5; j++) {
            int pct = p[j];
            int lvl = minLevel + (maxLevel - minLevel) * (50 + pct) / 100; // 50 = neutral
            equalizer.setBandLevel((short) j, (short) lvl);
            sliders[j].setProgress((lvl - minLevel) * 100 / (maxLevel - minLevel));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == enableEq) {
            try {
                spinner.setEnabled(isChecked);
                equalizer.setEnabled(isChecked);
                for (int i = 0; i < 5; i++) {
                    sliders[i].setEnabled(isChecked);
                }
                equalizerViewModel.setEqSwitch(isChecked);
            } catch (Exception e) {
                // effect might be null or released, reacquire
                equalizer = EffectInstance.getEqualizerInstance();
            }
            if (spinner.getSelectedItemPosition() == customIndex) {
                saveAutoCustom();
            }

        } else if (buttonView == enableBass) {
            try {
                bassBoost.setEnabled(isChecked);
                bassSlider.setEnabled(isChecked);
                equalizerViewModel.setbBSwitch(isChecked);
                // color the slider progress to give visual feedback
                int color = isChecked ? ContextCompat.getColor(getBaseContext(), R.color.colorAccent)
                                      : ContextCompat.getColor(getBaseContext(), android.R.color.darker_gray);
                bassSlider.setProgressTintList(android.content.res.ColorStateList.valueOf(color));
            } catch (Exception e) {
                bassBoost = EffectInstance.getBassBoostInstance();
            }
            // ignore other color changes for SeekBar
            if (spinner.getSelectedItemPosition() == customIndex) {
                saveAutoCustom();
            }

        } else if (buttonView == enableLoud) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    loudnessEnhancer.setEnabled(isChecked);
                loudSlider.setEnabled(isChecked);
                equalizerViewModel.setLoudSwitch(isChecked);
                int color = isChecked ? ContextCompat.getColor(getBaseContext(), R.color.colorAccent)
                                      : ContextCompat.getColor(getBaseContext(), android.R.color.darker_gray);
                loudSlider.setProgressTintList(android.content.res.ColorStateList.valueOf(color));
            } catch (Exception e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    loudnessEnhancer = EffectInstance.getLoudnessEnhancerInstance();
            }
            // ignore color changes for SeekBar
            if (isChecked) {
                Toast.makeText(getApplicationContext(), R.string.warning,
                        Toast.LENGTH_SHORT).show();
            }
            if (spinner.getSelectedItemPosition() == customIndex) {
                saveAutoCustom();
            }

        } else if (buttonView == enableVirtual) {
            try {
                virtualizer.setEnabled(isChecked);
                virtualSlider.setEnabled(isChecked);
                int color = isChecked ? ContextCompat.getColor(getBaseContext(), R.color.colorAccent)
                                      : ContextCompat.getColor(getBaseContext(), android.R.color.darker_gray);
                virtualSlider.setProgressTintList(android.content.res.ColorStateList.valueOf(color));
            } catch (Exception e) {
                virtualizer = EffectInstance.getVirtualizerInstance();
            }
            equalizerViewModel.setVirSwitch(isChecked);
            // ignore other color changes for SeekBar
            if (spinner.getSelectedItemPosition() == customIndex) {
                saveAutoCustom();
            }
        }
        serviceChecker();
    }


    public String milliHzToString(int milliHz) {
        if (milliHz < 1000) return "";
        if (milliHz < 1000000)
            return "" + (milliHz / 1000) + "Hz";
        else
            return "" + (milliHz / 1000000) + "kHz";
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int level, boolean b) {
        // virtualizer and loudness sliders (bass is handled by its own listener)
        if (seekBar == virtualSlider) {
            try {
                short current = virtualizer.getRoundedStrength();
                if (current != (short) level) {
                    equalizerViewModel.setVirSlider(level);
                    virtualizer.setStrength((short) level);
                }
            } catch (Throwable e) {
                Log.d(TAG, "onProgressChanged: virtualizer Error strength=" + virtualizer.getRoundedStrength() + " lvl=" + level);
                e.printStackTrace();
            }
            // if on custom slot, save the change
            if (spinner.getSelectedItemPosition() == customIndex) {
                saveAutoCustom();
            }
            return;
        }

        // frequency band sliders
        for (int i = 0; i < 5; i++) {
            if (sliders[i] == seekBar) {
                try {
                    int newLevel = minLevel + (maxLevel - minLevel) * level / 100;
                    // only write if the band value actually changes; repeated
                    // calls with identical input were generating audible clicks
                    short current = equalizer.getBandLevel((short) i);
                    if (current != (short) newLevel) {
                        equalizer.setBandLevel((short) i, (short) newLevel);
                    }
                    if (equalizerViewModel.getIsCustomSelected())
                        equalizerViewModel.setSlider(newLevel, i);
                    // auto-save if we're in custom slot
                    if (spinner.getSelectedItemPosition() == customIndex) {
                        saveAutoCustom();
                    }
                    break;
                } catch (Exception e) {
                    short cur = equalizer.getBandLevel((short) i);
                    Log.d(TAG, "onProgressChanged: Equalizer Error band="+i+" lvl="+level+" cur="+cur);
                    // effect may have been released by the system; reacquire
                    equalizer = EffectInstance.getEqualizerInstance();
                    minLevel = 0;
                    maxLevel = 0;
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("dark_theme")) {
            if (sharedPreferences.getBoolean("dark_theme", true)) {
                setTheme(R.style.AppTheme_Dark);
                equalizerViewModel.setDarkTheme(true);
                MainActivity.this.recreate();
            } else {
                setTheme(R.style.AppTheme);
                equalizerViewModel.setDarkTheme(false);
                MainActivity.this.recreate();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        int pos = spinner.getSelectedItemPosition();
        if (pos >= userSlotStart && pos < userSlotStart + USER_SLOT_COUNT) {
            // editing a user slot; keep selection and just update viewmodel
            for (int i = 0; i < 5; i++) {
                int newLevel = minLevel + (maxLevel - minLevel) * sliders[i].getProgress() / 100;
                equalizerViewModel.setSlider(newLevel, i);
            }
            return;
        }
        if (!equalizerViewModel.getIsCustomSelected()) {
            for (int i = 0; i < 5; i++) {
                int newLevel = minLevel + (maxLevel - minLevel) * sliders[i].getProgress() / 100;
                equalizerViewModel.setSlider(newLevel, i);
            }
            spinner.setSelection(eqPreset.size() - 1);
            spinnerPos = eqPreset.size() - 1;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // ads removal menu item removed; nothing to hide
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        }
        else if (id == R.id.action_about) {
            Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        }
        else if (id == R.id.action_load_preset) {
            showCustomPresetDialog();
            return true;
        }
        else if (id == R.id.action_save_preset) {
            int pos = spinner.getSelectedItemPosition();
            if (pos >= userSlotStart && pos < userSlotStart + USER_SLOT_COUNT) {
                saveCurrentToUserSlot(pos - userSlotStart);
            } else {
                showCustomSavePresetDialog();
            }
            return true;
        }
        else if (id == R.id.action_donate) {
            Intent myIntent = new Intent(MainActivity.this, SupportActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        }
        else if (id == R.id.action_audio_effect) {
            Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No audio effect panel available", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else if (id == R.id.action_diagnostics) {
            // launch the debug/reporting activity
            Intent diag = new Intent(MainActivity.this, DebugActivity.class);
            startActivity(diag);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showCustomPresetDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CustomPresetDialog alertDialog = new CustomPresetDialog();
        alertDialog.show(fm, "fragment_alert");
    }

    private void showCustomSavePresetDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CustomPresetSaveDialog alertDialog = new CustomPresetSaveDialog();

        alertDialog.show(fm, "fragment_alert");
    }

    // persistence for the fixed user slots.  Values are stored in shared
    // preferences rather than the freeform database used by the dialog.
    private void loadUserSlot(int slot) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int[] saved = new int[5];
        for (int j = 0; j < 5; j++) {
            saved[j] = prefs.getInt("user" + slot + "_slider" + j, minLevel);
        }
        for (int j = 0; j < 5; j++) {
            equalizer.setBandLevel((short) j, (short) saved[j]);
            sliders[j].setProgress((saved[j] - minLevel) * 100 / (maxLevel - minLevel));
            equalizerViewModel.setSlider(saved[j], j);
        }
        enableEq.setChecked(prefs.getBoolean("user"+slot+"_eq", true));
        enableBass.setChecked(prefs.getBoolean("user"+slot+"_bass", true));
        enableVirtual.setChecked(prefs.getBoolean("user"+slot+"_vir", true));
        enableLoud.setChecked(prefs.getBoolean("user"+slot+"_loud", true));
        equalizerViewModel.setEqSwitch(enableEq.isChecked());
        equalizerViewModel.setbBSwitch(enableBass.isChecked());
        equalizerViewModel.setVirSwitch(enableVirtual.isChecked());
        equalizerViewModel.setLoudSwitch(enableLoud.isChecked());
        equalizerViewModel.setSpinnerPos(userSlotStart + slot);
    }

    private void saveCurrentToUserSlot(int slot) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        for (int j = 0; j < 5; j++) {
            edit.putInt("user" + slot + "_slider" + j, equalizerViewModel.getSlider(j));
        }
        edit.putBoolean("user"+slot+"_eq", enableEq.isChecked());
        edit.putBoolean("user"+slot+"_bass", enableBass.isChecked());
        edit.putBoolean("user"+slot+"_vir", enableVirtual.isChecked());
        edit.putBoolean("user"+slot+"_loud", enableLoud.isChecked());
        edit.apply();
        Toast.makeText(this, R.string.preset_saved_successfully, Toast.LENGTH_SHORT).show();
    }

    public void disablePreset() {
        // don't hide the spinner; keep it visible so the UI doesn't shift and
        // we avoid "empty preset" errors when the user interacts later.
        // just move the selection to the custom entry and remember we cannot
        // use the current preset any more.
        if (eqPreset != null && !eqPreset.isEmpty()) {
            spinner.setSelection(eqPreset.size() - 1);
        }
        canPreset = false;
    }

    public void serviceChecker() {
        if (enableEq.isChecked() || enableBass.isChecked() || enableVirtual.isChecked() || enableLoud.isChecked()) {
            Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        } else {
            Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}

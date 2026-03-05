
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import com.libre_universish.equalizer.R;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;
import android.net.Uri;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.Context;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.preference.PreferenceFragment;
import android.arch.lifecycle.ViewModel;
import android.media.audiofx.Equalizer;
import java.util.List;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;


import android.util.Log;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "SettingsFragment";
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref);
    }
//    EqualizerViewModel equalizerViewModel;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        equalizerViewModel = ViewModelProviders.of(getActivity()).get(EqualizerViewModel.class);
//        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("dark_theme");
//        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Log.d(TAG, "onPreferenceChange: CALLED");
//
//                return true;
//            }
//        });
//    }

}

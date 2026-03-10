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
import androidx.preference.PreferenceFragmentCompat;
import android.widget.Toast;
import android.net.Uri;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;
import android.view.MenuItem;
import androidx.lifecycle.ViewModel;
import android.media.audiofx.Equalizer;
import java.util.List;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.util.Log;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "SettingsFragment";
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref);
        // react to language preference changes
        androidx.preference.ListPreference langPref =
                (androidx.preference.ListPreference) getPreferenceManager()
                        .findPreference("app_lang");
        if (langPref != null) {
            langPref.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {
                    String lang = (String) newValue;
                    LocaleHelpers.setLocale(getContext(), lang);
                    // recreate entire activity stack
                    if (getActivity() != null) {
                        getActivity().recreate();
                    }
                    return true;
                }
            });
        }
        androidx.preference.Preference upd = getPreferenceManager().findPreference("check_updates");
        if (upd != null) {
            upd.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://codeberg.org/universish/Libre..Equalizer/tags"));
                    startActivity(i);
                    return true;
                }
            });
        }
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

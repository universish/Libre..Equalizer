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

import com.libre_universish.equalizer.LocaleHelpers;
import androidx.preference.PreferenceManager;
import android.view.ViewGroup;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // apply language before theme
        LocaleHelpers.applySavedLocale(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("dark_theme",true)){
            setTheme(R.style.AppTheme_Dark);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }
}

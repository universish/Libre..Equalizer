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

import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import androidx.preference.PreferenceManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {
    View two, five, ten, twenty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("dark_theme", true)) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_support);

        two = findViewById(R.id.two_card);
        five = findViewById(R.id.five_card);
        ten = findViewById(R.id.ten_card);
        twenty = findViewById(R.id.twenty_card);

        Toolbar toolbar = findViewById(R.id.toolbar_donation);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // hide donation cards until/if configured
        two.setVisibility(View.GONE);
        five.setVisibility(View.GONE);
        ten.setVisibility(View.GONE);
        twenty.setVisibility(View.GONE);

        two.setOnClickListener(this);
        five.setOnClickListener(this);
        ten.setOnClickListener(this);
        twenty.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // could open a web link or show a dialog
        Toast.makeText(this, "Support feature not available", Toast.LENGTH_SHORT).show();
    }
}

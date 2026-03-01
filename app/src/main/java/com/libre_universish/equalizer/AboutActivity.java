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

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // apply dark theme same as other screens
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("dark_theme", true)){
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_about);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // wire up buttons
        findViewById(R.id.rate_on_google_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayStore();
            }
        });
        findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutActivity.this, SupportActivity.class));
            }
        });
        findViewById(R.id.fork_on_github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://codeberg.org/libre_universish/Libre-Equalizer");
            }
        });
        findViewById(R.id.report_bugs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:"));
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jazib27@hotmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Bug report: Libre Equalizer");
                if (email.resolveActivity(getPackageManager()) != null) {
                    startActivity(email);
                }
            }
        });
        findViewById(R.id.licenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LicensesDialogFragment.newInstance().show(getSupportFragmentManager(), "licenses");
            }
        });
        findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new androidx.appcompat.app.AlertDialog.Builder(AboutActivity.this)
                        .setTitle(R.string.privacy_policy)
                        .setMessage(R.string.privacy_text)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void openPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button, to taken back to our application,
        // we recommend to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            openUrl("https://play.google.com/store/apps/details?id=" + getPackageName());
        }
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

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

/*
 * SupportActivity simplified after removing all billing/closed-source code.
 * This screen now merely displays informational content; purchase features were
 * removed to keep the project fully free/libre.
 */
package com.libre_universish.equalizer;

import android.os.Bundle;
import com.libre_universish.equalizer.LocaleHelpers;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class SupportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // apply chosen language first
        LocaleHelpers.applySavedLocale(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("dark_theme", true)) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Toolbar toolbar = findViewById(R.id.toolbar_donation);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // no billing logic, just show static content
        // attach dynamic donation links
        LinearLayout layout = findViewById(R.id.support_content);
        if (layout != null) {
            layout.removeAllViews();
            TextView summary = new TextView(this);
            summary.setText(getString(R.string.support_summary));
            layout.addView(summary);
            // Linkler
            addButton(layout, "❤️ GitHub Sponsors", "https://github.com/sponsors/universish");
            // crypto section
            TextView cryptoTitle = new TextView(this);
            cryptoTitle.setText("\nKripto Cüzdanlar (Kopyalamak için tıkla)\n");
            cryptoTitle.setGravity(Gravity.CENTER);
            layout.addView(cryptoTitle);
            addCopyButton(layout, "Bitcoin (BTC)", "bc1q0wzwp7cta5pgmuaq46nkpjha0axkcx5yfkvmfmwm8ng2yewuprlq8sp6m5");
            addCopyButton(layout, "Patreon", "https://www.patreon.com/universish/gift");
            addCopyButton(layout, "IBAN", "LU494080000045715928");
            addCopyButton(layout, "BIC", "BCIRLULL");
            addButton(layout, "Kreosus", "https://kreosus.com/universish/about");
            addButton(layout, "GitHub Sponsors", "https://github.com/sponsors/universish");
        }
    }

    private void addButton(LinearLayout parent, String label, String url) {
        Button btn = new Button(this);
        btn.setText(label);
        btn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });
        parent.addView(btn);
    }

    private void addCopyButton(LinearLayout parent, String label, String value) {
        Button btn = new Button(this);
        btn.setText(label);
        btn.setOnClickListener(v -> {
            android.content.ClipboardManager cm =
                    (android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            cm.setPrimaryClip(android.content.ClipData.newPlainText(label, value));
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });
        parent.addView(btn);
    }
}

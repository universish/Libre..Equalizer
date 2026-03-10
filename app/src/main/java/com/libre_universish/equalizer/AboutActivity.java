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
import androidx.core.app.NavUtils;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import java.util.List;
import android.content.Intent;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.webkit.WebView;
import androidx.appcompat.app.AlertDialog;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CODEBERG = "https://codeberg.org/universish/Libre..Equalizer";
    public static final String PRIVACY_POLICY = "https://codeberg.org/universish/Libre..Equalizer/src/branch/main/PRIVACY_POLICY.md";
    private static final String TAG = "AboutActivity";
    LinearLayout donate, rateApp, eMail, licenses, privacyPolicy, github;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enforce selected language
        LocaleHelpers.applySavedLocale(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        if(sharedPreferences.getBoolean("dark_theme",true)){
            setTheme(R.style.AppTheme_Dark);
            Log.d(TAG, "onCreate: Dark Theme");
        }
        else {
            setTheme(R.style.AppTheme);
            Log.d(TAG, "onCreate: Light Theme");
        }
        
        
        setContentView(R.layout.activity_about);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);



        donate = findViewById(R.id.donate);
        rateApp = findViewById(R.id.rate_app);
        eMail = findViewById(R.id.report_bugs);
        licenses = findViewById(R.id.licenses);
        privacyPolicy = findViewById(R.id.privacy_policy);
        github = findViewById(R.id.fork_on_github);

        donate.setOnClickListener(this);
        rateApp.setOnClickListener(this);
        eMail.setOnClickListener(this);
        licenses.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        github.setOnClickListener(this);

        //Remove this once Billing is enabled.
//        donate.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private String readRawText(int resId) {
        try (InputStream is = getResources().openRawResource(resId);
             BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onClick(View v) {
        if(v==licenses){
            // show license text in an AlertDialog with a WebView
            View view = getLayoutInflater().inflate(R.layout.dialog_licenses, null);
            WebView web = (WebView) view;
            String license = readRawText(R.raw.license);
            web.loadDataWithBaseURL(null, license, "text/plain", "utf-8", null);
            new AlertDialog.Builder(this)
                    .setTitle("Licenses")
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
        else if (v == donate){
            Intent myIntent = new Intent(AboutActivity.this, SupportActivity.class);
            AboutActivity.this.startActivity(myIntent);
        }

        else if(v == rateApp){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:universish@tutamail.com"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"universish@tutamail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "App rating and feedback");
            String body = "Rate:\n" +
                    "⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐\n" +
                    "Please rate the app out of 10 by removing stars according to your score.\n\n" +
                    "Comment:\n" +
                    "Write your comment below to give feedback.";
            intent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        else if(v == eMail){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:universish@tutamail.com"));
            intent.putExtra(Intent.EXTRA_EMAIL, "universish@tutamail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Equalizer");
            startActivity(Intent.createChooser(intent, "E-Mail"));
        }
        else if (v == privacyPolicy){
            String priv = readRawText(R.raw.privacy);
            new AlertDialog.Builder(this)
                    .setTitle("Privacy Policy")
                    .setMessage(priv)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
        else if (v == github){
            openUrl(CODEBERG);
        }
    }
}

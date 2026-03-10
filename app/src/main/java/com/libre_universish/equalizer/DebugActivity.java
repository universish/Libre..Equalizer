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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Full‑screen diagnostics activity copied from the 3.5.3 base release.
 * Provides step‑by‑step log capture, sanitization and optional e‑mail
 * sending while preserving user privacy.  Additional header information
 * (app version and device details) are prepended to the output to give
 * more context for bug reports.
 */

public class DebugActivity extends Activity {
    private TextView text;
    private Button btnNext;
    private Button btnFinish;
    private int step = 0;
    private File rawReport;
    private File sanitizedReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
        showStepInstructions();
    }

    private void buildUi() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (30 * getResources().getDisplayMetrics().density);
        container.setPadding(pad, pad, pad, pad);

        text = new TextView(this);
        text.setTextSize(16f);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(text);

        btnNext = new Button(this);
        btnNext.setOnClickListener(v -> onNext());
        btnFinish = new Button(this);
        btnFinish.setOnClickListener(v -> onFinish());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f);
        container.addView(scroll, lp);
        container.addView(btnNext);
        container.addView(btnFinish);
        setContentView(container);
    }

    private void onNext() {
        if (step == 0) {
            showStepTest();
        } else if (step == 2) {
            sendEmailWithReport();
        }
    }

    private void onFinish() {
        if (step == 1) {
            beginLogCollection();
        }
    }

    private void showStepInstructions() {
        step = 0;
        text.setText("Please read the following instructions:\n1. Press the 'Next' button below.\n2. Use the app and reproduce the issue.\n3. Once the problem occurs again, press 'My test is done'.");
        btnNext.setText("Next");
        btnFinish.setVisibility(Button.GONE);
    }

    private void showStepTest() {
        step = 1;
        text.setText("Perform the test:\n• Use the app and trigger the issue.\n• When the problem recurs, press 'My test is done'.");
        btnNext.setVisibility(Button.GONE);
        btnFinish.setVisibility(Button.VISIBLE);
        btnFinish.setText("My test is done");
    }

    private void beginLogCollection() {
        step = 2;
        btnFinish.setVisibility(Button.GONE);
        text.setText("Please wait; logs are being created. Personal data will be removed.");
        new Thread(this::collectAndSanitizeLogs).start();
    }

    private void collectAndSanitizeLogs() {
        try {
            // header with app/device info for context
            String version = "";
            try {
                version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (Exception ignore) { }
            String header = "App version: " + version + "\n";
            header += "Device: " + Build.MODEL + " (" + Build.DEVICE + ")\n";
            header += "Brand: " + Build.BRAND + "\n";
            header += "Manufacturer: " + Build.MANUFACTURER + "\n";
            header += "Android Release: " + Build.VERSION.RELEASE + "\n";
            header += "Android SDK: " + Build.VERSION.SDK_INT + "\n";
            // include some preference and equalizer state to help debugging
            android.content.SharedPreferences prefs =
                    androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
            header += "dark_theme=" + prefs.getBoolean("dark_theme", true) + "\n";
            header += "eq_switch=" + prefs.getBoolean("eq_switch", true) + "\n";
            header += "bass_switch=" + prefs.getBoolean("bB_switch", true) + "\n";
            header += "virtual_switch=" + prefs.getBoolean("vir_switch", true) + "\n";
            header += "loud_switch=" + prefs.getBoolean("loud_switch", true) + "\n";
            try {
                Equalizer eq = EffectInstance.getEqualizerInstance();
                short bands = eq.getNumberOfBands();
                header += "num_bands=" + bands + "\n";
                for (short i = 0; i < bands && i < 5; i++) {
                    header += "band" + i + "=" + eq.getBandLevel(i) + "\n";
                }
                short presets = eq.getNumberOfPresets();
                header += "num_presets=" + presets + "\n";
            } catch (Exception e) {
                // ignore if no eq present
            }
            // audio manager info
            android.media.AudioManager am = (android.media.AudioManager)getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int curVol = am.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
                int maxVol = am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC);
                header += "vol=" + curVol + "/" + maxVol + "\n";
                header += "output_sr=" + am.getProperty(android.media.AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE) + "\n";
                header += "buffer=" + am.getProperty(android.media.AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER) + "\n";
                header += "devices=" + am.getDevices(android.media.AudioManager.GET_DEVICES_OUTPUTS).length + "\n";
            }
            // runtime memory
            Runtime rt = Runtime.getRuntime();
            header += "mem_free=" + rt.freeMemory() + "\n";
            header += "mem_total=" + rt.totalMemory() + "\n";
            header += "\n";

            Process proc = Runtime.getRuntime().exec(new String[]{
                    "logcat", "-d", "-v", "time",
                    "*:*"});
            String raw = new java.io.BufferedReader(
                    new java.io.InputStreamReader(proc.getInputStream()))
                    .lines().reduce("", (a, b) -> a + "\n" + b);
            proc.getInputStream().close();

            raw = header + raw;
            String sanitized = header + sanitizeLog(raw);

            String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            File folder = new File(getExternalFilesDir(null), "debug_reports");
            folder.mkdirs();
            rawReport = new File(folder, "debug_raw_" + ts + ".txt");
            sanitizedReport = new File(folder, "debug_sanitized_" + ts + ".txt");

            try (FileWriter fw = new FileWriter(rawReport)) {
                fw.write(raw);
            }
            try (FileWriter fw = new FileWriter(sanitizedReport)) {
                fw.write(sanitized);
            }

            runOnUiThread(this::showFinalScreen);
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Log creation failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private String sanitizeLog(String raw) {
        String out = raw;
        out = out.replaceAll("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", "[REDACTED_EMAIL]");
        out = out.replaceAll("\\+?\\d[\\d ()-]{6,}", "[REDACTED_PHONE]");
        out = out.replaceAll("(?i)[A-F0-9]{16,}", "[REDACTED_HEX]");
        out = out.replaceAll("[A-Za-z0-9-_]{32,}", "[REDACTED_TOKEN]");
        return out;
    }

    private void showFinalScreen() {
        step = 2;
        text.setText("Process complete. Two files have been saved:\n" +
                rawReport.getAbsolutePath() + "\n" +
                sanitizedReport.getAbsolutePath() + "\n" +
                "Please compare them to verify that your personal data has been removed.\n" +
                "If you consent to send the sanitized log, press the button below.");
        btnNext.setVisibility(Button.VISIBLE);
        btnNext.setText("I consent");
    }

    private void sendEmailWithReport() {
        if (sanitizedReport == null) return;
        try {
            android.net.Uri uri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider",
                    sanitizedReport);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"universish@tutamail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Hata Raporu] Libre Equalizer");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please describe the issue here and append device/OS info.\nLogs attached.\n");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(emailIntent, "Send bug report"));
        } catch (Exception e) {
            Toast.makeText(this, "Email not available: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

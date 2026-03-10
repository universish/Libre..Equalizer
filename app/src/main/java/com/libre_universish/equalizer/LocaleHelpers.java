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

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import java.util.Locale;

public class LocaleHelpers {
    /**
     * Apply the given ISO language code to the provided context.  The caller
     * should restart the activity after calling this to make the change
     * visible.  The new locale is also stored in shared preferences.
     */
    public static void setLocale(Context ctx, String langCode) {
        if (langCode == null || langCode.isEmpty()) return;
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources res = ctx.getResources();
        Configuration cfg = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cfg.setLocale(locale);
        } else {
            cfg.locale = locale;
        }
        res.updateConfiguration(cfg, res.getDisplayMetrics());
        // persist
        androidx.preference.PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .edit()
                .putString("app_lang", langCode)
                .apply();
    }

    /**
     * Load the saved language and apply it; useful during app startup.
     */
    public static void applySavedLocale(Context ctx) {
        String lang = androidx.preference.PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .getString("app_lang", "en");
        setLocale(ctx, lang);
    }
}

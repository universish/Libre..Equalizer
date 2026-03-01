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

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.libre_universish.equalizer.action.main";
        public static String STARTFOREGROUND_ACTION = "com.libre_universish.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.libre_universish.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
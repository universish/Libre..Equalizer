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

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.libre_universish.equalizer.action.main";
        public static String STARTFOREGROUND_ACTION = "com.libre_universish.equalizer.action.startforeground";
        public static String PREV_ACTION = "com.libre_universish.equalizer.action.prev";
        public static String PLAY_ACTION = "com.libre_universish.equalizer.action.play";
        public static String NEXT_ACTION = "com.libre_universish.equalizer.action.next";
        public static String STOPFOREGROUND_ACTION = "com.libre_universish.equalizer.action.stopforeground";
    }
    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}

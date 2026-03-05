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

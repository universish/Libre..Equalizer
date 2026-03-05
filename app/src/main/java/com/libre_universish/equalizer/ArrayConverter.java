
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import android.arch.persistence.room.TypeConverter;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ArrayConverter {

    private static final String TAG = "ArrayConverter";

    @TypeConverter
    public static int[] fromStringToArray(String value) {

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int[] slider = new int[5];
        for (int i=0; i<5; i++) {
            try {
                if (jsonArray != null) {
                    slider[i]=( jsonArray.getInt(i) );
                }
                else {
                    slider[i]=0;
                }
            } catch (JSONException e) {
                slider[i]=0;
            }
        }
        return slider;
    }

    @TypeConverter
    public static String fromArrayToString(int[] value) {
        Integer[] realInt = new Integer[value.length]; for(int i=0;i<value.length;i++){realInt[i]=Integer.valueOf(value[i]);}
        JSONArray jsonArray;
        jsonArray = new JSONArray(Arrays.asList(realInt));
        return jsonArray.toString();
    }
}
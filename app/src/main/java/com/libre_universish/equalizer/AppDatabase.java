
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.app.Application;
import android.content.Context;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;




@Database(entities = {CustomPreset.class}, version = 1)
@TypeConverters({ArrayConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CustomPresetDAO entryDAO();
    private static volatile AppDatabase INSTANCE;

    public static synchronized AppDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "custom_preset")
                            .build();
                }
        return INSTANCE;
    }
}

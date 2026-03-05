
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Dao;
import android.arch.lifecycle.LiveData;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CustomPresetDAO {
    @Query("SELECT * FROM custom_preset ORDER BY preset_name ASC")
    LiveData<List<CustomPreset>> getAllEntry();

    @Query("SELECT preset_name FROM custom_preset ORDER BY preset_name ASC")
    LiveData<List<String>> getPresetName();

    @Insert
    void insertAll(CustomPreset... customPresets);

    @Update
    void update(CustomPreset customPreset);

    @Delete
    void delete(CustomPreset customPreset);

}

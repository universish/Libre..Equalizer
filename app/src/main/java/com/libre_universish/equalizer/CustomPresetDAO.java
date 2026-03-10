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

import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Dao;
import androidx.lifecycle.LiveData;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;
import androidx.room.Update;
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

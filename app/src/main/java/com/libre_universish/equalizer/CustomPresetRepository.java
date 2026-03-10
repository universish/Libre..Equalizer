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

import android.os.AsyncTask;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;

public class CustomPresetRepository {
    private CustomPresetDAO mCustomPresetDAO;
    private LiveData<List<CustomPreset>> mAllEntry;
    private LiveData<List<String>> mPresetName;

    CustomPresetRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mCustomPresetDAO = db.entryDAO();
        mAllEntry = mCustomPresetDAO.getAllEntry();
        mPresetName = mCustomPresetDAO.getPresetName();
    }



    LiveData<List<CustomPreset>> getAllEntry() {
        return mAllEntry;
    }

    LiveData<List<String>> getPresetName() {
        return mPresetName;
    }

    public void insert(CustomPreset customPreset) {

        new insertAsyncTask(mCustomPresetDAO).execute(customPreset);
    }

    public void delete(CustomPreset customPreset) {

        new deleteAsyncTask(mCustomPresetDAO).execute(customPreset);
    }

    public void update(CustomPreset customPreset) {

        new updateAsyncTask(mCustomPresetDAO).execute(customPreset);
    }

    private static class updateAsyncTask extends AsyncTask<CustomPreset, Void, Void> {

        private CustomPresetDAO mAsyncTaskDao;

        updateAsyncTask(CustomPresetDAO dao) {

            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CustomPreset... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }



    private static class insertAsyncTask extends AsyncTask<CustomPreset, Void, Void> {

        private CustomPresetDAO mAsyncTaskDao;

        insertAsyncTask(CustomPresetDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CustomPreset... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<CustomPreset, Void, Void> {

        private CustomPresetDAO mAsyncTaskDao;

        deleteAsyncTask(CustomPresetDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CustomPreset... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}

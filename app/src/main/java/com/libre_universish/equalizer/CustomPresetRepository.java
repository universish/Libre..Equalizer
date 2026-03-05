
/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.os.AsyncTask;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import java.util.List;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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

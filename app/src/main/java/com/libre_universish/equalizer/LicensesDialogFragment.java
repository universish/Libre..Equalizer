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

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LicensesDialogFragment extends DialogFragment {

    public static LicensesDialogFragment newInstance() {
        return new LicensesDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WebView view = (WebView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_licenses, null);
        view.loadUrl("file:///android_asset/open_source_licenses.html");
        return new AlertDialog.Builder(getActivity())
                .setTitle("Licenses")
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}


/*
 * Original Code Copyright (c) 2018 Jazib (Jazib Khan) (GitHub: j4zib)
 * Modifications Copyright (c) 2026 universish (Saffet Yavuz) (GitHub: universish)
 * 
 * Licensed under the GNU General Public License v3.0
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.libre_universish.equalizer;
import android.arch.lifecycle.ViewModelProviders;
import android.preference.PreferenceManager;
import android.arch.lifecycle.ViewModelProviders;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;
import android.content.SharedPreferences;
import android.arch.lifecycle.ViewModel;
import android.media.audiofx.Equalizer;
import java.util.List;
import android.content.Intent;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import com.libre_universish.equalizer.R;
import com.libre_universish.equalizer.R;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

// import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

// public class SupportActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, View.OnClickListener {
//     BillingProcessor bp;
    CardView two,five,ten,twenty;
    TextView twoText,fiveText,tenText,twentyText,purchaseText;
    public static final String TWO_PRODUCT_ID = "two_dollar";
    public static final String FIVE_PRODUCT_ID = "five_dollar";
    public static final String TEN_PRODUCT_ID = "ten_dollar";
    public static final String TWENTY_PRODUCT_ID = "twenty_dollar";
    EqualizerViewModel equalizerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("dark_theme", true)) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_support);
        equalizerViewModel = ViewModelProviders.of(this).get(EqualizerViewModel.class);

        two = findViewById(R.id.two_card);
        five = findViewById(R.id.five_card);
        ten = findViewById(R.id.ten_card);
        twenty = findViewById(R.id.twenty_card);
        twoText = findViewById(R.id.two_text);
        fiveText = findViewById(R.id.five_text);
        tenText = findViewById(R.id.ten_text);
        twentyText = findViewById(R.id.twenty_text);
        purchaseText = findViewById(R.id.purchase_text);
        two.setVisibility(View.GONE);
        five.setVisibility(View.GONE);
        ten.setVisibility(View.GONE);
        twenty.setVisibility(View.GONE);
        purchaseText.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar_donation);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

//         // bp = new BillingProcessor(this, getString(R.string.license_key), this);
        bp.initialize();

        two.setOnClickListener(this);
        five.setOnClickListener(this);
        ten.setOnClickListener(this);
        twenty.setOnClickListener(this);

        if(equalizerViewModel.getIsPurchased()){
            purchaseText.setVisibility(View.VISIBLE);
        }
    }

        @Override
        public void onBillingInitialized() {
            two.setVisibility(View.VISIBLE);
            five.setVisibility(View.VISIBLE);
            ten.setVisibility(View.VISIBLE);
            twenty.setVisibility(View.VISIBLE);
            SkuDetails twoDetails = bp.getPurchaseListingDetails(TWO_PRODUCT_ID);
            SkuDetails fiveDetails = bp.getPurchaseListingDetails(FIVE_PRODUCT_ID);
            SkuDetails tenDetails = bp.getPurchaseListingDetails(TEN_PRODUCT_ID);
            SkuDetails twentyDetails = bp.getPurchaseListingDetails(TWENTY_PRODUCT_ID);
            twoText.setText(twoDetails.priceText);
            fiveText.setText(fiveDetails.priceText);
            tenText.setText(tenDetails.priceText);
            twentyText.setText(twentyDetails.priceText);
        }

        @Override
        public void onProductPurchased(String productId, TransactionDetails details) {
            /*
             * Called when requested PRODUCT ID was successfully purchased
             */

            switch (productId) {
                case TWO_PRODUCT_ID:
                    equalizerViewModel.setIsPurchased(true);
                    purchaseText.setVisibility(View.VISIBLE);
                    Toast.makeText(this, getString(R.string.purchased_successfully), Toast.LENGTH_SHORT).show();
                    break;
                case FIVE_PRODUCT_ID:
                    equalizerViewModel.setIsPurchased(true);
                    purchaseText.setVisibility(View.VISIBLE);
                    Toast.makeText(this, getString(R.string.purchased_successfully), Toast.LENGTH_SHORT).show();
                    break;
                case TEN_PRODUCT_ID:
                    equalizerViewModel.setIsPurchased(true);
                    purchaseText.setVisibility(View.VISIBLE);
                    Toast.makeText(this, getString(R.string.purchased_successfully), Toast.LENGTH_SHORT).show();
                    break;
                case TWENTY_PRODUCT_ID:
                    equalizerViewModel.setIsPurchased(true);
                    purchaseText.setVisibility(View.VISIBLE);
                    Toast.makeText(this, getString(R.string.purchased_successfully), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onBillingError(int errorCode, Throwable error) {
            /*
             * Called when some error occurred. See Constants class for more details
             *
             * Note - this includes handling the case where the user canceled the buy dialog:
             * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
             */
        }

        @Override
        public void onPurchaseHistoryRestored() {
            /*
             * Called when purchase history was restored and the list of all owned PRODUCT ID's
             * was loaded from Google Play
             */
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v==two){
            bp.purchase(this, TWO_PRODUCT_ID);
        }
        else if(v==five){
            bp.purchase(this, FIVE_PRODUCT_ID);
        }
        else if(v==ten){
            bp.purchase(this, TEN_PRODUCT_ID);
        }
        else if(v==twenty){
            bp.purchase(this, TWENTY_PRODUCT_ID);
        }

    }
}

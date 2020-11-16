package com.androidgames.WYRDate.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.androidgames.WYRDate.Interface.IShopView;
import com.androidgames.WYRDate.base.BasePresenter;
import com.androidgames.WYRDate.interactor.ShopActivityInteractor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class ShopActivityPresenter extends BasePresenter implements PurchasesUpdatedListener {

    private IShopView mShopActivity;
    private ShopActivityInteractor mInteractor;

    private ArrayList<String> skuList;
    private BillingClient billingClient;

    public ShopActivityPresenter(@NonNull IShopView shopView, ShopActivityInteractor interactor) {
        this.mShopActivity = shopView;
        this.mInteractor = interactor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        skuList = new ArrayList<>();
        skuList.add("hot_choice");
        skuList.add("fantasy_choice");
    }

    @Override
    public void onStart() {
        super.onStart();
        mShopActivity.hideLayoutFantasy();
        mShopActivity.hideLayoutHot();

        setupBillingClient();
        new LoadAds().execute();
    }

    public void setupBillingClient() {
        billingClient = BillingClient.newBuilder((Context) mShopActivity)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    mShopActivity.hideProgressBarShop();
                    loadAllSku();

                } else {
                    mShopActivity.hideProgressBarShop();
                    mShopActivity.showReconnectShop();
                    Toast.makeText((Context) mShopActivity, "Something went wrong!" + billingResult, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                mShopActivity.hideProgressBarShop();
                mShopActivity.showReconnectShop();
                Toast.makeText((Context) mShopActivity, "You are disconnected Billing", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadAllSku() {
        if (billingClient.isReady()) {

            SkuDetailsParams params = SkuDetailsParams.newBuilder().
                    setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {

                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                        List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                        for (final SkuDetails details : list) {
                            if (details.getSku().equals(skuList.get(0))) {
                                mShopActivity.showLayoutHot();
                                if (purchases != null && mInteractor.isProductOwned(details.getSku(), purchases)) {
                                    mShopActivity.createLayoutHot(true, null);
                                } else {
                                    mShopActivity.createLayoutHot(false, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            BillingFlowParams params = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(details)
                                                    .build();
                                            billingClient.launchBillingFlow((Activity) mShopActivity, params);
                                        }
                                    });
                                }
                            } else if (details.getSku().equals(skuList.get(1))) {
                                mShopActivity.showLayoutFantasy();
                                if (purchases != null && mInteractor.isProductOwned(details.getSku(), purchases)) {
                                    mShopActivity.createLayoutFantasy(true, null);
                                } else {
                                    mShopActivity.createLayoutFantasy(false, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            BillingFlowParams params = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(details)
                                                    .build();
                                            billingClient.launchBillingFlow((Activity) mShopActivity, params);
                                        }
                                    });

                                }
                            }
                        }
                    } else {
                        mShopActivity.showReconnectShop();
                        Toast.makeText((Context) mShopActivity, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            mShopActivity.showReconnectShop();
            Toast.makeText((Context) mShopActivity, "Billing is not ready", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> listPurchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && listPurchases != null) {
            for (Purchase purchase : listPurchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText((Context) mShopActivity, "You've cancelled your purchase", Toast.LENGTH_LONG).show();
        } else {
            // Handle any other error codes.
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(params, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        Toast.makeText((Context) mShopActivity, "Now your purchase is acknowledged", Toast.LENGTH_LONG).show();
                    }
                });
            }
            switch (purchase.getSku()) {
                case "hot_choice":
                    mShopActivity.createLayoutHot(true, null);
                    mShopActivity.popUpPurchasedItm();
                    Toast.makeText((Context) mShopActivity, "You've purchased the hot choices", Toast.LENGTH_LONG).show();
                    break;
                case "fantasy_choice":
                    mShopActivity.createLayoutFantasy(true, null);
                    mShopActivity.popUpPurchasedItm();
                    Toast.makeText((Context) mShopActivity, "You've purchased the fantasy choices", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //AsyncTask FOR LOAD AD BANNER
    private class LoadAds extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mShopActivity.showProgressBarAds();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MobileAds.initialize((Context) mShopActivity);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mShopActivity.loadAd(adRequest);
        }
    }
}

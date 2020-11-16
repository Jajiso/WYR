package com.androidgames.WYRDate.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.androidgames.WYRDate.Interface.IMainView;
import com.androidgames.WYRDate.Interface.InteractorListener;
import com.androidgames.WYRDate.R;
import com.androidgames.WYRDate.base.BasePresenter;
import com.androidgames.WYRDate.interactor.MainActivityInteractor;
import com.androidgames.WYRDate.repository.Entity.Scenario;
import com.androidgames.WYRDate.repository.Popup.PopUpDialog;
import com.androidgames.WYRDate.repository.Situations;
import com.androidgames.WYRDate.view.ShopActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityPresenter extends BasePresenter {

    private final IMainView mActivity;
    private final MainActivityInteractor mInteractor;
    private Situations actualDeck;
    private int addedDecks;
    private String actualScenarioID;
    private boolean isPercentageStage;
    private Map<String, Object> sessionClicks;
    private RewardedAd rewardedAd;


    private BillingClient billingClient;
    private List<Purchase> purchases;



    public MainActivityPresenter(@NonNull IMainView activity, MainActivityInteractor interactor) {
        this.mActivity = activity;
        this.mInteractor = interactor;
    }

    public void onCreate() {
        actualDeck = new Situations();
        addedDecks = 0;
        isPercentageStage = true;
        sessionClicks = new HashMap<>();

        addOrRemoveDeck(true, "basic");
        isPercentageStage(null);

        setOnCheckedListenerBasicCheckBox();
        setOnCheckedListenerHotCheckBox();
        setOnCheckedListenerFantasyCheckBox();

        setOnClickListenerShopButton();
        setOnClickListenerOptionsButton();
        createAndLoadRewardedAd();
        setOnClickListenerRewardedAdButton();
        new LoadAds().execute();

        setupBillingClient();
    }

    public void setupBillingClient() {
        billingClient = BillingClient.newBuilder((Context) mActivity)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                        setupBillingClient();
                    }
                })
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText((Context) mActivity, "Success to connect Billing", Toast.LENGTH_LONG).show();
                    loadAllSku();
                } else {
                    Toast.makeText((Context) mActivity, "Something went wrong!" + billingResult, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText((Context) mActivity, "You are disconnected Billing", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadAllSku() {
        if (billingClient.isReady()) {
            ArrayList<String> skuList = new ArrayList<>();
            skuList.add("hot_choice");
            skuList.add("fantasy_choice");

            SkuDetailsParams params = SkuDetailsParams.newBuilder().
                    setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                        purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                    } else {
                        Toast.makeText((Context) mActivity, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText((Context) mActivity, "Billing is not ready", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        PopUpDialog.incrementSessionToShowRatingDialogIfAllowed((Context) mActivity);
        super.onStart();
    }

    private void setOnCheckedListenerBasicCheckBox() {
        mActivity.setOnCheckedBasic(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                addOrRemoveDeck(b, "basic");
            }
        });
    }

    private void setOnCheckedListenerHotCheckBox() {
        mActivity.setOnCheckedHot(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mInteractor.isProductOwned("hot_choice", purchases)) {
                    addOrRemoveDeck(b, "hot");
                } else {
                    compoundButton.setChecked(false);
                    Toast.makeText((Context) mActivity, "You don't have hot deck!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setOnCheckedListenerFantasyCheckBox() {
        mActivity.setOnCheckedFantasy(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mInteractor.isProductOwned("hot_choice", purchases)) {
                    addOrRemoveDeck(b, "fantasy");
                } else {
                    compoundButton.setChecked(false);
                    Toast.makeText((Context) mActivity, "You don't have fantasy deck!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setOnClickListenerShopButton() {
        mActivity.setOnClickShop(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Activity) mActivity, ShopActivity.class);
                ((Activity) mActivity).startActivity(intent);
            }
        });
    }

    private void setOnClickListenerRewardedAdButton() {
        mActivity.setOnClickRewardedAd(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardedAd();
            }
        });
    }

    private void setOnClickListenerOptionsButton() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPercentageStage(view);
            }
        };
        mActivity.setOnClickOptionA(listener);
        mActivity.setOnClickOptionB(listener);
    }

    private void addOrRemoveDeck(boolean isToAdd, String deck) {
        if (isToAdd) {
            switch (deck) {
                case "basic":
                    actualDeck.addAll(mInteractor.getBasicDeck((Context) mActivity));
                    addedDecks++;
                    if (addedDecks == 1) {
                        mActivity.setBasicCheckBoxEnable(!mActivity.isBasicCheckBoxChecked());
                        mActivity.setHotCheckBoxEnable(!mActivity.isHotCheckBoxChecked());
                        mActivity.setFantasyCheckBoxEnable(!mActivity.isFantasyCheckBoxChecked());
                    }
                    break;
                case "hot":
                    mInteractor.getHotDeck(new InteractorListener.scenarioListListener() {
                        @Override
                        public void onSuccess(ArrayList<Scenario> list) {
                            actualDeck.addAll(list);
                            addedDecks++;
                            if (addedDecks == 1) {
                                mActivity.setBasicCheckBoxEnable(!mActivity.isBasicCheckBoxChecked());
                                mActivity.setHotCheckBoxEnable(!mActivity.isHotCheckBoxChecked());
                                mActivity.setFantasyCheckBoxEnable(!mActivity.isFantasyCheckBoxChecked());
                            }
                        }
                        @Override
                        public void onError() {
                            Toast.makeText((Context) mActivity, "You don't have connection", Toast.LENGTH_SHORT).show();
                            mActivity.setHotCheck(false);
                        }
                    });
                    break;
                case "fantasy":
                    mInteractor.getFantasyDeck(new InteractorListener.scenarioListListener() {
                        @Override
                        public void onSuccess(ArrayList<Scenario> list) {
                            actualDeck.addAll(list);
                            addedDecks++;
                            if (addedDecks == 1) {
                                mActivity.setBasicCheckBoxEnable(!mActivity.isBasicCheckBoxChecked());
                                mActivity.setHotCheckBoxEnable(!mActivity.isHotCheckBoxChecked());
                                mActivity.setFantasyCheckBoxEnable(!mActivity.isFantasyCheckBoxChecked());
                            }
                        }
                        @Override
                        public void onError() {
                            Toast.makeText((Context) mActivity, "You don't have connection", Toast.LENGTH_SHORT).show();
                            mActivity.setFantasyCheck(false);
                        }
                    });
                    break;
            }
        } else {
            switch (deck) {
                case "basic":
                    actualDeck.removeAll(mInteractor.getBasicDeck((Context) mActivity));
                    addedDecks--;
                    if (addedDecks == 1) {
                        mActivity.setBasicCheckBoxEnable(!mActivity.isBasicCheckBoxChecked());
                        mActivity.setHotCheckBoxEnable(!mActivity.isHotCheckBoxChecked());
                        mActivity.setFantasyCheckBoxEnable(!mActivity.isFantasyCheckBoxChecked());
                    }
                    break;
                case "hot":
                    mInteractor.getHotDeck(new InteractorListener.scenarioListListener() {
                        @Override
                        public void onSuccess(ArrayList<Scenario> list) {
                            actualDeck.removeAll(list);
                            addedDecks--;
                            if (addedDecks == 1) {
                                mActivity.setBasicCheckBoxEnable(!mActivity.isBasicCheckBoxChecked());
                                mActivity.setHotCheckBoxEnable(!mActivity.isHotCheckBoxChecked());
                                mActivity.setFantasyCheckBoxEnable(!mActivity.isFantasyCheckBoxChecked());
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
                    break;
                case "fantasy":
                    mInteractor.getFantasyDeck(new InteractorListener.scenarioListListener() {
                        @Override
                        public void onSuccess(ArrayList<Scenario> list) {
                            actualDeck.removeAll(list);
                            addedDecks--;
                            if (addedDecks == 1) {
                                mActivity.setBasicCheckBoxEnable(!mActivity.isBasicCheckBoxChecked());
                                mActivity.setHotCheckBoxEnable(!mActivity.isHotCheckBoxChecked());
                                mActivity.setFantasyCheckBoxEnable(!mActivity.isFantasyCheckBoxChecked());
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
                    break;
            }
        }
        if(addedDecks>1) {
            mActivity.setBasicCheckBoxEnable(true);
            mActivity.setHotCheckBoxEnable(true);
            mActivity.setFantasyCheckBoxEnable(true);
        }
    }

    private void isPercentageStage(final View view) {
        if (isPercentageStage){
            getNextScenario(actualDeck.nextScenario());
        } else {
            mInteractor.setOnOptionsPercentage(new InteractorListener.PercentageListener() {
                @Override
                public void onSuccess() {
                    if (view.getId() == R.id.option_a_button) {
                        incrementClickSession("optionA");
                    } else if (view.getId() == R.id.option_b_button) {
                        incrementClickSession("optionB");
                    }
                    int[] percentages = mInteractor.getOptionsPercentage(sessionClicks, actualScenarioID);
                    mActivity.setTextPercentageA(percentages[0]+"%");
                    mActivity.setTextPercentageB(percentages[1]+"%");
                    mActivity.showPercentageA();
                    mActivity.showPercentageB();
                    isPercentageStage = true;
                }
            });
        }
    }

    private void getNextScenario(Scenario scenario) {
        actualScenarioID = scenario.getId();
        mActivity.hidePercentageA();
        mActivity.hidePercentageB();
        mActivity.setTextOptionA(scenario.getOptionA());
        mActivity.setTextOptionB(scenario.getOptionB());
        mActivity.setIconImage(scenario.getCategory());
        isPercentageStage = false;
        popUpRatingOurApp();
    }

    private void popUpRatingOurApp() {
        if (PopUpDialog.shouldDhowRatingDialog((Context) mActivity) ) {
            mActivity.showPopUpRatingOurApp();
        }
    }

    public void incrementClickSession(String option) {
        int[] clicks;
        if (!sessionClicks.containsKey(actualScenarioID)) {
            clicks = new int[2];
        } else {
            clicks = mInteractor.getClicksNumberInScenario(sessionClicks, actualScenarioID);
        }
        switch (option) {
            case "optionA":
                clicks[0]++;
                break;
            case "optionB":
                clicks[1]++;
                break;
        }
        sessionClicks.put(actualScenarioID, mInteractor.setClicksOptionsInScenario(clicks));
    }

    private void showRewardedAd() {
        if (rewardedAd.isLoaded()) {
            mActivity.setRewardedAdButton(false);
            rewardedAd.show((Activity) mActivity, new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                    createAndLoadRewardedAd();
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    mInteractor.getRewardedAdScenario(new InteractorListener.rewardedAdListener() {
                        @Override
                        public void onSuccess(Scenario scenario) {
                            getNextScenario(scenario);
                            mActivity.showPopUpRewardedAd();
                        }
                        @Override
                        public void onError() {
                        }
                    });

                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            });
        }
    }

    //METHODS FOR REWARDED AD
    private void createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd((Context) mActivity, ((Context) mActivity).getResources().getString(R.string.id_rewarded_ad));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                mActivity.setRewardedAdButton(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                mActivity.setRewardedAdButton(false);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    //AsyncTask FOR LOAD AD BANNER
    private class LoadAds extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mActivity.showProgressBarAds();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MobileAds.initialize((Context) mActivity);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mActivity.loadAd(adRequest);
        }
    }
}

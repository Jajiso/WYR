package com.androidgames.WYRDate.view;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidgames.WYRDate.Interface.IMainView;
import com.androidgames.WYRDate.R;
import com.androidgames.WYRDate.base.BaseActivity;
import com.androidgames.WYRDate.interactor.MainActivityInteractor;
import com.androidgames.WYRDate.presenter.MainActivityPresenter;
import com.androidgames.WYRDate.repository.Popup.PopUpDialog;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends BaseActivity<MainActivityPresenter> implements IMainView {

    private CheckBox checkBox_basic, checkBox_hot, checkBox_fantasy;
    private Button option_A, option_B, button_shop, rewarded_ad;
    private TextView percentage_A, percentage_B;
    private ImageView icon_deck;
    private ProgressBar progressBarAds;
    private AdView mAdView;

    @NonNull
    @Override
    protected MainActivityPresenter createPresenter(@NonNull Context context) {
        return new MainActivityPresenter(this, new MainActivityInteractor());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //THIS WILL HIDE THE TITLE BAR
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //THIS WILL KEEP THE PORTRAIT OR LANDSCAPE MODE
        if (getResources().getBoolean(R.bool.is_portrait_only)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_main);

        //MAIN_COMPONENTS
        option_A = findViewById(R.id.option_a_button);
        option_B = findViewById(R.id.option_b_button);

        checkBox_basic = findViewById(R.id.basic);
        checkBox_hot = findViewById(R.id.hot);
        checkBox_fantasy = findViewById(R.id.fantasy);
        button_shop = findViewById(R.id.button_shop);

        percentage_A = findViewById(R.id.percentage_a_textView);
        percentage_B = findViewById(R.id.percentage_b_textView);

        icon_deck = findViewById(R.id.icon_deck);

        //BUTTON_REWARDED_AD
        rewarded_ad = findViewById(R.id.rewarded_ad);

        //AD_BANNER
        mAdView = findViewById(R.id.adView);
        progressBarAds = findViewById(R.id.progressBarAds);

        mPresenter.onCreate();
    }

    @Override
    public void showProgressBarAds() {
        progressBarAds.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBarAds() {
        progressBarAds.setVisibility(View.GONE);
    }

    @Override
    public void loadAd(AdRequest ad) {
        mAdView.loadAd(ad);
    }

    @Override
    public void setTextOptionA(String optionA) {
        option_A.setText(optionA);
    }

    @Override
    public void setTextOptionB(String optionB) {
        option_B.setText(optionB);
    }

    @Override
    public void setTextPercentageA(String percentageA) {
        percentage_A.setText(percentageA);
    }

    @Override
    public void setTextPercentageB(String percentageB) {
        percentage_B.setText(percentageB);
    }

    @Override
    public void showPercentageA() {
        percentage_A.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPercentageB() {
        percentage_B.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePercentageA() {
        percentage_A.setVisibility(View.GONE);
    }

    @Override
    public void hidePercentageB() {
        percentage_B.setVisibility(View.GONE);
    }

    @Override
    public void setIconImage(String icon) {
        switch (icon) {
            case "basic":
                icon_deck.setImageDrawable(getDrawable(R.drawable.heart));
                break;
            case "hot":
                icon_deck.setImageDrawable(getDrawable(R.drawable.flame));
                break;
            case "fantasy":
                icon_deck.setImageDrawable(getDrawable(R.drawable.bat));
                break;
            case "ad":
                icon_deck.setImageDrawable(getDrawable(R.drawable.crown_small));
                break;
            default:
                icon_deck.setImageDrawable(null);
                break;
        }
        if (!getResources().getBoolean(R.bool.is_portrait_only)) {
            ImageView icon_deck2 = findViewById(R.id.icon_deck2);
            switch (icon) {
                case "basic":
                    icon_deck2.setImageDrawable(getDrawable(R.drawable.heart));
                    break;
                case "hot":
                    icon_deck2.setImageDrawable(getDrawable(R.drawable.flame));
                    break;
                case "fantasy":
                    icon_deck2.setImageDrawable(getDrawable(R.drawable.bat));
                    break;
                case "ad":
                    icon_deck2.setImageDrawable(getDrawable(R.drawable.crown_small));
                    break;
                default:
                    icon_deck2.setImageDrawable(null);
                    break;
            }
        }
    }

    @Override
    public void setRewardedAdButton(boolean isEnable) {
        if (isEnable) {
            rewarded_ad.setEnabled(true);
            rewarded_ad.setAlpha(1);
        } else {
            rewarded_ad.setEnabled(false);
            rewarded_ad.setAlpha(0.8f);
        }
    }

    @Override
    public void showPopUpRewardedAd() {
        new PopUpDialog.Builder(this)
                .setLayout(R.layout.dialog_rewarded_ad)
                .build()
                .showDialog();
    }

    @Override
    public void showPopUpRatingOurApp() {
        new PopUpDialog.Builder(this)
                .setLayout(R.layout.dialog_rate_app)
                .IsRateOurAppDialog(true)
                .setIdButtonPositive(R.id.btnOk)
                .setIdButtonNegative(R.id.btnNegative)
                .setSessionToLaunch(2)
                .setActionTriggerCount(10)
                .build()
                .showDialog();
    }

    @Override
    public void showPopUpNotPremiumDeck() {
        new PopUpDialog.Builder(this)
                .setLayout(R.layout.dialog_rewarded_ad)
                .build()
                .showDialog();
    }

    @Override
    public void setBasicCheckBoxEnable(boolean isEnable) {
        checkBox_basic.setEnabled(isEnable);
    }

    @Override
    public void setHotCheckBoxEnable(boolean isEnable) {
        checkBox_hot.setEnabled(isEnable);
    }

    @Override
    public void setFantasyCheckBoxEnable(boolean isEnable) {
        checkBox_fantasy.setEnabled(isEnable);
    }

    @Override
    public void setBasicCheck(boolean check) {
        checkBox_basic.setChecked(check);
    }

    @Override
    public void setHotCheck(boolean check) {
        checkBox_hot.setChecked(check);
    }

    @Override
    public void setFantasyCheck(boolean check) {
        checkBox_fantasy.setChecked(check);
    }

    @Override
    public boolean isBasicCheckBoxChecked() {
        return checkBox_basic.isChecked();
    }

    @Override
    public boolean isHotCheckBoxChecked() {
        return checkBox_hot.isChecked();
    }

    @Override
    public boolean isFantasyCheckBoxChecked() {
        return checkBox_fantasy.isChecked();
    }

    @Override
    public void setOnClickOptionA(View.OnClickListener onClickListener) {
        option_A.setOnClickListener(onClickListener);
    }

    @Override
    public void setOnClickOptionB(View.OnClickListener onClickListener) {
        option_B.setOnClickListener(onClickListener);
    }

    @Override
    public void setOnCheckedBasic(CompoundButton.OnCheckedChangeListener listener) {
        checkBox_basic.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setOnCheckedHot(CompoundButton.OnCheckedChangeListener listener) {
        checkBox_hot.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setOnCheckedFantasy(CompoundButton.OnCheckedChangeListener listener) {
        checkBox_fantasy.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setOnClickShop(View.OnClickListener onClickListener) {
        button_shop.setOnClickListener(onClickListener);
    }

    @Override
    public void setOnClickRewardedAd(View.OnClickListener onClickListener) {
        rewarded_ad.setOnClickListener(onClickListener);
    }
}
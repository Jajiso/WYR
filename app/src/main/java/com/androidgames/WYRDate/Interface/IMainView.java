package com.androidgames.WYRDate.Interface;

import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.ads.AdRequest;

public interface IMainView {
    void showProgressBarAds();
    void hideProgressBarAds();
    void loadAd(AdRequest ad);
    void setTextOptionA(String optionA);
    void setTextOptionB(String optionB);
    void setTextPercentageA(String percentageA);
    void setTextPercentageB(String percentageB);
    void showPercentageA();
    void showPercentageB();
    void hidePercentageA();
    void hidePercentageB();
    void setIconImage(String icon);
    void setRewardedAdButton(boolean isEnable);
    void showPopUpRewardedAd();
    void showPopUpRatingOurApp();
    void showPopUpNotPremiumDeck();
    void setBasicCheckBoxEnable(boolean isEnable);
    void setHotCheckBoxEnable(boolean isEnable);
    void setFantasyCheckBoxEnable(boolean isEnable);
    void setBasicCheck(boolean check);
    void setHotCheck(boolean check);
    void setFantasyCheck(boolean check);
    boolean isBasicCheckBoxChecked();
    boolean isHotCheckBoxChecked();
    boolean isFantasyCheckBoxChecked();
    void setOnClickOptionA(View.OnClickListener onClickListener);
    void setOnClickOptionB(View.OnClickListener onClickListener);
    void setOnCheckedBasic(CompoundButton.OnCheckedChangeListener listener);
    void setOnCheckedHot(CompoundButton.OnCheckedChangeListener listener);
    void setOnCheckedFantasy(CompoundButton.OnCheckedChangeListener listener);
    void setOnClickShop(View.OnClickListener onClickListener);
    void setOnClickRewardedAd(View.OnClickListener onClickListener);
}

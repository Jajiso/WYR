package com.campfiregames.WYRDate.Interface;

import android.view.View;

import com.google.android.gms.ads.AdRequest;

public interface IShopView {
    void showProgressBarAds();
    void hideProgressBarAds();
    void loadAd(AdRequest ad);
    void showProgressBarShop();
    void hideProgressBarShop();
    void showReconnectShop();
    void hideReconnectShop();
    void createLayoutHot(boolean isProductOwned, View.OnClickListener onClickListener);
    void createLayoutFantasy(boolean isProductOwned, View.OnClickListener onClickListener);
    void showLayoutHot();
    void hideLayoutHot();
    void showLayoutFantasy();
    void hideLayoutFantasy();
    void popUpPurchasedItm();
}

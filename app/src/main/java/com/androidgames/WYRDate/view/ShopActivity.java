package com.androidgames.WYRDate.view;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidgames.WYRDate.Interface.IShopView;
import com.androidgames.WYRDate.R;
import com.androidgames.WYRDate.base.BaseActivity;
import com.androidgames.WYRDate.interactor.ShopActivityInteractor;
import com.androidgames.WYRDate.presenter.ShopActivityPresenter;
import com.androidgames.WYRDate.repository.Popup.PopUpDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ShopActivity extends BaseActivity<ShopActivityPresenter> implements IShopView {

    private ProgressBar progressBarAds, progressBarShop;
    private AdView mAdView;
    private TextView tvReconnect;
    private FrameLayout frameLayoutHot;
    private FrameLayout frameLayoutFantasy;
    private ImageView tickHot, tickFantasy;


    @NonNull
    @Override
    protected ShopActivityPresenter createPresenter(@NonNull Context context) {
        return new ShopActivityPresenter(this, new ShopActivityInteractor());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //THIS WILL HIDE THE TITLE BAR
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //THIS WILL KEEP THE PORTRAIT OR LANDSCAPE MODE
        if(getResources().getBoolean(R.bool.is_portrait_only)){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_shop);

        //AD_BANNER
        mAdView = findViewById(R.id.adView);
        progressBarAds = findViewById(R.id.progressBarAds);

        //FRAME_LAYOUT_PRODUCTS
        frameLayoutHot = findViewById(R.id.include_hot);
        frameLayoutFantasy = findViewById(R.id.include_fantasy);

        //IMAGE_TICK_PRODUCTS
        tickHot = findViewById(R.id.image_tick_hot);
        tickFantasy = findViewById(R.id.image_tick_fantasy);

        //RECONNECT_VIEWS
        progressBarShop = findViewById(R.id.progressBarShop);
        tvReconnect = findViewById(R.id.tvReconnect);
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
    public void showProgressBarShop() {
        progressBarShop.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBarShop() {
        progressBarShop.setVisibility(View.GONE);
    }

    @Override
    public void showReconnectShop() {
        tvReconnect.setVisibility(View.VISIBLE);
        tvReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBarShop();
                hideReconnectShop();
                mPresenter.setupBillingClient();
            }
        });
    }

    @Override
    public void hideReconnectShop() {
        tvReconnect.setVisibility(View.GONE);
    }

    @Override
    public void createLayoutHot(boolean isProductOwned, View.OnClickListener onClickListener) {
        TextView tvTitle = frameLayoutHot.findViewById(R.id.tvTitle);
        TextView tvMessage = frameLayoutHot.findViewById(R.id.tvMessage);
        TextView tvMessage2 = frameLayoutHot.findViewById(R.id.tvMessage2);
        Button btnPurchase = frameLayoutHot.findViewById(R.id.btnPurchase);
        ImageView icon = frameLayoutHot.findViewById(R.id.imageIcon);

        tvTitle.setText("HOT");
        icon.setImageDrawable(getDrawable(R.drawable.flame));
        tvMessage.setText("Get dirty with questions that will make your prudish friend sweat!");
        tvMessage2.setText("20 NEW Choices!");
        if (isProductOwned){
            tickHot.setVisibility(View.VISIBLE);
            btnPurchase.setText("owned");
            btnPurchase.setAlpha(0.5f);
            btnPurchase.setEnabled(false);
        } else {
            btnPurchase.setText("£0.99");
            btnPurchase.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void createLayoutFantasy(boolean isProductOwned, View.OnClickListener onClickListener) {
        TextView tvTitle = frameLayoutFantasy.findViewById(R.id.tvTitle);
        TextView tvMessage = frameLayoutFantasy.findViewById(R.id.tvMessage);
        TextView tvMessage2 = frameLayoutFantasy.findViewById(R.id.tvMessage2);
        Button btnPurchase = frameLayoutFantasy.findViewById(R.id.btnPurchase);
        ImageView icon = frameLayoutFantasy.findViewById(R.id.imageIcon);

        tvTitle.setText("FANTASY");
        icon.setImageDrawable(getDrawable(R.drawable.bat));
        tvMessage.setText("Yes, we know you are into vampires, werewolves and all kinds of crazy.");
        tvMessage2.setText("20 NEW Choices!");
        if (isProductOwned){
            tickFantasy.setVisibility(View.VISIBLE);
            btnPurchase.setText("owned");
            btnPurchase.setAlpha(0.5f);
            btnPurchase.setEnabled(false);
        } else {
            btnPurchase.setText("£0.99");
            btnPurchase.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void showLayoutHot() {
        frameLayoutHot.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLayoutHot() {
        frameLayoutHot.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutFantasy() {
        frameLayoutFantasy.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLayoutFantasy() {
        frameLayoutFantasy.setVisibility(View.GONE);
    }

    @Override
    public void popUpPurchasedItm() {
        new PopUpDialog.Builder(this)
                .setLayout(R.layout.dialog_purchased_deck)
                .build()
                .showDialog();
    }
}
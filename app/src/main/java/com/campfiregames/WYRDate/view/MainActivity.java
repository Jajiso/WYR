package com.campfiregames.WYRDate.view;

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

import com.campfiregames.WYRDate.Interface.IMainView;
import com.campfiregames.WYRDate.R;
import com.campfiregames.WYRDate.base.BaseActivity;
import com.campfiregames.WYRDate.interactor.MainActivityInteractor;
import com.campfiregames.WYRDate.presenter.MainActivityPresenter;

import com.campfiregames.WYRDate.repository.Popup.PopUpDialog;
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


    //
//    private CheckBox button_basic;
//    private CheckBox button_hot;
//    private CheckBox button_fantasy;
//    private Button btn_shop;
//    private Button option_A;
//    private Button option_B;
//    private TextView porcentage_A;
//    private TextView porcentage_B;
//    private ImageView icon_deck;
//    private ImageView icon_deck2;
//    private boolean isPorcentage;
//
//    private Situations deck_basic;
//    private Situations deck_hot;
//    private Situations deck_fantasy;
//    private Situations deck_ad;
//    private Situations situations_display;
//    private int addedDecks;
//
//    private Button add_reward;
//    private AdView mAdView;
//    private ProgressBar progress_bar_ads;
//    private RewardedAd rewardedAd;
//
//    private FireBaseManager fireBaseManager;
//
//
//    @NonNull
//    @Override
//    protected MainActivityPresenter createPresenter(@NonNull Context context) {
//        return new MainActivityPresenter(this, new MainActivityInteractor(/*context*/));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //THIS WILL HIDE THE TITLE BAR
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //THIS WILL KEEP THE PORTRAIT OR LANDSCAPE MODE
//
//        if(getResources().getBoolean(R.bool.is_portrait_only)){
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }else{
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//        setContentView(R.layout.activity_main);
//
//
//
//
//
//
//        //COMPONENTS TO DISPLAY SCENARIO
//        option_A = findViewById(R.id.option_a_button);
//        option_B = findViewById(R.id.option_b_button);
//        porcentage_A = findViewById(R.id.percentage_a_textView);
//        porcentage_B = findViewById(R.id.percentage_b_textView);
//        icon_deck = findViewById(R.id.icon_deck);
//        icon_deck2 = findViewById(R.id.icon_deck2);
//        isPorcentage = false;
//
//        //BUTTONS DECKS
//        button_basic = findViewById(R.id.basic);
//        button_hot = findViewById(R.id.hot);
//        button_fantasy = findViewById(R.id.fantasy);
//
//        //AD_BANNER
//        mAdView = findViewById(R.id.adView);
//        progress_bar_ads = findViewById(R.id.progressBarAds);
//        new LoadAds().execute();
//
//        //REWARDED_AD
//        add_reward = findViewById(R.id.add_reward);
//        createAndLoadRewardedAd();
//
//        //BUTTONS SHOP
//        btn_shop = findViewById(R.id.button_shop);
//
//
//
//
//
//
//        /*
//        //DATABASE MANAGERS
//        fireBaseManager = new FireBaseManager();
//        try {
//            jsonManager = new JSONManager(this, "decksDB.json");
//        }catch (JSONException e){}
//
//        //DIFFERENT SITUATIONS
//        try {
//            deck_basic = jsonManager.getSituationFromJSON("basic");
//            deck_hot = jsonManager.getSituationFromJSON("hot");
//            deck_fantasy = jsonManager.getSituationFromJSON("fantasy");
//
//            deck_ad = jsonManager.getSituationFromJSON("ad");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }*/
//
//        //DB TEMPORAL
//        deck_basic = mPresenter.addSituationFromDB("basic");
//        deck_hot = mPresenter.addSituationFromDB("hot");
//        deck_fantasy = mPresenter.addSituationFromDB("fantasy");
//
//
//        situations_display = new Situations();
//        addedDecks = 0;
//        addDeck(button_basic.isChecked(), "basic");
//
//
//






//
//
//
//
//        fireBaseManager = new FireBaseManager();
//        //option_A.setText(fireBaseManager.instString());
//        //fireBaseManager.asdf();
//
//        //fireBaseManager.createHOT(deck_hot.getSituation(), deck_fantasy.getSituation()/*deck_hot.nextScenario()*/ );
//        /*Situations s = fireBaseManager.readSituation("hot");
//        Scenario a = s.nextScenario();
//        option_A.setText(a.getOptionA());
//        option_A.setText(a.getOptionB());*/
//        /*
//        ArrayList<Scenario> todos = new ArrayList<>();
//        todos.addAll(deck_basic.getSituation());
//        todos.addAll(deck_hot.getSituation());
//        todos.addAll(deck_fantasy.getSituation());
//        todos.addAll(deck_ad.getSituation());







//        fireBaseManager.asdf(todos);*/
//        String a = "";
//        fireBaseManager.readString("hot", /*new CallbackDB.onCallbackJson() {
//            @Override
//            public void onCallback(String json) {
//                Log.w("JSONACTIVITY: ", json);
//            }
//        });*/ new FireBaseManager.Listenner() {
//                    @Override
//                    public void listenner(String json) {
//                        Log.w("JSONACTIVITY: ", json);
//                    }
//        });
//
//                Log.w("JSONACTIVITY: ", a);
//        //fireBaseManager.readString("hot");
//        Log.w("HOLA", "Mensaje por LOG W");
//
//        fireBaseManager.readString("hot", /*new CallbackDB.onCallbackJson() {
//            @Override
//            public void onCallback(String json) {
//                Log.w("JSONACTIVITY: ", json);
//            }
//        });*/ new FireBaseManager.Listenner() {
//            @Override
//            public void listenner(String json) {
//                String array = JSONManager.getJsonArrayFromJsonObject(json, "hot");
//
//                List<String> objJSON = JSONManager.getJsonObjectsFromJsonArray(array);
//                for (String js : objJSON) {
//                    Scenario scenario = JSONManager.getScenarioFromJsonObject(js);
//                    Log.w("ACTIVITY JSONMANAGER2", ""+scenario.toString());
//                }
//
//            }
//        });







//
//
//        //SET_ON_CLICK_LISTENERS
//        option_A.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                getScenario(view);
//            }
//        });
//
//        option_B.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                getScenario(view);
//            }
//        });
//
//        btn_shop.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        button_basic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addDeck(button_basic.isChecked(), "basic");
//                Log.w("checkbox", "" );
//                //mPresenter.onSelectDeck(button_basic.isChecked(), "basic");
//            }
//        });
//
//        button_hot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addDeck(button_hot.isChecked(), "hot");
//                //mPresenter.onSelectDeck(button_hot.isChecked(), "hot");
//            }
//        });
//
//        button_fantasy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addDeck(button_fantasy.isChecked(), "fantasy");
//                //mPresenter.onSelectDeck(button_fantasy.isChecked(), "fantasy");
//            }
//        });
//
//        add_reward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showRewardedAd();
//            }
//        });
//
//
//        PopUpDialog.ratingDialogOnCreate(this);
//
//    }
//




//    private void getScenarioPremium() {
//        Scenario scenarioBuffer = deck_ad.nextScenario();
//        String optionA = scenarioBuffer.getOptionA();
//        String optionB = scenarioBuffer.getOptionB();
//
//        option_A.setText(optionA);
//        option_B.setText(optionB);
//
//        porcentage_A.setVisibility(View.GONE);
//        porcentage_B.setVisibility(View.GONE);
//
//        getScenarioIcon(scenarioBuffer.getCategory(), icon_deck);
//        getScenarioIcon(scenarioBuffer.getCategory(), icon_deck2);
//
//        isPorcentage = false;
//    }
//



//
//    private void showPopUpRate(){
//        if (PopUpDialog.isAgreeShowRatingDialog(this)){
//            new PopUpDialog.Builder(this)
//                    .setLayout(R.layout.dialog_rate_app)
//                    .IsRateOurAppDialog(true)
//                    .setActionTriggerCount(10)
//                    .setSessionToLaunch(2)
//                    .build()
//                    .showDialog();
//        }
//    }
//
//    private void showRewardedPopUp(){
//        new PopUpDialog.Builder(this)
//                .setLayout(R.layout.dialog_rewarded_ad)
//                .build()
//                .showDialog();
//    }
//




//
//    public void getScenario(View view) {
//        //Scenario scenarioBuffer = mPresenter.getRandomScenario();
//        //if (scenarioBuffer != null) {
//        if (addedDecks != 0 ){
//            if(isPorcentage){
//                Scenario scenarioBuffer = situations_display.nextScenario();
//                String optionA = scenarioBuffer.getOptionA();
//                String optionB = scenarioBuffer.getOptionB();
//
//                option_A.setText(optionA);
//                option_B.setText(optionB);
//
//                porcentage_A.setVisibility(View.GONE);
//                porcentage_B.setVisibility(View.GONE);
//
//                getScenarioIcon(scenarioBuffer.getCategory(), icon_deck);
//                getScenarioIcon(scenarioBuffer.getCategory(), icon_deck2);
//
//                isPorcentage = false;
//                showPopUpRate();
//
//            }else {
//                porcentage_A.setText("60%");
//                porcentage_B.setText("40%");
//
//                porcentage_A.setVisibility(View.VISIBLE);
//                porcentage_B.setVisibility(View.VISIBLE);
//
//                isPorcentage = true;
//            }
//        }else {
//            option_A.setText("Please choose a deck to start");
//            option_B.setText("Please choose a deck to start");
//            getScenarioIcon("", icon_deck);
//            getScenarioIcon("", icon_deck2);
//            isPorcentage = true;
//        }
//    }



}
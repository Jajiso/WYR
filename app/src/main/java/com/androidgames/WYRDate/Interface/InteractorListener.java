package com.androidgames.WYRDate.Interface;

import com.androidgames.WYRDate.repository.Entity.Scenario;

import java.util.ArrayList;

public interface InteractorListener {

    interface scenarioListListener {
        void onSuccess(ArrayList<Scenario> list);
        void onError();
    }

    interface PercentageListener {
        void onSuccess();
    }

    interface rewardedAdListener {
        void onSuccess(Scenario scenario);
        void onError();
    }
}

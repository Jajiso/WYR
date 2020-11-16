package com.androidgames.WYRDate.Interface;

import com.androidgames.WYRDate.repository.Entity.Scenario;

import java.util.Map;

public interface CallbacksDB {

    interface onCallbackJson {
        void onCallback(String json);
        void onError();
    }

    interface onCallbackMap {
        void onCallback(Map<String, Object> map);
        void onError();
    }

    interface onCallbackScenarioAd {
        void onCallback(Scenario scenario);
        void onError();
    }
}

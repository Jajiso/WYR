package com.androidgames.WYRDate.interactor;

import android.content.Context;

import com.android.billingclient.api.Purchase;
import com.androidgames.WYRDate.Interface.CallbacksDB;
import com.androidgames.WYRDate.Interface.InteractorListener;
import com.androidgames.WYRDate.repository.Entity.Scenario;
import com.androidgames.WYRDate.repository.FireBaseManager;
import com.androidgames.WYRDate.repository.JSONManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityInteractor {

    private final ArrayList<Scenario> basicList = new ArrayList<>();
    private final ArrayList<Scenario> hotList = new ArrayList<>();
    private final ArrayList<Scenario> fantasyList = new ArrayList<>();
    private final Map<String, Object> percentageMap = new HashMap<>();

    public ArrayList<Scenario> getBasicDeck(Context context) {
        if (basicList.size() == 0) {
            String jsonFromAssets = JSONManager.getJsonFromAssets(context);
            String basicArray = JSONManager.getJsonArrayFromJsonObject(jsonFromAssets, "basic");
            List<String> objJSON = JSONManager.getJsonObjectsFromJsonArray(basicArray);
            for (String scenarioJson : objJSON) {
                Scenario scenario = JSONManager.getScenarioFromJsonObject(scenarioJson);
                basicList.add(scenario);
            }
        }
        return basicList;
    }

    public void getHotDeck(final InteractorListener.scenarioListListener listener) {
        if (hotList.size() == 0) {
            FireBaseManager.setPremiumDeckOnCallback("hot", new CallbacksDB.onCallbackJson() {
                @Override
                public void onCallback(String json) {
                    String jsonArray = JSONManager.getJsonArrayFromJsonObject(json, "hot");
                    List<String> objJSON = JSONManager.getJsonObjectsFromJsonArray(jsonArray);
                    for (String scenarioJson : objJSON) {
                        Scenario scenario = JSONManager.getScenarioFromJsonObject(scenarioJson);
                        hotList.add(scenario);
                    }
                    listener.onSuccess(hotList);
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            });
        } else {
            listener.onSuccess(hotList);
        }
    }

    public void getFantasyDeck(final InteractorListener.scenarioListListener listener) {
        if (fantasyList.size() == 0) {
            FireBaseManager.setPremiumDeckOnCallback("fantasy", new CallbacksDB.onCallbackJson() {
                @Override
                public void onCallback(String json) {
                    String jsonArray = JSONManager.getJsonArrayFromJsonObject(json, "fantasy");
                    List<String> objJSON = JSONManager.getJsonObjectsFromJsonArray(jsonArray);
                    for (String scenarioJson : objJSON) {
                        Scenario scenario = JSONManager.getScenarioFromJsonObject(scenarioJson);
                        fantasyList.add(scenario);
                    }
                    listener.onSuccess(fantasyList);
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            });
        } else {
            listener.onSuccess(fantasyList);
        }
    }

    public void getRewardedAdScenario(final InteractorListener.rewardedAdListener listener) {
        FireBaseManager.getRewardedAdScenario(new CallbacksDB.onCallbackScenarioAd() {
            @Override
            public void onCallback(Scenario scenario) {
                listener.onSuccess(scenario);
            }
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }

    public void setOnOptionsPercentage(final InteractorListener.PercentageListener listener) {
        if (percentageMap.isEmpty()) {
            FireBaseManager.getMapClicksNumbersOptions(new CallbacksDB.onCallbackMap() {
                @Override
                public void onCallback(Map<String, Object> map) {
                    percentageMap.putAll(map);
                    listener.onSuccess();
                }
                @Override
                public void onError() {
                }
            });
        } else {
            listener.onSuccess();
        }
    }

    public int[] getOptionsPercentage(Map<String, Object> sessionClicks, String id) {
        if (!sessionClicks.isEmpty()) {
            int[] optionsParameterMap = getClicksNumberInScenario(sessionClicks, id);
            int[] optionsPercentageMap = getClicksNumberInScenario(percentageMap, id);
            int[] percentages = new int[2];
            for (int i = 0; i <optionsParameterMap.length; i++) {
                percentages[i] = optionsParameterMap[i]+optionsPercentageMap[i];
            }
            if ( (percentages[0] + percentages[1]) != 0) {
                int total = percentages[0] + percentages[1];
                percentages[0] = Math.round( (float) (percentages[0]*100)/total);
                percentages[1] = Math.round( (float) (percentages[1]*100)/total);
                return percentages;
            }
            return new int[2];
        }
        return new int[2];
    }

    public int[] getClicksNumberInScenario(Map<String, Object> map, String id) {
        try {
            String jsonMap = map.get(id).toString();
            JSONObject clicks = JSONManager.getJsonObjectObj(jsonMap);
            int[] clicksNumberInOptions = new int[2];
            clicksNumberInOptions[0] = clicks.getInt("clicksOptionA");
            clicksNumberInOptions[1] = clicks.getInt("clicksOptionB");
            return clicksNumberInOptions;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Integer> setClicksOptionsInScenario(int[] clicks) {
        Map<String, Integer> map = new HashMap<>();
        map.put("clicksOptionA", clicks[0]);
        map.put("clicksOptionB", clicks[1]);
        return map;
    }

    public void updateClicksMap(Map<String, Object> sessionMap) {
        if (!sessionMap.isEmpty()) {
            FireBaseManager.updateMapPercentagesToDB(sessionMap);
        }
    }

    public boolean isProductOwned(String sku, List<Purchase> purchases) {
        boolean isProductOwned = false;
        if (purchases!=null ) {
            if (!purchases.isEmpty()){
                for (Purchase purchasedItem: purchases) {
                    if (purchasedItem.getSku().equals(sku)){
                        isProductOwned = true;
                        break;
                    }
                }
            }
        }
        return isProductOwned;
    }

    public boolean HotDeckIsEmpty() {
        return hotList.isEmpty();
    }

    public boolean FantasyDeckIsEmpty() {
        return fantasyList.isEmpty();
    }

}

package com.campfiregames.WYRDate.repository;

import androidx.annotation.NonNull;

import com.campfiregames.WYRDate.Interface.CallbacksDB;
import com.campfiregames.WYRDate.repository.Entity.Scenario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseManager {

    public static void setPremiumDeckOnCallback(final String category, final CallbacksDB.onCallbackJson callback) {
        FirebaseFirestore.getInstance().collection("premiumDecks")
                .document(category)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Object obj = documentSnapshot.getData();
                        String json = JSONManager.setObjectToJson(obj);
                        callback.onCallback(json);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                });
    }

    public static void getRewardedAdScenario(final CallbacksDB.onCallbackScenarioAd callback) {
        FirebaseFirestore.getInstance().collection("premiumDecks")
                .document("rewardedAd")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<Object> objList = (ArrayList<Object>) documentSnapshot.getData().get("rewardedAd");
                            Object obj = objList.get(UtilsTools.generateRandomNumber(objList.size() - 1) );
                            String json = JSONManager.setObjectToJson(obj);
                            Scenario s = JSONManager.getScenarioFromJsonObject(json);
                            callback.onCallback(s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                });
    }

    public static void getMapClicksNumbersOptions(final CallbacksDB.onCallbackMap callback) {
        FirebaseFirestore.getInstance().collection("percentage")
                .document("percentages")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callback.onCallback(documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                });
    }

    public static void updateMapPercentagesToDB(final Map<String, Object> data) {
        if (!data.isEmpty()) {
            getMapClicksNumbersOptions(new CallbacksDB.onCallbackMap() {
                @Override
                public void onCallback(Map<String, Object> map) {
                    FirebaseFirestore.getInstance().collection("percentage")
                            .document("percentages")
                            .update(addClicks(map, data));
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    private static Map<String, Object> addClicks(Map<String, Object> mapDB, Map<String, Object> mapLocal) {
        Map<String, Object> result = new HashMap<>();
        if (!(mapDB.isEmpty() && mapLocal.isEmpty())) {
            for (Map.Entry<String, Object> entry : mapLocal.entrySet()) {
                String key = entry.getKey();
                String valueLocal = entry.getValue().toString();

                int[] clicksFromLocal = getClicksValue(valueLocal);

                String valueDB = mapDB.get(key).toString();
                int[] clicksFromDB = getClicksValue(valueDB);

                clicksFromLocal[0] = clicksFromDB[0] + clicksFromLocal[0];
                clicksFromLocal[1] = clicksFromDB[1] + clicksFromLocal[1];

                Map<String, Object> clicks = new HashMap<>();
                clicks.put("clicksOptionA", clicksFromLocal[0]);
                clicks.put("clicksOptionB", clicksFromLocal[1]);

                result.put(key, clicks);
            }
        }
        return result;
    }

    private static int[] getClicksValue(String jsonValue) {
        try {
            int[] clicksNumberInOptions = new int[2];
            clicksNumberInOptions[0] = JSONManager.getJsonObjectObj(jsonValue).getInt("clicksOptionA");
            clicksNumberInOptions[1] = JSONManager.getJsonObjectObj(jsonValue).getInt("clicksOptionB");
            return clicksNumberInOptions;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

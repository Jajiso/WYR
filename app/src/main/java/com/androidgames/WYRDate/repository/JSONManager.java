package com.androidgames.WYRDate.repository;

import android.content.Context;

import com.androidgames.WYRDate.repository.Entity.Scenario;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONManager {

    private static final String ASSETS_JSON_FILE_NAME = "decksDB.json";
    private static final Gson GSON = new Gson();

    public static JSONObject getJsonObjectObj(String jsonObject) {
        try {
            JSONObject object = new JSONObject(jsonObject);
            return object;
        }catch (JSONException err) {
            err.getMessage();
            return null;
        }
    }

    public static JSONArray getJsonArrayObj(String jsonArray) {
        try {
            JSONArray array = new JSONArray(jsonArray);
            return array;
        }catch (JSONException err) {
            err.getMessage();
            return null;
        }
    }

    public static String getJsonArrayFromJsonObject(String jsonObject, String arrayKey) {
        try {
            JSONArray array = new JSONObject(jsonObject).getJSONArray(arrayKey);
            return array.toString();

        }catch (JSONException err) {
            err.getMessage();
            return "";
        }
    }

    public static List<String> getJsonObjectsFromJsonArray(String jsonArray) {
        try {
            List<String> objList = new ArrayList<>();
            JSONArray array = new JSONArray(jsonArray);
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    String JSONObject = array.getString(i);
                    if (!JSONObject.equals("{}")) {
                        objList.add(JSONObject);
                    }
                }
            }
            return objList;
        }catch (JSONException err) {
            err.getMessage();
            return null;
        }
    }

    public static String setObjectToJson(Object obj){
        String json = GSON.toJson(obj);
        return json;
    }

    public static Scenario getScenarioFromJsonObject(String jsonObject){
        Scenario scenario = GSON.fromJson(jsonObject, Scenario.class);
        return scenario;
    }

    public static String getJsonFromAssets(Context context){
        String json = getJsonFromAssets(context, ASSETS_JSON_FILE_NAME);
        return json;
    }

    private static String getJsonFromAssets(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

}

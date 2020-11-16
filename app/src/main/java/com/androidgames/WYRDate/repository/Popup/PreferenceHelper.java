package com.androidgames.WYRDate.repository.Popup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class PreferenceHelper {

    private static final String PREF_FILE_NAME = "pop_up_pref_file";

    private static final String PREF_KEY_LAUNCH_SESSION = "launch_session";
    private static final String PREF_KEY_CURRENT_LAUNCH_SESSION = "current_launch_session";


    private static final String PREF_KEY_ACTION_TRIGGER_COUNT = "action_trigger_count";
    private static final String PREF_KEY_CURRENT_ACTION_TRIGGER_COUNT = "current_action_trigger_count";


    private static final String PREF_KEY_IS_AGREE_SHOW_RATING_DIALOG = "is_agree_show_rating_dialog";

    private PreferenceHelper() {}

    static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    static Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }

    static void resetPreferences(Context context){
        getPreferencesEditor(context).clear().apply();
    }

    static int getLaunchSession(Context context){
        return getPreferences(context).getInt(PREF_KEY_LAUNCH_SESSION, 0);
    }

    static void setLaunchSession(Context context, int launchSession){
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_LAUNCH_SESSION, launchSession);
        editor.apply();
    }

    static int getCurrentLaunchSession(Context context){
        return getPreferences(context).getInt(PREF_KEY_CURRENT_LAUNCH_SESSION, 0);
    }

    static void setCurrentLaunchSession(Context context, int currentLaunch){
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_CURRENT_LAUNCH_SESSION, currentLaunch);
        editor.apply();
    }

    static void incrementCurrentLaunchSession(Context context){
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_CURRENT_LAUNCH_SESSION, getCurrentLaunchSession(context)+1);
        editor.apply();
    }

    static int getActionTriggerCount(Context context){
        return getPreferences(context).getInt(PREF_KEY_ACTION_TRIGGER_COUNT, 0);
    }

    static void setActionTriggerCount(Context context, int triggerCount){
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_ACTION_TRIGGER_COUNT, triggerCount);
        editor.apply();
    }

    static int getCurrentActionTriggerCount(Context context){
        return getPreferences(context).getInt(PREF_KEY_CURRENT_ACTION_TRIGGER_COUNT, 0);
    }

    static void setCurrentActionTriggerCount(Context context, int currentTriggerCount){
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_CURRENT_ACTION_TRIGGER_COUNT, currentTriggerCount);
        editor.apply();
    }

    static boolean getIsAgreeShowRatingDialog(Context context){
        return getPreferences(context).getBoolean(PREF_KEY_IS_AGREE_SHOW_RATING_DIALOG, true);
    }

    static void setIsAgreeShowRatingDialog(Context context, boolean isAgree){
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_RATING_DIALOG, isAgree);
        editor.apply();
    }

}
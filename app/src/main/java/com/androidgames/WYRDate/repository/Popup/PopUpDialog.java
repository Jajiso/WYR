package com.campfiregames.WYRDate.repository.Popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import static com.campfiregames.WYRDate.repository.Popup.IntentHelper.createIntentForGooglePlay;
import static com.campfiregames.WYRDate.repository.Popup.PreferenceHelper.*;

public final class PopUpDialog {

    private final Context context;
    private Dialog dialog;
    private final PopUpParams params = new PopUpParams();

    private PopUpDialog(Context context) {
        this.context = context;
    }

    public static boolean shouldDhowRatingDialog(Context context) {
        if (PreferenceHelper.getIsAgreeShowRatingDialog(context)) {
            if (PreferenceHelper.getCurrentLaunchSession(context) >= PreferenceHelper.getLaunchSession(context)) {
                int currentCount = PreferenceHelper.getCurrentActionTriggerCount(context) + 1;
                if (currentCount >= PreferenceHelper.getActionTriggerCount(context)) {
                    return true;
                }
                PreferenceHelper.setCurrentActionTriggerCount(context, currentCount);
                return false;
            }
            return false;
        }
        return false;
    }

    public static void incrementSessionToShowRatingDialogIfAllowed(Context context) {
        if (PreferenceHelper.getIsAgreeShowRatingDialog(context)) {
            incrementCurrentLaunchSession(context);
            setCurrentActionTriggerCount(context, 0);
        }
    }

    public static void resetPreferences(Context context) {
        PreferenceHelper.resetPreferences(context);
    }

    private int getId(String nameId) {
        return context.getResources().getIdentifier(nameId, "id", context.getPackageName());
        /*
         * Way to use this method - view.findViewById(getId("BUTTON_NAME"))
        */
    }

    public void showDialog() {
        if (params.isRateOurAppDialog()) {
            shouldShowRatingDialog();
        } else {
            showCustomDialog();
        }
    }

    private void shouldShowRatingDialog() {
        if (shouldDhowRatingDialog(context)) {
            PreferenceHelper.setCurrentLaunchSession(context, 0);
            PreferenceHelper.setCurrentActionTriggerCount(context, 0);
            showRatingDialog();
        }
    }

    private void showCustomDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(params.getLayout());

        dialog = builder.create();
        dialog.show();
    }

    private void showRatingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(params.getLayout());


        Button positive = params.getLayout().findViewById(params.getIdPositive());
        Button negative = params.getLayout().findViewById(params.getIdNegative());


        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intentToAppstore = createIntentForGooglePlay(context);
                context.startActivity(intentToAppstore);
                setIsAgreeShowRatingDialog(context, false);
                dialog.dismiss();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsAgreeShowRatingDialog(context, false);
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    public static class Builder {

        private final PopUpDialog mPoUpDialog;

        public Builder(Context context) {
            this.mPoUpDialog = new PopUpDialog(context);
        }

        public Builder setLayout(int idLayout) {
            LayoutInflater inflater = (LayoutInflater) this.mPoUpDialog.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mPoUpDialog.params.setLayout(inflater.inflate(idLayout, null));
            return this;
        }

        public Builder IsRateOurAppDialog(boolean isRateOurApp) {
            this.mPoUpDialog.params.isRateOurAppDialog(isRateOurApp);
            return this;
        }

        public Builder setIdButtonPositive(int id) {
            this.mPoUpDialog.params.setIdPositive(id);
            return this;
        }

        public Builder setIdButtonNegative(int id) {
            this.mPoUpDialog.params.setIdNegative(id);
            return this;
        }

        public Builder setActionTriggerCount(int actionTriggerCount) {
            PreferenceHelper.setActionTriggerCount(mPoUpDialog.context, actionTriggerCount);
            return this;
        }

        public Builder setSessionToLaunch(int sessionToLaunch) {
            PreferenceHelper.setLaunchSession(mPoUpDialog.context, sessionToLaunch);
            return this;
        }

        public PopUpDialog build() {
            return mPoUpDialog;
        }
    }
}

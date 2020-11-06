package com.campfiregames.WYRDate.repository.Popup;

import android.view.View;

public final class PopUpParams {

    private View layout;
    private boolean isRateOurAppDialog = false;
    private int idPositive,
                idNegative;

    public PopUpParams(){}

    public void setLayout(View layout) {
        this.layout = layout;
    }

    public View getLayout() {
        return this.layout;
    }

    public void isRateOurAppDialog(boolean isRateOurApp) {
        this.isRateOurAppDialog = isRateOurApp;
    }

    public boolean isRateOurAppDialog() {
        return this.isRateOurAppDialog;
    }

    public int getIdPositive() {
        return idPositive;
    }

    public void setIdPositive(int idPositive) {
        this.idPositive = idPositive;
    }

    public int getIdNegative() {
        return idNegative;
    }

    public void setIdNegative(int idNegative) {
        this.idNegative = idNegative;
    }
}

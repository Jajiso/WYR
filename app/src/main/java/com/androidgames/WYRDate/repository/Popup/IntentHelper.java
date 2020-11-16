package com.campfiregames.WYRDate.repository.Popup;

import android.content.Context;
import android.content.Intent;

import static com.campfiregames.WYRDate.repository.Popup.UriHelper.getGooglePlay;
import static com.campfiregames.WYRDate.repository.Popup.UriHelper.isPackageExists;

final class IntentHelper {

    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    private IntentHelper() {
    }

    static Intent createIntentForGooglePlay(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW, getGooglePlay(packageName));
        if (isPackageExists(context, GOOGLE_PLAY_PACKAGE_NAME)) {
            intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
        }
        return intent;
    }
}

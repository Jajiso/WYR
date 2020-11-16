package com.campfiregames.WYRDate.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

public abstract class BasePresenter {

    protected BasePresenter(){}

    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
    }

    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
    }

    @CallSuper
    public void onStart() {
    }

    @CallSuper
    public void onPause() {
    }

    @CallSuper
    public void onResume() {
    }
    @CallSuper
    public void onStop(){
    }

    @CallSuper
    public void onDestroy() {
    }

    @CallSuper
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    }
}

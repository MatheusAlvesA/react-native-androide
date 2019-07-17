package com.rnandroid.admob;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.views.view.ReactViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

class Banner extends ReactViewGroup {
  ReactApplicationContext context = null;
  boolean initializated = false;

  Banner(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;
    this.initializeAdMob();
  }

  public void setInitializated(boolean initializated) {
    this.initializated = initializated;
  }

  private void initializeAdMob() {
    final Banner that = this;
    MobileAds.initialize(this.context, new OnInitializationCompleteListener() {
      @Override
      public void onInitializationComplete(InitializationStatus initializationStatus) {
        that.setInitializated(true);
      }
    });
  }

}

package com.rnandroid.admob;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.view.ReactViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;

public class BannerView extends ReactViewGroup {
  private ThemedReactContext context = null;
  private boolean initializated = false;
  private AdView mAdView = null;

  BannerView(ThemedReactContext reactContext) {
    super(reactContext);
    this.context = reactContext;
    this.initializeAdMob();
  }

  public void setInitializated(boolean initializated) {
    this.initializated = initializated;
  }

  public void createAdView() {
    if (this.mAdView != null) this.mAdView.destroy();

    AdView adView = new AdView(this.context);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); //DEBUG id

    this.addView(adView);
    this.mAdView = adView;
  }

  private void initializeAdMob() {
    MobileAds.initialize(this.context, new OnInitializationCompleteListener() {
      @Override
      public void onInitializationComplete(InitializationStatus initializationStatus) {
        setInitializated(true);
        createAdView();
        loadAd();
      }
    });
  }

  public void loadAd() {
    AdRequest adRequest = new AdRequest.Builder().build();
    this.mAdView.loadAd(adRequest);
  }

}

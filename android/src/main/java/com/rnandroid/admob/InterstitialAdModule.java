package com.rnandroid.admob;

import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdListener;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class InterstitialAdModule extends ReactContextBaseJavaModule {

  private InterstitialAd mInterstitialAd;
  private Promise mRequestAdPromise;

  public InterstitialAdModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.mInterstitialAd = new InterstitialAd(reactContext);

    final InterstitialAdModule that = this;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        that.mInterstitialAd.setAdListener(that.getAdEventsListener());
      }
    });
  }

  @Override
  public String getName() {
      return "InterstitialAdModule";
  }

  @ReactMethod
  public void setAdUnitID(String adUnitID, Promise p) {
      // Can set only one time
      if (this.mInterstitialAd.getAdUnitId() == null) {
          this.mInterstitialAd.setAdUnitId(adUnitID);
      }
      p.resolve(true);
  }

  @ReactMethod
  public void requestAd(final Promise promise) {
    // Running on the main UI Thread
    final InterstitialAdModule that = this;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {

        if (that.mInterstitialAd.isLoaded() || that.mInterstitialAd.isLoading()) {
            promise.reject("ERROR", "Ad is already loading or loaded.");
        } else {
            that.mRequestAdPromise = promise;
            that.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

      }
    });
  }

  @ReactMethod
  public void showAd(final Promise promise) {
    // Running on the main UI Thread
    final InterstitialAdModule that = this;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {

        if (that.mInterstitialAd.isLoaded()) {
            that.mInterstitialAd.show();
            promise.resolve(true);
        } else {
            promise.reject("ERROR", "Ad is not ready.");
        }

      }
    });
  }

  private AdListener getAdEventsListener() {
    final InterstitialAdModule that = this;
    return (new AdListener() {

        @Override
        public void onAdFailedToLoad(int errorCode) {
          switch (errorCode) {
              case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                  that.mRequestAdPromise.reject("ERROR", "An invalid response was received from the ad server.");
                  break;
              case AdRequest.ERROR_CODE_INVALID_REQUEST:
                  that.mRequestAdPromise.reject("ERROR", "The ad unit ID was incorrect.");
                  break;
              case AdRequest.ERROR_CODE_NETWORK_ERROR:
                  that.mRequestAdPromise.reject("ERROR", "The ad request was unsuccessful due to network connectivity.");
                  break;
              case AdRequest.ERROR_CODE_NO_FILL:
                  that.mRequestAdPromise.reject("ERROR", "The ad request was successful, but no ad was returned due to lack of ad inventory.");
                  break;
              default:
                  that.mRequestAdPromise.reject("ERROR", "Unknown error");
                  break;
          }
        }

        @Override
        public void onAdLeftApplication() {
            /* EMPTY */
        }

        @Override
        public void onAdLoaded() {
            //TODO
            that.mRequestAdPromise.resolve(true);
        }

        @Override
        public void onAdOpened() {
            //TODO
        }

        @Override
        public void onAdClosed() {
            //TODO
        }

      });
  }

}

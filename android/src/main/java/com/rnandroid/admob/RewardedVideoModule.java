package com.rnandroid.admob;

import android.os.Handler;
import android.os.Looper;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.MobileAds;

public class RewardedVideoModule extends ReactContextBaseJavaModule implements RewardedVideoAdListener {

  ReactApplicationContext context;
  Promise mRequestAdPromise;
  RewardedVideoAd mRewardedVideoAd;
  String adUnitID;

  public RewardedVideoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;

    this.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this.context);
    this.setListener();
  }

  @Override
  public String getName() {
      return "RewardedVideoModule";
  }

  private void setListener() {
    // Running on the main UI Thread
    final RewardedVideoModule that = this;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        that.mRewardedVideoAd.setRewardedVideoAdListener(that);
      }
    });
  }

  @ReactMethod
  public void requestAd(final Promise promise) {
    // Running on the main UI Thread
    final RewardedVideoModule that = this;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {

        if (that.mRewardedVideoAd.isLoaded()) {
            promise.resolve(true);
        } else {
          that.mRewardedVideoAd.loadAd(that.adUnitID, new AdRequest.Builder().build());
          that.mRequestAdPromise = promise;
        }

      }
    });
  }

  @ReactMethod
  public void showAd(final Promise promise) {
      // Running on the main UI Thread
      final RewardedVideoModule that = this;
      new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override
          public void run() {

              if (that.mRewardedVideoAd.isLoaded()) {
                  that.mRewardedVideoAd.show();
                  promise.resolve(true);
              } else {
                  promise.reject("ERROR", "Ad is not ready.");
              }

          }
      });
  }

  @ReactMethod
  public void setAdUnitID(String adUnitID) {
      this.adUnitID = adUnitID;
  }


  //--------------------------
  //      Event listeners
  //--------------------------

  @Override
  public void onRewarded(RewardItem reward) {
    WritableNativeMap event = new WritableNativeMap();

    event.putString("id", this.adUnitID);
    event.putString("type", reward.getType());
    event.putInt("amount", reward.getAmount());

    this.context
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit("AdMob_RewardedVideoReward", event);
  }

  @Override
  public void onRewardedVideoAdLeftApplication() {
    /* EMPTY */
  }

  @Override
  public void onRewardedVideoAdFailedToLoad(int errorCode) {
    switch (errorCode) {
        case AdRequest.ERROR_CODE_INTERNAL_ERROR:
            this.mRequestAdPromise.reject("ERROR", "An invalid response was received from the ad server.");
            break;
        case AdRequest.ERROR_CODE_INVALID_REQUEST:
            this.mRequestAdPromise.reject("ERROR", "The ad unit ID was incorrect.");
            break;
        case AdRequest.ERROR_CODE_NETWORK_ERROR:
            this.mRequestAdPromise.reject("ERROR", "The ad request was unsuccessful due to network connectivity.");
            break;
        case AdRequest.ERROR_CODE_NO_FILL:
            this.mRequestAdPromise.reject("ERROR", "The ad request was successful, but no ad was returned due to lack of ad inventory.");
            break;
        default:
            this.mRequestAdPromise.reject("ERROR", "Unknown error");
            break;
    }
  }

  @Override
  public void onRewardedVideoAdLoaded() {
     this.mRequestAdPromise.resolve(true);
  }

  @Override
  public void onRewardedVideoAdOpened() {
    WritableNativeMap event = new WritableNativeMap();
    event.putString("id", this.adUnitID);
    this.context
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit("AdMob_RewardedVideoOpened", event);
  }

  @Override
  public void onRewardedVideoAdClosed() {
    WritableNativeMap event = new WritableNativeMap();
    event.putString("id", this.adUnitID);
    this.context
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit("AdMob_RewardedVideoClosed", event);
  }

  @Override
  public void onRewardedVideoStarted() {
    WritableNativeMap event = new WritableNativeMap();
    event.putString("id", this.adUnitID);
    this.context
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit("AdMob_RewardedVideoStarted", event);
  }

  @Override
  public void onRewardedVideoCompleted() {
    WritableNativeMap event = new WritableNativeMap();
    event.putString("id", this.adUnitID);
    this.context
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit("AdMob_RewardedVideoCompleted", event);
  }
}

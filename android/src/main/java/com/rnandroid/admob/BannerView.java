package com.rnandroid.admob;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.PixelUtil;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;

public class BannerView extends ReactViewGroup {
  private ThemedReactContext context = null;
  private AdView mAdView = null;

  public static String EVENT_AD_SIZE_CHANGED = "onSizeChange";
  public static String EVENT_AD_LOAD_FAIL = "onFailedToLoad";
  public static String EVENT_AD_LOADED = "onLoad";
  public static String EVENT_AD_CLICKED = "onClick";
  public static String EVENT_AD_OPENED = "onOpen";
  public static String EVENT_AD_CLOSED = "onClose";

  BannerView(ThemedReactContext reactContext) {
    super(reactContext);
    this.context = reactContext;
    MobileAds.initialize(this.context);
    this.createAdView();
  }

  public void createAdView() {
    if (this.mAdView != null) this.mAdView.destroy();

    AdView adView = new AdView(this.context);
    adView.setAdListener(this.getAdMobEventListener());

    this.addView(adView);
    this.mAdView = adView;
  }

  public void setAdSize(AdSize size) {
    this.mAdView.setAdSize(size);
    this.loadIfIsReady();
  }

  public void setAdUnitID(String id) {
    this.mAdView.setAdUnitId(id);
    this.loadIfIsReady();
  }

  public boolean loadIfIsReady() {
    if(this.mAdView == null) {
      return false;
    }
    String uId = this.mAdView.getAdUnitId();
    if(this.mAdView.getAdSize() == null || uId == null || uId.equals("")) {
      return false;
    }

    this.mAdView.loadAd(new AdRequest.Builder().build());
    return true;
  }

  private void sendOnSizeChangeEvent() {
      int width = 0;
      int height = 0;
      WritableNativeMap event = new WritableNativeMap();
      AdSize adSize = this.mAdView.getAdSize();
      if (adSize.equals(AdSize.SMART_BANNER)) {
          width  = (int) PixelUtil.toDIPFromPixel(adSize.getWidthInPixels(this.context));
          height = (int) PixelUtil.toDIPFromPixel(adSize.getHeightInPixels(this.context));
      } else {
          width  = adSize.getWidth();
          height = adSize.getHeight();
      }
      event.putInt("width", width);
      event.putInt("height", height);

      this.context.getJSModule(RCTEventEmitter.class).receiveEvent(
                      getId(),
                      EVENT_AD_SIZE_CHANGED,
                      event);
  }

  public AdListener getAdMobEventListener() {
    final BannerView that = this;

    return (new AdListener() {
        @Override
        public void onAdLoaded() {
          int width = that.mAdView.getAdSize().getWidthInPixels(that.context);
          int height = that.mAdView.getAdSize().getHeightInPixels(that.context);
          int left = that.mAdView.getLeft();
          int top = that.mAdView.getTop();
          that.mAdView.measure(width, height);
          that.mAdView.layout(left, top, left + width, top + height);

          sendOnSizeChangeEvent();
          that.context.getJSModule(RCTEventEmitter.class).receiveEvent(
                          getId(),
                          EVENT_AD_LOADED,
                          null);
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
          WritableNativeMap event = new WritableNativeMap();
          switch (errorCode) {
              case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                  event.putString("message", "An invalid response was received from the ad server.");
                  break;
              case AdRequest.ERROR_CODE_INVALID_REQUEST:
                  event.putString("message", "The ad unit ID was incorrect.");
                  break;
              case AdRequest.ERROR_CODE_NETWORK_ERROR:
                  event.putString("message", "The ad request was unsuccessful due to network connectivity.");
                  break;
              case AdRequest.ERROR_CODE_NO_FILL:
                  event.putString("message", "The ad request was successful, but no ad was returned due to lack of ad inventory.");
                  break;
              default:
                  event.putString("message", "Unknown error");
                  break;
          }

          that.context.getJSModule(RCTEventEmitter.class).receiveEvent(
                          getId(),
                          EVENT_AD_LOAD_FAIL,
                          event);
        }

        @Override
        public void onAdOpened() {
          that.context.getJSModule(RCTEventEmitter.class).receiveEvent(
                          getId(),
                          EVENT_AD_OPENED,
                          null);
        }

        @Override
        public void onAdClicked() {
          that.context.getJSModule(RCTEventEmitter.class).receiveEvent(
                          getId(),
                          EVENT_AD_CLICKED,
                          null);
        }

        @Override
        public void onAdLeftApplication() {
            /* EMPTY */
        }

        @Override
        public void onAdClosed() {
          that.context.getJSModule(RCTEventEmitter.class).receiveEvent(
                          getId(),
                          EVENT_AD_CLOSED,
                          null);
        }
    });
  }

}

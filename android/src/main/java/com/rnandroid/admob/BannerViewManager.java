package com.rnandroid.admob;

import android.view.View;

import com.facebook.react.uimanager.ViewGroupManager;

class BannerViewManager extends ViewGroupManager<BannerView> {
  @Override
  public String getName() {
    return "BannerViewManager";
  }

  @Override
  protected BannerView createViewInstance(ThemedReactContext themedReactContext) {
    return new BannerView(themedReactContext);
  }

  @Override
  public void addView(BannerView parent, View child, int index) {
    throw new RuntimeException("AdMobView cannot have subviews");
  }

}

package com.rnandroid.admob;

import android.view.View;

import com.facebook.react.uimanager.ViewGroupManager;

class BannerViewManager extends ViewGroupManager<Banner> {
  @Override
  public String getName() {
    return "BannerViewManager";
  }

  @Override
  protected Banner createViewInstance(ThemedReactContext themedReactContext) {
    return new Banner(themedReactContext);
  }

  @Override
  public void addView(Banner parent, View child, int index) {
    throw new RuntimeException("AdMobView cannot have subviews");
  }

}

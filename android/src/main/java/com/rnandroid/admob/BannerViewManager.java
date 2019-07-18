package com.rnandroid.admob;

import android.view.View;
import androidx.annotation.Nullable;

import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.common.MapBuilder;
//import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class BannerViewManager extends ViewGroupManager<BannerView> {
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

  @Override
  @Nullable
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
      MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
      String[] events = {
          BannerView.EVENT_AD_SIZE_CHANGED,
          BannerView.EVENT_AD_LOAD_FAIL
      };
      for (int i = 0; i < events.length; i++) {
          builder.put(events[i], MapBuilder.of("registrationName", events[i]));
      }
      return builder.build();
  }

}

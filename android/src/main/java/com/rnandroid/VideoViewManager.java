package com.rnandroid;

import android.widget.VideoView;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;
import java.util.HashMap;

public class VideoViewManager extends SimpleViewManager<VideoView> {

  @Override
  public String getName() {
    return "VideoViewManager";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();

    constants.put("TODO", 0);

    return constants;
  }


  @Override
  protected VideoView createViewInstance(ThemedReactContext reactContext) {
    return new VideoView(reactContext);
  }

  @ReactProp(name="url")
  public void setVideoPath(VideoView videoView, String urlPath) {
    Uri uri = Uri.parse(urlPath);
    videoView.setVideoURI(uri);
    videoView.start();
  }

}

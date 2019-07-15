package com.rnandroid;

import android.widget.VideoView;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;
import java.util.HashMap;

public class VideoViewManager extends SimpleViewManager<VideoView> {

  ReactContext context = null;

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
    this.context = reactContext;
    return new VideoView(reactContext);
  }

  @ReactProp(name="url")
  public void setVideoPath(VideoView videoView, String urlPath) {
    Uri uri = Uri.parse(urlPath);
    videoView.setVideoURI(uri);
  }

  @ReactProp(name="paused")
  public void setVideoPath(VideoView videoView, boolean paused) {
    if(paused) {
      videoView.pause();
    } else {
      videoView.start();
    }
  }

  @ReactProp(name="id")
  public void setVideoId(final VideoView videoView, final String id) {
    final ReactContext cx = this.context;

    new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          WritableNativeMap wm = new WritableNativeMap();
          wm.putString("id", id);
          wm.putInt("currentPosition", videoView.getCurrentPosition());
          wm.putInt("duration", videoView.getDuration());

          cx
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit("RNA_videoProgress", wm);

          try {
            Thread.currentThread().sleep(1000);
          } catch (InterruptedException e) {
            /* EMPTY */
          }
        }
      }
    }).start();
  }

}

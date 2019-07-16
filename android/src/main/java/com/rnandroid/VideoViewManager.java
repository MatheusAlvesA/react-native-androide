package com.rnandroid;

import android.media.MediaMetadataRetriever;
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

public class VideoViewManager extends SimpleViewManager<RNAVideoView> {

  ReactContext context = null;

  @Override
  public String getName() {
    return "VideoViewManager";
  }

  @Override
  protected RNAVideoView createViewInstance(ThemedReactContext reactContext) {
    this.context = reactContext;
    return new RNAVideoView(reactContext);
  }

  @ReactProp(name="url")
  public void setVideoPath(RNAVideoView videoView, String urlPath) {
    videoView.setVideoURI(Uri.parse(urlPath));

    this.updateDimensions(videoView);
    videoView.setIsCompleted(false);
  }

  @ReactProp(name="paused")
  public void setVideoPaused(RNAVideoView videoView, boolean paused) {
    if(paused) {
      videoView.pause();
    } else {
      videoView.start();
      videoView.setIsCompleted(false);
    }
  }

  @ReactProp(name="progress")
  public void setVideoProgress(RNAVideoView videoView, double percent) {
    if(percent > 1.0 || percent < 0.0) {
      return;
    }
    videoView.seekTo((int) (percent * videoView.getDuration()));
    if(percent < 1) {
      videoView.setIsCompleted(false);
    }
  }

  @ReactProp(name="id")
  public void setVideoId(final RNAVideoView videoView, final String id) {
    final VideoViewManager that = this;
    videoView.setVideoId(id);

    new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          WritableNativeMap wm = new WritableNativeMap();

          wm.putString("id", id);
          wm.putInt("currentPosition", videoView.getCurrentPosition());
          wm.putInt("duration", videoView.getDuration());
          wm.putBoolean("completed", videoView.isCompleted());

          that.context
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

  private void updateDimensions(RNAVideoView videoView) {
    WritableNativeMap wm = new WritableNativeMap();
    int width = -1;
    int height = -1;

    try {
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      Uri uri = videoView.getVideoUri();
      String url = uri.toString();

      if(url.startsWith("http") || url.startsWith("https")) {
        retriever.setDataSource(url, new HashMap<String, String>());
      } else {
        retriever.setDataSource(this.context, uri);
      }

      width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
      height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

      String metaRotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
      int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
      if(rotation == 90 || rotation == 270) {
        int tmp = width;
        width = height;
        height = tmp;
      }

      retriever.release();
    } catch(Exception e) {
      /* EMPTY */
    }

    wm.putString("id", videoView.getVideoId());
    wm.putInt("width", width);
    wm.putInt("height", height);
    this.context
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit("RNA_videoResolutionChanged", wm);
  }

}

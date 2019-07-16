package com.rnandroid;

import android.widget.VideoView;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
  Uri uri = null;
  int width = -1;
  int height = -1;
  boolean completed = false;

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
    this.uri = Uri.parse(urlPath);
    videoView.setVideoURI(this.uri);

    this.updateDimensions();
    this.completed = false;
  }

  @ReactProp(name="paused")
  public void setVideoPaused(VideoView videoView, boolean paused) {
    if(paused) {
      videoView.pause();
    } else {
      videoView.start();
      this.completed = false;
    }
  }

  @ReactProp(name="progress")
  public void setVideoProgress(VideoView videoView, double percent) {
    if(percent > 1.0 || percent < 0.0) {
      return;
    }
    videoView.seekTo((int) (percent * videoView.getDuration()));
    this.completed = false;
  }

  @ReactProp(name="id")
  public void setVideoId(final VideoView videoView, final String id) {
    final VideoViewManager that = this;

    new Thread(new Runnable() {
      @Override
      public void run() {
        int width = -1;
        int height = -1;

        while(true) {
          WritableNativeMap wm = new WritableNativeMap();

          wm.putString("id", id);
          wm.putInt("currentPosition", videoView.getCurrentPosition());
          wm.putInt("duration", videoView.getDuration());
          wm.putInt("width", that.width);
          wm.putInt("height", that.height);
          wm.putBoolean("completed", that.completed);

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

    videoView.setOnCompletionListener(new OnCompletionListener() {
      @Override
      public synchronized void onCompletion(MediaPlayer mp) {
        that.completed = true;
      }
    });
  }

  private void updateDimensions() {
    try {
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      retriever.setDataSource(this.context, this.uri);
      this.width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
      this.height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

      String metaRotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
      int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
      if(rotation == 90 || rotation == 270) {
        int tmp = this.width;
        this.width = this.height;
        this.height = tmp;
      }

      retriever.release();
    } catch(Exception e) {
      /* EMPTY */
    }
  }

}

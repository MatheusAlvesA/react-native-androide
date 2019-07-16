package com.rnandroid;

import android.net.Uri;
import android.widget.VideoView;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class RNAVideoView extends VideoView {
  String id = null;
  boolean completed = false;
  Uri uri = null;

  RNAVideoView(Context cx) {
    super(cx);
  }

  public void setVideoId(String id) {
    this.id = id;
    final RNAVideoView that = this;

    this.setOnCompletionListener(new OnCompletionListener() {
      @Override
      public synchronized void onCompletion(MediaPlayer mp) {
        that.setIsCompleted(true);
      }
    });
  }

  public String getVideoId() {
    return this.id;
  }

  public void setIsCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isCompleted() {
    return this.completed;
  }

  public void setVideoURI(Uri uri) {
    super.setVideoURI(uri);
    this.uri = uri;
  }

  public Uri getVideoUri() {
    return this.uri;
  }
}

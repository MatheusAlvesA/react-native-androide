package com.rnandroid;

import java.io.File;
import java.io.IOException;

import android.net.Uri;
import android.media.MediaPlayer;
import android.content.res.AssetFileDescriptor;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;


public class SoundModule extends ReactContextBaseJavaModule {

  MediaPlayer mediaPlayer;
  ReactApplicationContext context;

  public SoundModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;
  }

  @Override
  public String getName() {
    return "Sound";
  }

  @ReactMethod
  public void prepare(String source, Promise p) {
    try {
      this.prepareMediaPlayer(source);
      p.resolve(true);
    } catch (IOException e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void play(Promise p) {
    if(this.mediaPlayer == null) {
      p.reject("ERROR", "Media Player is not prepareted");
      return;
    }

    try {
      this.mediaPlayer.start();
      p.resolve(true);
    } catch (IllegalStateException e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void stop(Promise p) {
    if(this.mediaPlayer == null) {
      p.reject("ERROR", "Media Player is not prepareted");
      return;
    }

    try {
      this.mediaPlayer.stop();
      this.mediaPlayer.release();
      this.mediaPlayer = null;
      p.resolve(true);
    } catch (IllegalStateException e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void pause(Promise p) {
    if(this.mediaPlayer == null) {
      p.reject("ERROR", "Media Player is not prepareted");
      return;
    }

    try {
      this.mediaPlayer.pause();
      p.resolve(true);
    } catch (IllegalStateException e) {
      p.reject(e);
    }
  }

  private void prepareMediaPlayer(String source) throws IOException {
    if(this.mediaPlayer != null)
      this.mediaPlayer.release();

    int res = this.context.getResources()
                          .getIdentifier(
                                          this.removeFileExtension(source);,
                                          "raw",
                                          this.context.getPackageName()
                                        );
    if(res != 0) { // Is a resource
      this.mediaPlayer = new MediaPlayer();
      try (AssetFileDescriptor afd = context.getResources().openRawResourceFd(res)) {
        this.mediaPlayer.setDataSource(
                                  afd.getFileDescriptor(),
                                  afd.getStartOffset(),
                                  afd.getLength()
                                );
        this.mediaPlayer.prepare();
      }
      return;
    }

    // It's from the Internet
    if(source.startsWith("http://") || source.startsWith("https://")) {
      Uri uri = Uri.parse(source);
      this.mediaPlayer = MediaPlayer.create(this.context, uri);
      if(this.mediaPlayer == null)
        throw new IOException("Failed to open uri: ".concat(source));
      this.mediaPlayer.prepare();
    }

    File file = new File(source);
    if (file.exists()) { // It's from the file system
      this.mediaPlayer.setDataSource(source);
      this.mediaPlayer.prepare();
      return;
    }

    // Is undetermined
    throw new IOException("Failed to open uri: ".concat(source));
  }

  private String removeFileExtension(String source) {
    String[] splited = source.split("[.]");
    StringBuilder joined = new StringBuilder();

    for(int i = 0; i < splited.length-1; i++) {
      joined.append(splited[i]);
    }

    return joined.toString();
  }

}

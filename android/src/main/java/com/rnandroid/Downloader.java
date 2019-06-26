package com.rnandroid;

import android.app.DownloadManager;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.util.Map;
import java.util.HashMap;

public class Downloader extends ReactContextBaseJavaModule {

  DownloadManager dm;

  public Downloader(ReactApplicationContext reactContext) {
    super(reactContext);
    this.dm = (DownloadManager) reactContext.getSystemService(reactContext.DOWNLOAD_SERVICE);
  }

  @Override
  public String getName() {
    return "Downloader";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();

    constants.put("ok", this.dm != null);

    return constants;
  }

  @ReactMethod
  public void download(ReadableMap config, Promise p) {
    if(!config.hasKey("url") && config.getString("url") != null) {
      p.reject("ERROR", "The URL must be a valid String");
      return;
    }

    DownloadManager.Request req = null;
    try {
      req = new DownloadManager.Request(Uri.parse(config.getString("url")));
    } catch(Exception e) {
      p.reject(e);
      return;
    }

    try {
      this.setConfiguration(config, req);
    } catch(Exception e) {
      p.reject(e);
      return;
    }

    p.resolve( String.valueOf(this.dm.enqueue(req)) );
  }

  private void setConfiguration(ReadableMap config, DownloadManager.Request req) {
    if(config.hasKey("destination") && config.getString("destination") != null) {
      String dest = config.getString("destination");
      if(!dest.startsWith("file://")) {
        dest = "file://" + dest;
      }
      req.setDestinationUri( Uri.parse(dest) );
    }
    if(config.hasKey("title") && config.getString("title") != null) {
      req.setTitle(config.getString("title"));
    }
    if(config.hasKey("description") && config.getString("description") != null) {
      req.setDescription(config.getString("description"));
    }
    if(config.hasKey("hide") && config.getBoolean("hide")) {
      // requires android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
      req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
    }
    if(config.hasKey("headers") && config.getMap("headers") != null) {
      this.setHeaders(config.getMap("headers"), req);
    }

    req.setAllowedOverMetered(true); // Set if download is allowed on Mobile network
    req.setAllowedOverRoaming(true);// Set if download is allowed on roaming network
  }

  private void setHeaders(ReadableMap headers, DownloadManager.Request req) {
    ReadableMapKeySetIterator it = headers.keySetIterator();
    while(it.hasNextKey()) {
      String key = it.nextKey();
      String value = headers.getString(key);
      if(value != null) {
        req.addRequestHeader(key, value);
      }
    }
  }
}

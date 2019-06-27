package com.rnandroid;

import android.app.DownloadManager;
import android.net.Uri;
import android.database.Cursor;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

public class Downloader extends ReactContextBaseJavaModule {

  DownloadManager dm;
  ReactApplicationContext context;

  public Downloader(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;
    this.dm = (DownloadManager) reactContext.getSystemService(reactContext.DOWNLOAD_SERVICE);
  }

  @Override
  public String getName() {
    return "Downloader";
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

    Long id = this.dm.enqueue(req);
    p.resolve( String.valueOf(id) );
    this.watchDownload(id);
  }

  @ReactMethod
  public void cancel(String idString, Promise p) {
    long id = (long)0;
    try {
      id = Long.valueOf(idString);
    } catch(NumberFormatException e) {
      p.reject(e);
      return;
    }

    int n = this.dm.remove(id);
    if(n == 0) {
      p.resolve(false);
    } else {
      p.resolve(true);
    }
  }

  private void watchDownload(final Long id) {
    final Downloader that = this;

    new Thread(new Runnable() {
      @Override
      public void run() {
          boolean downloading = true;
          while(downloading) {
            WritableNativeMap wm = that.getDownloadMetadata(id);
            downloading = !wm.getBoolean("finished");
            that.context
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("downloadProgress", wm);

            try {
              Thread.currentThread().sleep(1000);
            } catch(InterruptedException e) {
              /* Empty */
            }
          }
      }
    }).start();
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
    req.setAllowedOverRoaming(true); // Set if download is allowed on roaming network
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

  private WritableNativeMap getDownloadMetadata(Long id) {
    DownloadManager.Query q = new DownloadManager.Query();
    q.setFilterById(id);
    Cursor cur = this.dm.query(q);
    cur.moveToFirst();

    WritableNativeMap wm = new WritableNativeMap();
    if(cur.getCount() > 0) {
      wm.putString("id", String.valueOf(id));
      wm.putDouble("progress", this.getDownloadProgress(cur));
      wm.putBoolean("finished", this.isDownloadFinished(cur));
      wm.putBoolean("error", this.isDownloadFailed(cur));
      wm.putString("filePath", this.getDownloadFilePath(cur));
    } else { // Empty Cursor, perhaps download is calceled
      wm.putString("id", String.valueOf(id));
      wm.putDouble("progress", 0.0);
      wm.putBoolean("finished", true);
      wm.putBoolean("error", true);
      wm.putString("filePath", null);
    }

    return wm;
  }

  private double getDownloadProgress(Cursor cursor) {
    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
    double progress = 0;
    if(bytes_total > 0) {
      progress = ((double) bytes_downloaded) / bytes_total;
    }
    return progress;
  }

  private boolean isDownloadFinished(Cursor cursor) {
    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
    if(status == DownloadManager.STATUS_PAUSED ||
      status == DownloadManager.STATUS_PENDING ||
      status == DownloadManager.STATUS_RUNNING
    ) {
      return false;
    }
    return true;
  }

  private boolean isDownloadFailed(Cursor cursor) {
    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
    if(status == DownloadManager.STATUS_FAILED) {
      return true;
    }
    return false;
  }

  private String getDownloadFilePath(Cursor cursor) {
    String downloadFilePath = null;
    String downloadFileLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
    if (downloadFileLocalUri != null) {
        File mFile = new File(Uri.parse(downloadFileLocalUri).getPath());
        downloadFilePath = mFile.getAbsolutePath();
    }
    return downloadFilePath;
  }
}

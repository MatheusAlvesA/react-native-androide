package com.rnandroid;

import android.app.Activity;
import android.view.WindowManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class KeepAwake extends ReactContextBaseJavaModule {

  WakeLock wakeLock = null;

  public KeepAwake(ReactApplicationContext reactContext) {
    super(reactContext);

    PowerManager pm = (PowerManager) reactContext.getSystemService(reactContext.POWER_SERVICE);
    this.wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
        "WakeLock::PartialCPU");
  }

  @Override
  public String getName() {
    return "KeepAwake";
  }

  @ReactMethod
  public void screen() {
    final Activity activity = getCurrentActivity();

    if(activity != null) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
      });
    }
  }

  @ReactMethod
  public void CPU(Promise p) {
    if(this.wakeLock == null) {
      p.reject("ERROR", "WakeLock is not initialized");
      return;
    }
    try {
      wakeLock.acquire();
      p.resolve(true);
    } catch(SecurityException e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void freeCPU(Promise p) {
    if(this.wakeLock == null) {
      p.reject("ERROR", "WakeLock is not initialized");
      return;
    }
    try {
      wakeLock.release();
      p.resolve(true);
    } catch(SecurityException e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void freeScreen() {
    final Activity activity = getCurrentActivity();

    if(activity != null) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          activity.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
      });
    }
  }

}

package com.rnandroid;

import android.os.Vibrator;
import android.os.Build;
import android.os.VibrationEffect;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class VibratorModule extends ReactContextBaseJavaModule {

  Vibrator vibrator;

  public VibratorModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.vibrator = (Vibrator) reactContext.getSystemService(reactContext.VIBRATOR_SERVICE);
  }

  @Override
  public String getName() {
    return "Vibrator";
  }

  // Using deprecated vibrate(int) only if the api leve of device is old
  @SuppressWarnings("deprecation")
  @ReactMethod
  public void vibrate(int time, int intensity, Promise promise) {
    if(this.vibrator == null || !this.vibrator.hasVibrator())
      promise.reject("ERROR", "No vibrator detected on device");

    if(time < 0) time = 1000; // Default value is 1 sec to time
    intensity = this.intensityConvert(intensity);

    try {

      if(Build.VERSION.SDK_INT < 26) {
        this.vibrator.vibrate(time); // Deprecated method
        promise.resolve(false); // Have no suport to intensity control
      }
      else {
        this.vibrator.vibrate(VibrationEffect.createOneShot(
                                                time,
                                                intensity
                                                ));
        // Returning if is intensity control enabled
        promise.resolve(this.vibrator.hasAmplitudeControl());
      }

    } catch(Exception e) {
      promise.reject(e);
    }
  }

  @SuppressWarnings("deprecation")
  @ReactMethod
  public void vibratePattern(ReadableArray time, Promise promise) {
    if(this.vibrator == null || !this.vibrator.hasVibrator())
      promise.reject("ERROR", "No vibrator detected on device");

    long[] timeArray = this.readableArray2ArrayLong(time);

    try {

        this.vibrator.vibrate(timeArray, -1); // Deprecated method
        promise.resolve(true); // Everything ok

    } catch(Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void hasVibrator(Promise promise) {
    promise.resolve(this.vibrator != null && this.vibrator.hasVibrator());
  }

  @ReactMethod
  public void hasIntensityControl(Promise promise) {
    promise.resolve(
                      this.vibrator != null &&
                      (Build.VERSION.SDK_INT >= 26) &&
                      this.vibrator.hasAmplitudeControl() // Added only at API level 26
                    );
  }

  @ReactMethod
  public void stop(Promise promise) {
    if(this.vibrator == null || !this.vibrator.hasVibrator())
      promise.reject("ERROR", "No vibrator detected on device");

      try {
        this.vibrator.cancel();
        promise.resolve(true);
      } catch(Exception e) {
        promise.reject(e);
      }
  }

  private long[] readableArray2ArrayLong(ReadableArray source) {
    long[] target = new long[source.size()];
    for(int i = 0; i < source.size(); i++)
      target[i] = (long) source.getInt(i);
    return target;
  }

  private int intensityConvert(int original) {
    if(original < 1 || original > 100)
      return VibrationEffect.DEFAULT_AMPLITUDE;
    else
      return (int) Math.round(((float)original/100)*255);
  }
}

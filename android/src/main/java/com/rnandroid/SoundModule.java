package com.rnandroid;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;


public class SoundModule extends ReactContextBaseJavaModule {

  Vibrator vibrator;

  public VibratorModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.vibrator = (Vibrator) reactContext.getSystemService(reactContext.VIBRATOR_SERVICE);
  }

  @Override
  public String getName() {
    return "Sound";
  }

}

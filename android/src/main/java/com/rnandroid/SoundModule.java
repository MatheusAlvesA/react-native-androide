package com.rnandroid;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;


public class SoundModule extends ReactContextBaseJavaModule {

  public SoundModule(ReactApplicationContext reactContext) {
    super(reactContext);
    // TODO
  }

  @Override
  public String getName() {
    return "Sound";
  }

}

package com.rnandroid;

import android.os.BatteryManager;
import android.content.Intent;
import android.content.IntentFilter;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.Map;
import java.util.HashMap;

/*
  This Class requires Android API >=23
*/
public class StatusModule extends ReactContextBaseJavaModule {

  BatteryManager battery = null;
  ReactApplicationContext reactContext = null;

  public StatusModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.battery = (BatteryManager)reactContext.getSystemService(reactContext.BATTERY_SERVICE);
  }

  @Override
  public String getName() {
    return "AndroidSystemStatus";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();

   //TODO

    return constants;
  }

  @ReactMethod
  public void getBatteryLevel(Promise promisse) {
  	try {
  		promisse.resolve(
          this.battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
      );
  	}
    catch(Exception e) { // Any exception causes a rejection of the promisse
  		promisse.reject("ERROR", e.getMessage(), e);
  	}
  }

  @ReactMethod
  public void batteryIsCharging(Promise promisse) {
    try {
      IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
      Intent batteryStatus = this.reactContext.getApplicationContext().registerReceiver(null, ifilter);
      int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
      boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

      promisse.resolve(isCharging);
    }
    catch(Exception e) { // Any exception causes a rejection of the promisse
      promisse.reject("ERROR", e.getMessage(), e);
    }
  }

}

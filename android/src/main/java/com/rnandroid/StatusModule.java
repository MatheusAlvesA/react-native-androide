package com.rnandroid;

import android.os.BatteryManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.content.pm.PackageManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Collections;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.math.BigInteger;


/*
  This Class requires Android API >=23
*/
public class StatusModule extends ReactContextBaseJavaModule {

  BatteryManager battery = null;
  ReactApplicationContext reactContext = null;
  WifiManager wfManager = null;

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

    constants.put("brand", Build.BRAND);
    constants.put("deviceCountry", this.getCurrentCountry());
    constants.put("systemVersion", Build.VERSION.RELEASE);
    constants.put("isEmulator", this.isEmulator());
    constants.put("apiLevel", Build.VERSION.SDK_INT);
    constants.put("model", Build.MODEL);

    return constants;
  }

  private Boolean isEmulator() {
    return Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.contains("Android SDK built for x86")
        || Build.MANUFACTURER.contains("Genymotion")
        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        || "google_sdk".equals(Build.PRODUCT);
  }

  private String getCurrentCountry() {
    Locale current = getReactApplicationContext().getResources().getConfiguration().getLocales().get(0);
    return current.getCountry();
  }

  /*
    Warning: this functions requires android.permission.ACCESS_WIFI_STATE
  */
  private WifiManager getWifiManager() {
    if(this.wfManager == null) {
      this.wfManager = (WifiManager) this.reactContext.getApplicationContext().getSystemService(this.reactContext.WIFI_SERVICE);
    }
    return this.wfManager;
  }

  private WifiInfo getWifiInfo() {
      WifiManager wifiMgr = this.getWifiManager();
      return wifiMgr.getConnectionInfo();
  }

  private Boolean isWifiConnected() {
      WifiManager wifiMgr = this.getWifiManager();

      if(wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON
          WifiInfo wifiInfo = this.getWifiInfo();

          if( wifiInfo.getNetworkId() == -1 ) {
              return false; // Not connected to an access point
          }

          return true; // Connected to an access point
      } else {
          return false; // Wi-Fi adapter is OFF
      }
  }

  @ReactMethod
  public void isWifiConnected(Promise promisse) {
      try {
        promisse.resolve( this.isWifiConnected() );
      } catch (Exception e) {
        promisse.reject(e);
      }
  }

  @ReactMethod
  public void isWifiEnabled(Promise promisse) {
    try {
      WifiManager wifiMgr = this.getWifiManager();
      promisse.resolve( wifiMgr.isWifiEnabled() );
    } catch (Exception e) {
      promisse.reject(e);
    }
  }

  @ReactMethod
  public void getFreeDiskStorage(Promise promisse) {
    try {
      StatFs external = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
      promisse.resolve( BigInteger.valueOf(external.getAvailableBytes()).intValue() );
    } catch (Exception e) {
      promisse.reject(e);
    }
  }

  @ReactMethod
  public void getIpAddress(Promise promisse) {
    if(!this.isWifiConnected()) {
      promisse.reject("WIFI_DISCONNECTED", "The WiFi adapter are not connected in any access point");
    }

    try {
      int ip = getWifiInfo().getIpAddress();
      String ipStr = String.format("%d.%d.%d.%d",
                                   (ip & 0xff),
                                   (ip >> 8 & 0xff),
                                   (ip >> 16 & 0xff),
                                   (ip >> 24 & 0xff));

      promisse.resolve(ipStr);
    } catch(Exception e) {
      promisse.reject(e);
    }
  }

  @ReactMethod
  private void getCurrentLanguage(Promise promisse) {
    Locale current = getReactApplicationContext().getResources().getConfiguration().getLocales().get(0);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      promisse.resolve( current.toLanguageTag() );
    } else {
      StringBuilder builder = new StringBuilder();
      builder.append(current.getLanguage());
      if (current.getCountry() != null) {
        builder.append("-");
        builder.append(current.getCountry());
      }
      promisse.resolve( builder.toString() );
    }
  }

  @ReactMethod
  public void getMacAddress(Promise promisse) {
    // Maybe this work, but we have no guarantee.
    String macAddress = "Unknown";
    try {
      macAddress = getWifiInfo().getMacAddress();
    } catch(Exception e) {
      promisse.reject(e);
      return;
    }

    String permission = "android.permission.INTERNET";
    int res = this.reactContext.checkCallingOrSelfPermission(permission);
    // Trying a better way to get this info
    if (res == PackageManager.PERMISSION_GRANTED) {
      try {
        List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface nif : all) {
          if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

          byte[] macBytes = nif.getHardwareAddress();
          if (macBytes == null) {
              macAddress = "";
          } else {

            StringBuilder res1 = new StringBuilder();
            for (byte b : macBytes) {
                res1.append(String.format("%02X:",b));
            }

            if (res1.length() > 0) {
                res1.deleteCharAt(res1.length() - 1);
            }

            macAddress = res1.toString();
          }
        }
      } catch (Exception ex) { /* Empty because we already have the info */ }
    }

    promisse.resolve(macAddress);
  }

  @ReactMethod
  public void getBatteryLevel(Promise promisse) {
  	try {
  		promisse.resolve(
          this.battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
      );
  	}
    catch(Exception e) { // Any exception causes a rejection of the promisse
  		promisse.reject(e);
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
      promisse.reject(e);
    }
  }

}

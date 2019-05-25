package com.rnandroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class RNAndroidPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {

      List<NativeModule> modules = new ArrayList<>();

	    modules.add(new ToastModule(reactContext));
	    modules.add(new StatusModule(reactContext));
      modules.add(new VibratorModule(reactContext));
      modules.add(new SoundModule(reactContext));
      modules.add(new KeepAwake(reactContext));
      modules.add(new FileSystem(reactContext));

      return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }
}

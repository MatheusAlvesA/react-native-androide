# react-native-androide

[![npm version](https://img.shields.io/npm/v/react-native-androide.svg)](https://www.npmjs.com/package/react-native-androide)
[![npm dm](https://img.shields.io/npm/dm/react-native-androide.svg)](https://www.npmjs.com/package/react-native-androide)
[![npm dt](https://img.shields.io/npm/dt/react-native-androide.svg)](https://www.npmjs.com/package/react-native-androide)

React Native is a amazing framework to create UI, but does not have too much functions likes java applications made to Android do.
This library is designed to Android developers, and has several functions and components that allow better integrations with the Android system.

Full documentation can be found in https://github.com/MatheusAlvesA/react-native-androide/wiki

## Getting started

`$ npm install react-native-androide --save`

Since the version 1.0.0 of this library an additional step of installation is **required**.

This lib implements the AdMob. Unfortunately the AdMob platform requires an application id placed on the Manifest File.
The absence of this parameter will cause Crash in your App. To solve this issue, you only needs to put the oficial test application id on the Manifest File.

Open the Manifest file located in `android\app\src\main\AndroidManifest.xml` and add the test id inside the `application` tag:
```XML
<application>

   ...

  <meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"
  />
</application>
```

### Mostly automatic linking (Recommended)

> If you are using React Native >= v0.60.0, the linking steps are not required.

`$ react-native link react-native-androide`

### Manual linking

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.rnandroid.RNAndroidPackage;` to the imports at the top of the file
  - Add `new RNAndroidPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-androide'
  	project(':react-native-androide').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-androide/android')
  	```
3. Insert the following line inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-androide')
  	```  

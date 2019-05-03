# react-native-androide

[![npm version](https://img.shields.io/npm/v/react-native-androide.svg)](https://www.npmjs.com/package/react-native-androide)
[![npm dm](https://img.shields.io/npm/dm/react-native-androide.svg)](https://www.npmjs.com/package/react-native-androide)
[![npm dt](https://img.shields.io/npm/dt/react-native-androide.svg)](https://www.npmjs.com/package/react-native-androide)
[![deps](https://david-dm.org/getsentry/react-native-androide/status.svg)](https://david-dm.org/getsentry/react-native-androide?view=list)

React Native is a amazing framework to create UI, but does not have too much functions likes java applications made to Android do.
This library is designed to Android developers, and has several functions and components that allow better integrations with the Android system.

Full documentation can be found in https://github.com/MatheusAlvesA/react-native-androide/wiki

## Getting started

`$ npm install react-native-androide --save`

### Mostly automatic installation (Recommended)

`$ react-native link react-native-androide`

### Manual installation

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

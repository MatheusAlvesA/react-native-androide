# react-native-android

This library is under development, this means that it can not be used in production environment yet

## Getting started

`$ npm install react-native-android --save`

### Mostly automatic installation

`$ react-native link react-native-android`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNAndroidPackage;` to the imports at the top of the file
  - Add `new RNAndroidPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-android'
  	project(':react-native-android').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-android/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-android')
  	```


## Usage
```javascript
import RNAndroid from 'react-native-android';

// TODO
RNAndroid;
```
  
# react-native-android

This library is under development, this means that it can not be used in production environment yet

## Getting started

`$ npm install react-native-androide --save`

### Mostly automatic installation

`$ react-native link react-native-androide`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.rnandroid.RNAndroidPackage;` to the imports at the top of the file
  - Add `new RNAndroidPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-androide'
  	project(':react-native-androide').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-androide/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-androide')
  	```


## Usage
```javascript
import RNAndroid from 'react-native-androide';

// TODO
RNAndroid;
```
  
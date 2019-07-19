import { NativeModules, DeviceEventEmitter } from 'react-native';

const { InterstitialAdModule } = NativeModules;

let AdMobInterstitialProxy = {
	...InterstitialAdModule,
  setInterstitialOpenedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_InterstitialAdOpened', cb);
	},
  setInterstitialClosedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_InterstitialAdClosed', cb);
  },
  setInterstitialClickedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_InterstitialAdClicked', cb);
  }
};

export default AdMobInterstitialProxy;

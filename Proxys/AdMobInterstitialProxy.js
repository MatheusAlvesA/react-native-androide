import { NativeModules, DeviceEventEmitter } from 'react-native';

const { InterstitialAdModule } = NativeModules;

const AdMobInterstitialProxy = {
	...InterstitialAdModule,
  setOpenedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_InterstitialAdOpened', cb);
	},
  setClosedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_InterstitialAdClosed', cb);
  },
  setClickedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_InterstitialAdClicked', cb);
  }
};

export default AdMobInterstitialProxy;

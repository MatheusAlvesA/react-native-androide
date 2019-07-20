import { NativeModules, DeviceEventEmitter } from 'react-native';

const { RewardedVideoModule } = NativeModules;

const AdMobRewardedVideoProxy = {
	...RewardedVideoModule,
  setOpenedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_RewardedVideoOpened', cb);
	},
  setClosedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_RewardedVideoClosed', cb);
  },
  setStartedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_RewardedVideoStarted', cb);
  },
  setCompletedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_RewardedVideoCompleted', cb);
  },
  setRewardedListener: function(cb) {
    return DeviceEventEmitter.addListener('AdMob_RewardedVideoReward', cb);
  }
};

export default AdMobRewardedVideoProxy;

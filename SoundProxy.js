import { NativeModules, DeviceEventEmitter } from 'react-native';

const { Sound } = NativeModules;

let SoundProxy = {
	...Sound,
  setOnCompletionListener: function(cb) {
    return DeviceEventEmitter.addListener('soundFinishedPlay', cb);
	},
	seekTo: async function(where) {
		return await Sound.seekTo(where.toString());
	},
  getDuration: async function() {
		return Number(await Sound.getDuration());
	},
  getCurrentPosition: async function() {
		return Number(await Sound.getCurrentPosition());
	}
};

export default SoundProxy;

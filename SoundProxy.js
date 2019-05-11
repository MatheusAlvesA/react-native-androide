import { NativeModules, DeviceEventEmitter, PermissionsAndroid } from 'react-native';

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
	},
	playNow: async function(name) {
		await Sound.prepare(name);
		await Sound.play();
		return true;
	},
	requestPermission: async function() {
			const granted = await PermissionsAndroid.request(
			  PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
			  {
				'title': 'Play Sound',
				'message': 'This application requires access to the file system to play songs'
			  }
			);

			if (granted === PermissionsAndroid.RESULTS.GRANTED) {
				return true;
			} else {
				return false;
			}
	}
};

export default SoundProxy;

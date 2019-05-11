import { NativeModules } from 'react-native';

const { Sound } = NativeModules;

let SoundProxy = {
	...Sound,
	seekTo: async function(souce) {
		return await Sound.seekTo(souce.toString());
	},
  getDuration: async function() {
		return Number(await Sound.getDuration());
	},
  getCurrentPosition: async function() {
		return Number(await Sound.getCurrentPosition());
	}
};

export default SoundProxy;

import { NativeModules } from 'react-native';

const { Vibrator } = NativeModules;

let VibratorProxy = {
	...Vibrator,
  vibratePattern: undefined, // unseting the overridden method
	vibrate: async function(time, intensity) {
    if(typeof time === 'number') {
      if(typeof intensity !== 'number')
        intensity = 100;
      return await Vibrator.vibrate(Math.round(time), Math.round(intensity));
    }

    if(Array.isArray(time))
      return await Vibrator.vibratePattern(time);

		throw Error('time must be a number or a array<number>');
	}
};

export default VibratorProxy;

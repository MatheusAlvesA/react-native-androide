import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './AndroidSystemStatusProxy.js';
import VibratorProxy from './VibratorProxy.js';
import SoundProxy from './SoundProxy.js';

const { AndroidToast, KeepAwake } = NativeModules;

module.exports = {
  AndroidToast,
  KeepAwake,
  Sound: SoundProxy,
  Vibrator: VibratorProxy,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

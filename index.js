import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './AndroidSystemStatusProxy.js';
import VibratorProxy from './VibratorProxy.js';

const { AndroidToast } = NativeModules;

module.exports = {
  AndroidToast,
  Vibrator: VibratorProxy,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

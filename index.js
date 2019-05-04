import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './AndroidSystemStatusProxy.js';

const { AndroidToast } = NativeModules;

module.exports = {
  AndroidToast,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

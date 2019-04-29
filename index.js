
import { NativeModules } from 'react-native';

const { AndroidToast, AndroidSystemStatus } = NativeModules;

module.exports = {
  AndroidToast,
  AndroidSystemStatus
};

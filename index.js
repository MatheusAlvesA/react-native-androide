import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './AndroidSystemStatusProxy.js';
import VibratorProxy from './VibratorProxy.js';
import SoundProxy from './SoundProxy.js';
import FileSystemProxy from './FileSystemProxy.js';

const { AndroidToast, KeepAwake, Downloader } = NativeModules;

module.exports = {
  AndroidToast,
  KeepAwake,
  Downloader,
  FileSystem: FileSystemProxy,
  Sound: SoundProxy,
  Vibrator: VibratorProxy,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

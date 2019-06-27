import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './AndroidSystemStatusProxy.js';
import VibratorProxy from './VibratorProxy.js';
import SoundProxy from './SoundProxy.js';
import DownloaderProxy from './DownloaderProxy.js';
import FileSystemProxy from './FileSystemProxy.js';

const { AndroidToast, KeepAwake } = NativeModules;

module.exports = {
  AndroidToast,
  KeepAwake,
  Downloader: DownloaderProxy,
  FileSystem: FileSystemProxy,
  Sound: SoundProxy,
  Vibrator: VibratorProxy,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

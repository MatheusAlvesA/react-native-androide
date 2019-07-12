import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './AndroidSystemStatusProxy.js';
import VibratorProxy from './VibratorProxy.js';
import SoundProxy from './SoundProxy.js';
import DownloaderProxy from './DownloaderProxy.js';
import FileSystemProxy from './FileSystemProxy.js';
import VideoContainer from './VideoProxy.js';


const { AndroidToast, KeepAwake, VideoViewManager } = NativeModules;

module.exports = {
  AndroidToast,
  KeepAwake,
  Video: VideoContainer,
  Downloader: DownloaderProxy,
  FileSystem: FileSystemProxy,
  Sound: SoundProxy,
  Vibrator: VibratorProxy,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

import { NativeModules } from 'react-native';
import AndroidSystemStatusProxy from './Proxys/AndroidSystemStatusProxy.js';
import VibratorProxy from './Proxys/VibratorProxy.js';
import SoundProxy from './Proxys/SoundProxy.js';
import DownloaderProxy from './Proxys/DownloaderProxy.js';
import FileSystemProxy from './Proxys/FileSystemProxy.js';
import VideoContainer from './Proxys/VideoProxy.js';
import AdMobBanner from './Proxys/AdMobBannerProxy.js';


const { AndroidToast, KeepAwake, VideoViewManager } = NativeModules;

module.exports = {
  AndroidToast,
  KeepAwake,
  AdMobBanner,
  Video: VideoContainer,
  Downloader: DownloaderProxy,
  FileSystem: FileSystemProxy,
  Sound: SoundProxy,
  Vibrator: VibratorProxy,
  AndroidSystemStatus: AndroidSystemStatusProxy
};

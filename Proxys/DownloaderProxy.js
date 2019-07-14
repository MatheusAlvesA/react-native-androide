import { NativeModules, DeviceEventEmitter } from 'react-native';

const { Downloader } = NativeModules;

let DownloaderProxy = {
	...Downloader,
  setProgressListener: function(cb) {
    return DeviceEventEmitter.addListener('downloadProgress', cb);
	}
};

export default DownloaderProxy;

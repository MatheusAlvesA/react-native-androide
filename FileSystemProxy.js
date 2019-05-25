import { NativeModules, PermissionsAndroid } from 'react-native';

const { FileSystem } = NativeModules;

let FileSystemProxy = {
	...FileSystem,
	requestPermission: async function() {
			const granted = await PermissionsAndroid.request(
			  PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
			  {
				'title': 'Access Files',
				'message': 'This application requires access to the file system'
			  }
			);

			return (granted === PermissionsAndroid.RESULTS.GRANTED);
	}
};

export default FileSystemProxy;

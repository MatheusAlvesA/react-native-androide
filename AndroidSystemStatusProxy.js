import { NativeModules } from 'react-native';

const { AndroidSystemStatus } = NativeModules;

let AndroidSystemStatusProxy = {
	...AndroidSystemStatus,
	getFreeDiskStorage: async function() {
		return Number(await AndroidSystemStatus.getFreeDiskStorage());
	}
};

export default AndroidSystemStatusProxy;

import { NativeModules, PermissionsAndroid } from 'react-native';

const { AndroidSystemStatus } = NativeModules;

let AndroidSystemStatusProxy = {
	...AndroidSystemStatus,
	getFreeDiskStorage: async function() {
		return Number(await AndroidSystemStatus.getFreeDiskStorage());
	},
	requestCoarseLocationPermission: async function() {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION,
        {
          title: "Location",
          message: "This App needs your location"
        }
      );
			return (granted === PermissionsAndroid.RESULTS.GRANTED);
  },
	requestFineLocationPermission: async function() {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
        {
          title: "Location",
          message: "This App needs your location"
        }
      );
			return (granted === PermissionsAndroid.RESULTS.GRANTED);
  }
};

export default AndroidSystemStatusProxy;

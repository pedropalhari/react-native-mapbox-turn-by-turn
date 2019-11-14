import {NativeModules} from 'react-native';

function initMapbox(apiKey, callbackFunction) {
  NativeModules.ToastExample.initMapbox(apiKey, callbackFunction);
}

function navigateFromTo(from = {lat: 0, lon: 0}, to = {lat: 0, lon: 0}) {
  NativeModules.ToastExample.navigateFromTo(from.lon, from.lat, to.lon, to.lat);
}

const Mapbox = {initMapbox, navigateFromTo};

export default Mapbox;

import { NativeModules } from "react-native";

function initMapbox(apiKey, callbackFunction) {
  NativeModules.ToastExample.initMapbox(apiKey, callbackFunction);
}

function navigateFromTo(from = { lat: 0, lon: 0 }, to = { lat: 0, lon: 0 }) {
  return new Promise(resolve => {
    NativeModules.ToastExample.navigateFromTo(
      from.lon,
      from.lat,
      to.lon,
      to.lat,
      res => {
        resolve(res);
      }
    );
  });
}

const Mapbox = { initMapbox, navigateFromTo };

export default Mapbox;

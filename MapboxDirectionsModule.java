package com.mapboxdirections;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.mapboxsdk.Mapbox;

import java.util.Map;
import java.util.HashMap;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

// classes needed to initialize map
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

public class MapboxDirectionsModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  private static ReactApplicationContext reactContext;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";
  private DirectionsRoute currentRoute;
  com.facebook.react.bridge.Callback navigationCallback;

  MapboxDirectionsModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
    reactContext.addActivityEventListener(this);
  }

  @Override
  public String getName() {
    return "ToastExample";
  }

  @ReactMethod
  public void initMapbox(String apiKey, com.facebook.react.bridge.Callback cb){
    getCurrentActivity().runOnUiThread(() -> {
      Mapbox.getInstance(getReactApplicationContext(),
              apiKey);

      cb.invoke("true");
    });
  }

  @ReactMethod
  public void navigateFromTo(Double fromLong, Double fromLat, Double toLong, Double toLat, com.facebook.react.bridge.Callback cb) {
    // Toast.makeText(getReactApplicationContext(), message, duration).show();

    Log.d("RRRR", "rodando!!!!");

    navigationCallback = cb;

    getCurrentActivity().runOnUiThread(() -> {
      //getRoute(Point.fromLngLat(-45.453680, -22.4238269), Point.fromLngLat(-45.4496699, -22.4138284F));
      getRoute(Point.fromLngLat(fromLong, fromLat), Point.fromLngLat(toLong, toLat));
    });
  }


  private void getRoute(Point origin, Point destination) {

    Intent intent = new Intent(getReactApplicationContext().getCurrentActivity(), MapboxActivity.class);
    double[] points = {origin.latitude(), origin.longitude(), destination.latitude(), destination.longitude()};
    intent.putExtra("latLongs", points);
    getReactApplicationContext().getCurrentActivity().startActivityForResult(intent, 1);

  }


  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    Log.d("RRRR", "VOLTOU");

    final int RESULT_OK = -1;
    final int RESULT_CANCELED = 0;

    if (requestCode == 1) {
      if (resultCode == RESULT_OK) {
        navigationCallback.invoke(true);

      }
      if (resultCode == RESULT_CANCELED) {
        navigationCallback.invoke(false);
      }
    }
  }

  @Override
  public void onNewIntent(Intent intent) {

  }
}
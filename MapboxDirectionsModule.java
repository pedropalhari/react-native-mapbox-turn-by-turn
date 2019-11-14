package com.mapboxdirections;

import android.widget.Toast;

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

public class MapboxDirectionsModule extends ReactContextBaseJavaModule {
  private static ReactApplicationContext reactContext;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";
  private DirectionsRoute currentRoute;

  MapboxDirectionsModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
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
  public void navigateFromTo(Double fromLong, Double fromLat, Double toLong, Double toLat) {
    // Toast.makeText(getReactApplicationContext(), message, duration).show();

    getCurrentActivity().runOnUiThread(() -> {
      //getRoute(Point.fromLngLat(-45.453680, -22.4238269), Point.fromLngLat(-45.4496699, -22.4138284F));
      getRoute(Point.fromLngLat(fromLong, fromLat), Point.fromLngLat(toLong, toLat));
    });
  }

  private void getRoute(Point origin, Point destination) {
    NavigationRoute.builder(getReactApplicationContext().getCurrentActivity()).accessToken(Mapbox.getAccessToken())
        .origin(origin).destination(destination).build().getRoute(new Callback<DirectionsResponse>() {
          @Override
          public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
            // You can get the generic HTTP info about the response
            Log.d("MAPBOX", "Response code: " + response.code());
            if (response.body() == null) {
              Log.e("MAPBOX", "No routes found, make sure you set the right user and access token.");
              return;
            } else if (response.body().routes().size() < 1) {
              Log.e("MAPBOX", "No routes found");
              return;
            }

            currentRoute = response.body().routes().get(0);

            boolean simulateRoute = false;
            NavigationLauncherOptions options = NavigationLauncherOptions.builder().directionsRoute(currentRoute)
                .shouldSimulateRoute(simulateRoute).build();
            // Call this method with Context from within an Activity
            NavigationLauncher.startNavigation(getReactApplicationContext().getCurrentActivity(), options);

          }

          @Override
          public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
            Log.e("MAPBOX", "Error: " + throwable.getMessage());
          }
        });
  }
}
# Mapbox Directions API

API Criada para ter acesso as funcionalidades do turn-by-turn do Mapbox

Essa API também está feita somente para RN:0.60+ pois tem binds com o androidx feitos por mim.

## Instalação

- adicionar o repo maven no `build.gradle`:

```groovy
allprojects {
    repositories {
      ...
      mavenLocal()
      maven { url 'https://mapbox.bintray.com/mapbox' } //adicionar
    }
}
```

- adicionar as dependências no `app/build.gradle`:

```groovy
dependencies {
  ...
  implementation 'com.mapbox.mapboxsdk:mapbox-android-navigation-ui:0.42.3' //Mapbox
  implementation 'androidx.appcompat:appcompat:1.0.0' //Libs de compatibilidade do Androidx
  implementation 'androidx.annotation:annotation:1.1.0'
  ...
}
```

- adicionar ao `AndroidManifest.xml` as permissões:

```xml
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

- adicionar ao `android/app/src/main/res/values/strings.xml` as seguintes chaves:

```xml
<resources>
  ...
  <!-- aqui dentro -->
  <string name="title_mock_navigation">Mock Navigation</string>
  <string name="description_mock_navigation">Mock a navigation session using a mock location engine.</string>

  <string name="title_off_route_detection">Off route detection</string>
  <string name="description_off_route_detection">Uses the Route Utils class to determine if a users off route.</string>

  <string name="title_reroute">Reroute</string>
  <string name="description_reroute">Test the reroute function inside the navigation SDK</string>

  <string name="title_navigation_route_ui">Navigation Map Route</string>
  <string name="description_navigation_route_ui">Shows different styles using NavigationMapRoute</string>

  <string name="title_navigation_launcher">Navigation Launcher</string>
  <string name="description_navigation_launcher">Drop-in UI experience</string>

  <string name="title_end_navigation">End Navigation</string>
  <string name="description_end_navigation">Shows how to end navigation using NavigationView</string>

  <string name="title_dual_navigation_map">Dual Navigation Map</string>
  <string name="description_dual_navigation_map">Shows how to add NavigationView and MapView in the same layout</string>

  <string name="title_waypoint_navigation">Waypoint Navigation</string>
  <string name="description_waypoint_navigation">Navigation with waypoints between destinations</string>

  <string name="title_embedded_navigation">Embedded Navigation</string>
  <string name="description_embedded_navigation">Navigation in a view which contains other views</string>

  <string name="title_fragment_navigation">NavigationView implemented with Fragment</string>
  <string name="description_fragment_navigation">NavigationView implemented with Fragment</string>

  <string name="title_component_navigation">MapboxNavigation with UI components</string>
  <string name="description_component_navigation">MapboxNavigation with UI components</string>

  <string name="settings">Settings</string>
  <string name="simulate_route">Simulate Route</string>
  <string name="language">Language</string>
  <string name="unit_type">Unit Type</string>
  <string name="route_profile">Route Profile</string>

  <string name="unit_type_key" translatable="false">unit_type</string>
  <string name="simulate_route_key" translatable="false">simulate_route</string>
  <string name="language_key" translatable="false">language</string>
  <string name="route_profile_key" translatable="false">route_profile</string>
  <string name="offline_preference_key"
      translatable="false">offline_preference_key</string>
  <string name="view_examples_key" translatable="false">view_examples</string>
  <string name="default_locale" translatable="false">default_for_device</string>
  <string name="default_unit_type" translatable="false">default_for_device</string>
  <string name="current_night_mode" translatable="false">current_night_mode</string>
  <string name="was_in_tunnel" translatable="false">was_in_tunnel</string>
  <string name="was_navigation_stopped" translatable="false">was_navigation_stopped</string>

  <string name="error_route_not_available">Current route is not available</string>
  <string name="error_select_longer_route">Please select a longer route</string>
  <string name="error_valid_route_not_found">Valid route not found.</string>
  <string name="explanation_long_press_waypoint">Long press map to place waypoint</string>

  <string name="blue" translatable="false">#4264fb</string>

  <string name="hint_where_to">Where to?</string>
  <string name="offline_routing">Offline routing</string>
  <string name="autocompleteBehavior" translatable="false">com.mapbox.services.android.navigation.testapp.example.ui.autocomplete.AutocompleteBottomSheetBehavior</string>
</resources>

```

- jogar o arquivo `dimens.xml` junto com o `strings.xml` em `android/app/src/main/res/values/`.

- jogar o arquivo `activity_embedded_navigation.xml` em `android/app/src/main/res/layout/`.

- copiar `MapboxDirectionsModule.java`, `MapboxDirectionsPackage.java` e `MapboxActivity.java` para a mesma pasta do seu `MainApplication.java`.

- trocar o `package` do `MapboxDirectionsModule.java`, `MapboxDirectionsPackage.java` e `MapboxActivity.java` de `com.mapboxdirections` para o mesmo do seu `MainApplication.java`.

```Java
package com.mapboxdirections; //Antigo

package meu.pacote.mainapplication; //Certo
```

- adicionar o pacote nos `packages` no seu `MainApplication.java`:

```Java
//MainApplication.java
package com.meu.pacote.mainapplication;


//...
import com.meu.pacote.mainapplication.MapboxDirectionsPackage; //Adicionar
//...

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    //....

    @Override
    protected List<ReactPackage> getPackages() {
      @SuppressWarnings("UnnecessaryLocalVariable")
      List<ReactPackage> packages = new PackageList(this).getPackages();
      // Packages that cannot be autolinked yet can be added manually here, for
      // example:
      // packages.add(new MyReactNativePackage());


      packages.add(new MapboxDirectionsPackage()); //Adicionar aqui
      return packages;
    }
  };
}
```

- Por fim, copiar `MapboxLib.js` para uma pasta qualquer `/utils` da sua aplicação.

## Usando

```javascript
  import Mapbox from `./MapboxLib.js`

  Mapbox.initMapbox(
    '<CHAVE_API_MAPBOX>',
    () => {
      //Callback de sucesso
    },
  );

  //Caso o callback tenha sido executado com sucesso
  Mapbox.navigateFromTo(
    {lat: -22.4238269, lon: -45.45368},
    {lat: -22.4138284, lon: -45.4496699},
  );
```

- `initMapbox(apiKey: string, callbackSuccessFunc: (success: string) => any)`

Chama a API, deve ser executada somente uma vez no código inteiro e inicializa o objeto nativo `Mapbox`

- `navigateFromTo(from: {lat: number, to: number}, to: {lat: number, to: number}): Promise<boolean>`

Chama a janela de navegação da posição `from` até a posição `to`. Retorna uma Promise que vai resolver `true` se tiver chegado ao destino final ou `false` caso tenha saido da navegação.

## Developing

Basicamente copiado o https://docs.mapbox.com/help/tutorials/android-navigation-sdk/, isolando somente a parte do navegação. Conforme os erros de androidx foram aparecendo, fui substituindo as bibliotecas.

Para pegar a MainActivity no React é utilizado `getReactApplicationContext().getCurrentActivity()` dentro da classe. Para rodar na _thread UI_ requerida, são usados spawners nas duas funções da API, como exemplo:

```java
  @ReactMethod
  public void navigateFromTo(Double fromLong, Double fromLat, Double toLong, Double toLat) {
    //Spawnando na UIThread
    getCurrentActivity().runOnUiThread(() -> {
      getRoute(Point.fromLngLat(fromLong, fromLat), Point.fromLngLat(toLong, toLat));
    });
  }
```

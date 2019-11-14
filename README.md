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

- copiar `MapboxDirectionsModule.java` e `MapboxDirectionsPackage.java` para a mesma pasta do seu `MainApplication.java`.

- trocar o `package` do `MapboxDirectionsModule.java` e `MapboxDirectionsPackage.java` de `com.mapboxdirections` para o mesmo do seu `MainApplication.java`.

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

- `navigateFromTo(from: {lat: number, to: number}, to: {lat: number, to: number})`

Chama a janela de navegação da posição `from` até a posição `to`.

## Developing

Basicamente copiado o https://docs.mapbox.com/help/tutorials/android-navigation-sdk/, isolando somente a parte do navegação. Conforme os erros de androidx foram aparecendo, fui substituindo as bibliotecas.

Para pegar a MainActivity no React é utilizado `getReactApplicationContext().getCurrentActivity()` dentro da classe. Para rodar na *thread UI* requerida, são usados spawners nas duas funções da API, como exemplo:

```java
  @ReactMethod
  public void navigateFromTo(Double fromLong, Double fromLat, Double toLong, Double toLat) {
    //Spawnando na UIThread
    getCurrentActivity().runOnUiThread(() -> {
      getRoute(Point.fromLngLat(fromLong, fromLat), Point.fromLngLat(toLong, toLat));
    });
  }
```

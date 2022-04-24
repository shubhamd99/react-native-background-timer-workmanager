# React Native Background Timer Workmanager [Android]
Emit event periodically in both foreground and background.

## Installation
1. If you use Expo to create a project [you'll just need to](https://facebook.github.io/react-native/docs/getting-started#caveats) "[eject](https://docs.expo.io/versions/latest/expokit/eject)".

    ```bash
    expo eject
    ```

2. Install React Native Background Timer Work Manager package.

    ```bash
    yarn add react-native-background-timer-workmanager
    # or using npm
    npm install react-native-background-timer-workmanager --save
    ```

3. Link React Native Background Timer library. This step is not necessary when you use React Native >= 0.60 (and your app is not ejected from Expo).

    ```bash
    react-native link react-native-background-timer-workmanager
    ```

4. Link the library manually if you get errors:

<details>
    <summary>Android manual linking</summary>

- `android/settings.gradle`

    ```diff
    + include ':react-native-background-timer-workmanager'
    + project(':react-native-background-timer-workmanager').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-background-timer-workmanager/android')
    ```

- `android/app/build.gradle`

    ```diff
    dependencies {
    +   implementation project(':react-native-background-timer-workmanager')
    }
    ```

- `android/app/src/main/java/com/your-app/MainApplication.java`

    ```diff
    + import com.shubhamd99.timer.BackgroundTimerPackage;

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
    +   new BackgroundTimerPackage()
      );
    }
    ```
</details>

## Usage

```js
import BackgroundTimer from 'react-native-background-timer-workmanager';
```

```js
BackgroundTimer.start(3000, 'UNIQUE_TAG' () => { 
// code that will be called every 3 seconds 
});

BackgroundTimer.stop('UNIQUE_TAG'); // To Stop Polling
```

Example:
```js
import React, {useEffect} from 'react';

const App = () => {
    useEffect(() => {
        BackgroundTimer.start(10000, 'homeScreenPolling', () => {
            console.log('polling..');
        });
        return () => BackgroundTimer.stop('homeScreenPolling');
    }, []);
};

export default App;
```




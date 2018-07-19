
# react-native-tesse-call-voip

## Getting started

`$ npm install react-native-tesse-call-voip --save`

### Mostly automatic installation

`$ react-native link react-native-tesse-call-voip`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-tesse-call-voip` and add `RNTesseCallVoip.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNTesseCallVoip.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.tesse.reactnative.RNTesseCallVoipPackage;` to the imports at the top of the file
  - Add `new RNTesseCallVoipPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-tesse-call-voip'
  	project(':react-native-tesse-call-voip').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-tesse-call-voip/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-tesse-call-voip')
  	```


## Usage
```javascript
import RNCall from 'react-native-tesse-call-voip';

// TODO: What to do with the module?
RNCall;
```
  
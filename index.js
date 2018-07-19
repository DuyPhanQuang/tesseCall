
const ReactNative = require("react-native");
const NativeModules = ReactNative.NativeModules;
const RNCall = NativeModules.RNCall;

const ReactNativeCall = {
  setAppName: appName => {
    return new Promise((resolve, reject) => {
      const success = RNCall.setAppName(appName);
      if (success) {
        resolve();
      } else {
        reject();
      }
    });
  },
  //@param {String} appName - The title of the call
  
  setIcon: iconName => {
    return new Promise((resolve, reject) => {
      const success = RNCall.setIcon(iconName);
      if (success) {
        resolve();
      } else {
        reject();
      }
    });
  },
  //param {String} iconName - The file name (should be a .png file) of the icon

  setRingtone: ringtoneName => {
    return new Promise((resolve, reject) => {
      const success = RNCall.setRingtone(ringtoneName);
        if (success) {
            resolve();
        } else {
            reject();
        }
    });
  },
  //param {String} ringtoneName - The file name (should be a .caf file) of the ringtone 

  setIncludeInRecents: showInRecentCalls => {
    return new Promise((resolve, reject) => {
      if (typeof showInRecentCalls !== "boolean") {
        reject(
            new Error("setIncludeInRecents takes a boolean as an arguments")
        );
        return;
      }

      const success = RNCall.setIncludeInRecents(showInRecentCalls);
      if (success) {
        resolve();
      } else {
        reject();
      }
    });
  },
  // @param {Boolean} showInRecentCalls - Set this to true if you want calls to show up in recent calls.
  
  setVideo: showAsVideoCall => {
    return new Promise((resolve, reject) => {
      if (typeof showAsVideoCall !== "boolean") {
        reject(new Error("setVideo takes a boolean as an argument"));
        return;
      }

      const success = RNCall.setVideo(showAsVideoCall);
      if (success) {
        resolve();
      } else {
        reject();
      }
    });
  },

  receiveCall: from => {
    console.log('receiveCall', RNCall.receiveCall(from));
    return RNCall.receiveCall(from);
  },
  sendCall: to => {
    console.log('receiveCall', RNCall.receiveCall(from));
    return RNCall.sendCall(to);
  },
  connectCall: () => {
    return RNCall.connectCall();
  },
  endCall: () => {
    return RNCall.endCall();
  },
  mute: () => {
    return RNCall.mute();
  },
  unmute: () => {
    return RNCall.unmute();
  },
  speakerOn: () => {
    return RNCall.speakerOn();
  },
  speakerOff: () => {
    return RNCall.speakerOff();
  },
  callNumber: () => {
    return RNCall.callNumber(to);
  }
};

module.exports = ReactNativeCall;


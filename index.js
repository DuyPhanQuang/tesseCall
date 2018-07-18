
const ReactNative = require("react-native");
const NativeModules = ReactNative.NativeModules;
const RNTesseCallVoip = NativeModules.RNTesseCallVoip;

const ReactNativeCall = {
  setAppName: appName => {
    return new Promise((resolve, reject) => {
      const success = RNTesseCallVoip.setAppName(appName);
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
      const success = RNTesseCallVoip.setIcon(iconName);
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
      const success = RNTesseCallVoip.setRingtone(ringtoneName);
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

      const success = RNTesseCallVoip.setIncludeInRecents(showInRecentCalls);
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

      const success = RNTesseCallVoip.setVideo(showAsVideoCall);
      if (success) {
        resolve();
      } else {
        reject();
      }
    });
  },

  receiveCall: from => {
    return RNTesseCallVoip.receiveCall(from);
  },
  sendCall: to => {
    return RNTesseCallVoip.sendCall(to);
  },
  connectCall: () => {
    return RNTesseCallVoip.connectCall();
  },
  endCall: () => {
    return RNTesseCallVoip.endCall();
  },
  mute: () => {
    return RNTesseCallVoip.mute();
  },
  unmute: () => {
    return RNTesseCallVoip.unmute();
  },
  speakerOn: () => {
    return RNTesseCallVoip.speakerOn();
  },
  speakerOff: () => {
    return RNTesseCallVoip.speakerOff();
  },
  callNumber: () => {
    return RNTesseCallVoip.callNumber(to);
  }
};

module.exports = ReactNativeCall;


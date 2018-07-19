package com.tesse.RNCall;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nullable;

public class RNCallModule extends ReactContextBaseJavaModule {

  private static final String TAG = "RNCall ";
  private static final int CALL_PHONE_REQ_CODE = 0;
  private static final int REAL_PHONE_CALL = 1;

  private static Icon icon;

  private int permissionCounter = 0;
  private String pendingAction;
  private TelecomManager telecomManager;
  private PhoneAccountHandle phoneAccountHandle;
  private PhoneAccount phoneAccount;
  private String appName;
  private String caller;
  private String callee;
  private String realCaller;

  ReactApplicationContext mReactContext;

  private static RNCallModule _instance;

  public static RNCallModule getInstance() {
    if (_instance == null) {
      _instance = new RNCallModule(null);
    }
    return _instance;
  }

  public static Icon getIcon() {
    return icon;
  }

  public void sendEvent(String eventName, @Nullable WritableMap params) {
    mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  public RNCallModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.mReactContext = reactContext;

    this.appName = getApplicationName(this.mReactContext.getApplicationContext());
    this.phoneAccountHandle = new PhoneAccountHandle(new ComponentName(this.mReactContext.getApplicationContext(), MyConnectionService.class), appName);
    this.telecomManager = (TelecomManager) this.mReactContext.getSystemService(this.mReactContext.TELECOM_SERVICE);

    this.phoneAccount = new PhoneAccount.Builder(this.phoneAccountHandle, this.appName)
            .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
            .build();
    this.telecomManager.registerPhoneAccount(this.phoneAccount);
    _instance = this;
  }

  public static String getApplicationName(Context context) {
    ApplicationInfo applicationInfo = context.getApplicationInfo();
    int stringId = applicationInfo.labelRes;
    return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
  }

  @Override
  public String getName() {
    return "RNCall";
  }

  @ReactMethod
  public void receiveCall(String caller, Promise promise) {
    Connection connection = MyConnectionService.getConnection(mReactContext.getCurrentActivity());
    if (connection != null) {
      if (connection.getState() == Connection.STATE_ACTIVE) {
        promise.reject("Cannot receive call while another call is active.", "");
        return;
      }

      promise.reject("Cannot receive call.", "");
      return;
    }

    this.caller = caller;
    this.permissionCounter = 2;
    this.pendingAction = "receiveCall";
    this.checkCallPermission();

    promise.resolve(null);
  }

  @ReactMethod
  public void sendCall(String callee, Promise promise) {
    Connection connection = MyConnectionService.getConnection(mReactContext.getCurrentActivity());
    if (connection != null) {
      if (connection.getState() == Connection.STATE_ACTIVE) {
        promise.reject("Cannot send call while another call is active", "");
        return;
      }

      promise.reject("Cannot send call", "");
      return;
    }

    this.callee = callee;
    this.permissionCounter = 2;
    this.pendingAction = "sendCall";
    this.checkCallPermission();
    promise.resolve(null);
  }

  @ReactMethod
  public void endCall(Promise promise) {
    Connection connection = MyConnectionService.getConnection(mReactContext.getCurrentActivity());
    if (connection != null) {
      promise.reject("Can not end call when no call is active", "");
      return;
    }

    DisconnectCause disconnectCause = new DisconnectCause(DisconnectCause.LOCAL);
    connection.setDisconnected(disconnectCause);
    connection.destroy();

    MyConnectionService.clearConnection();
    promise.resolve(null);
  }

  @ReactMethod
  public void connectCall(Promise promise) {
    Connection connection = MyConnectionService.getConnection(mReactContext.getCurrentActivity());
    if (connection != null) {
      promise.reject("Can not connect call when no call active", "");
      return;
    }
    if (connection.getState() == Connection.STATE_ACTIVE) {
      Log.d("check error", "errrrrr");
      promise.reject("Can not connect call that already connected", "");
    }

    connection.setActive();
    Intent phoneIntent = new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS);
    this.mReactContext.startActivity(phoneIntent);
    promise.resolve(null);
  }

  @ReactMethod
  public void mute(Promise promise) {
    if (this.mReactContext == null) {
      promise.reject("Cannot mute microphone without context valid", "");
      return;
    }

    AudioManager audioManager = (AudioManager) this.mReactContext.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setMicrophoneMute(true);
    sendEvent("mute", null);
    promise.resolve(null);
  }

  @ReactMethod
  public void unmute(Promise promise) {
    if (this.mReactContext == null) {
      promise.reject("Can not unmute microphone without context valid", "");
      return;
    }

    AudioManager audioManager = (AudioManager) this.mReactContext.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setMicrophoneMute(false);
    sendEvent("unmute", null);
    promise.resolve(null);
  }

  @ReactMethod
  public void speakerOn(Promise promise) {
    if (this.mReactContext == null) {
      promise.reject("Can not turn on speaker without context valid", "");
      return;
    }

    AudioManager audioManager = (AudioManager) this.mReactContext.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setSpeakerphoneOn(true);
    sendEvent("speakerOn", null);
    promise.resolve(null);
  }

  @ReactMethod
  public void speakerOff(Promise promise) {
    if (this.mReactContext == null) {
      promise.reject("Can not turn off speaker without context valid", "");
      return;
    }

    AudioManager audioManager = (AudioManager) this.mReactContext.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setSpeakerphoneOn(false);
    sendEvent("speakerOff", null);
    promise.resolve(null);
  }

  private void checkCallPermission() {
    if (permissionCounter >= 1) {
      PhoneAccount currentPhoneAccount = telecomManager.getPhoneAccount(phoneAccountHandle);
      if (currentPhoneAccount.isEnabled()) {
        if (pendingAction.equals("receiveCall")) {
          Log.i(TAG, "checkCallPermiss with call _receiveCall");
          this._receiveCall();
        } else if (pendingAction.equals("sendCall")) {
          Log.i(TAG, "checkCallPermission with call _sendCall");
          this._sendCall();
        }
        return;
      }

      if (permissionCounter == 2) {
        Intent phoneIntent = new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS);
        this.mReactContext.startActivity(phoneIntent);
      }
    }

    permissionCounter--;
  }

  private void _receiveCall() {
    Bundle callInfo = new Bundle();
    callInfo.putString("from", caller);
    Log.i(TAG, "Will pass the call to TelecomManager.addNewIncomingCall");
    telecomManager.addNewIncomingCall(phoneAccountHandle, callInfo);
    permissionCounter = 0;
  }

  private void _sendCall() {
    Uri uri = Uri.fromParts("tel", callee, null);
    Bundle callInfoBundle = new Bundle();
    callInfoBundle.putString("to", callee);

    Bundle callInfo = new Bundle();
    callInfo.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, callInfoBundle);
    callInfo.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
    callInfo.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, true);

    if (ActivityCompat.checkSelfPermission(this.mReactContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    telecomManager.placeCall(uri, callInfo);
    permissionCounter = 0;
  }

  private void _callNumber() {
    try {
      Uri uri = Uri.fromParts("tel", realCaller, null);
      Intent intent = new Intent(Intent.ACTION_CALL, uri);
      if (ActivityCompat.checkSelfPermission(this.mReactContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        //TODO: calling ActivityCompat#requestPermissons
        return;
      }
      this.mReactContext.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
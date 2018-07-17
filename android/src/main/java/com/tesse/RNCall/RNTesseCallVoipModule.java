
package com.tesse.RNCall;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNTesseCallVoipModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNTesseCallVoipModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNTesseCallVoip";
  }
}
package com.tesse.RNCall;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.StatusHints;
import android.telecom.TelecomManager;
import android.util.Log;

public class MyConnectionService extends ConnectionService {

    private static final String TAG = "MyConnectionService";
    private static Connection mConnection;
    private static Activity mActivity;

    public static Connection getConnection (Activity mainActivity) {
        mActivity = mainActivity;
        return mConnection;
    }

    public static void clearConnection() {
        mConnection = null;
    }

    public MyConnectionService (Activity mainActivity) {
        mActivity = mainActivity;
    }



    @Override
    public Connection onCreateIncomingConnection (final PhoneAccountHandle connectionManagerPhoneAccount, final ConnectionRequest connectionRequest) {
        Log.i(TAG, "onCreateIncomingConnection");
        final Connection connection = new Connection() {
            @Override
            public void onAnswer() {
                this.setActive();
                Intent intent = new Intent(getApplicationContext(), mActivity.getClass());
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getApplicationContext().startActivity(intent);
                RNCallModule.getInstance().sendEvent("answer", null);
            }

            @Override
            public void onReject() {
                DisconnectCause disconnectCause = new DisconnectCause(DisconnectCause.REJECTED);
                this.setDisconnected(disconnectCause);
                this.destroy();
                mConnection = null;
                RNCallModule.getInstance().sendEvent("reject", null);
            }
            @Override
            public void onDisconnect() {
                DisconnectCause disconnectCause = new DisconnectCause(DisconnectCause.LOCAL);
                this.setDisconnected(disconnectCause);
                this.destroy();
                mConnection = null;
                RNCallModule.getInstance().sendEvent("hangup", null);
            }
        };

        connection.setAddress(Uri.parse(connectionRequest.getExtras().getString("from")), TelecomManager.PRESENTATION_ALLOWED);

        Icon icon = RNCallModule.getIcon();
        if (icon != null) {
            StatusHints statusHints = new StatusHints("", icon, new Bundle());
            connection.setStatusHints(statusHints);
        }

        RNCallModule.getInstance().sendEvent("receiveCall", null);
        mConnection = connection;
        return mConnection;
    }

    @Override
    public Connection onCreateOutgoingConnection (final PhoneAccountHandle connectionManagerPhoneAccount, final ConnectionRequest connectionRequest) {
        final Connection connection = new Connection() {
            @Override
            public void onStateChanged(int state) {
                if (state == Connection.STATE_DIALING) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), mActivity.getClass());
                            getApplicationContext().startActivity(intent);
                        }
                    }, 500);
                }
            }

            @Override
            public void onDisconnect() {
                DisconnectCause disconnectCause = new DisconnectCause(DisconnectCause.LOCAL);
                this.setDisconnected(disconnectCause);
                this.destroy();
                mConnection = null;
                RNCallModule.getInstance().sendEvent("hangup", null);
            }
        };

        connection.setAddress(Uri.parse(connectionRequest.getExtras().getString("to")), TelecomManager.PRESENTATION_ALLOWED);

        Icon icon = RNCallModule.getIcon();
        if (icon != null) {
            StatusHints statusHints = new StatusHints("", icon, new Bundle());
            connection.setStatusHints(statusHints);
        }

        RNCallModule.getInstance().sendEvent("sendCall", null);
        mConnection = connection;
        return mConnection;
    }
}

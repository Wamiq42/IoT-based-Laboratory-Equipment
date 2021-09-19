package com.sherry.diopus;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class GlobalBluetoothSocket extends Application {

    BluetoothSocket globalSocket = null;

    public BluetoothSocket getGlobalSocket() {
        return globalSocket;
    }

    public void setGlobalSocket(BluetoothSocket globalSocket) {
        this.globalSocket = globalSocket;
    }
}

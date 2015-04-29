package com.example.hockeytom1.eatingapp.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

public class ConnectThread extends Thread {

    private BluetoothSocket socket;
    private BluetoothDevice device;
    private Handler handler;

    public ConnectThread(BluetoothDevice device) {
        this.device = device;

        try {
            this.socket = this.device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
        } catch (IOException e) {
            Log.d("eatingapp", e.getMessage());
        }
    }

    public void run() {
        Log.d("eatingapp", "Starting Bluetooth connect thread");

        if (this.socket == null) {
            Log.d("eatingapp", "Could not find Bluetooth socket");
        } else {
            Log.d("eatingapp", "Successfully found Bluetooth socket");
        }

        try {
            this.socket.connect();

            ConnectedThread connectedThread = new ConnectedThread(this.socket);
            connectedThread.setCommandProcessedHandler(this.handler);
            connectedThread.start();
        } catch (IOException e) {
            Log.d("eatingapp", "Failed to connect to Bluetooth socket");
        }
    }

    public void setProcessedCommandHandler(Handler handler) {
        this.handler = handler;
    }

}

package com.example.hockeytom1.eatingapp.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.Set;
import java.util.UUID;

public class BluetoothConnection {

    private String deviceName;
    private String deviceAddress;
    private UUID deviceUuid;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;

    // Event handling
    private Handler commandProcessedHandler;

    public BluetoothConnection(String deviceName) {
        this.deviceName = deviceName;
        initAdapter();
        findDevice();
    }

    public boolean initAdapter() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Log.d("eatingapp", "Bluetooth adapter not found");
            return false;
        } else {
            Log.d("eatingapp", "Bluetooth adapter found");
            return true;
        }
    }

    public void startReading() {

        ConnectThread connectThread = new ConnectThread(this.device);
        connectThread.setProcessedCommandHandler(this.commandProcessedHandler);
        connectThread.start();
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BluetoothAdapter adapter) {
        this.adapter = adapter;
    }

    private void findDevice() {
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                if (device.getName().equals(this.deviceName)) {
                    this.deviceAddress = device.getAddress();
                    this.device = device;

                    // Find UUID of device
                    for (ParcelUuid uuid : device.getUuids()) {
                        this.deviceUuid = uuid.getUuid();
                    }
                }
            }
        }
    }

    public void setCommandProcessedHandler(Handler commandProcessedHandler) {
        this.commandProcessedHandler = commandProcessedHandler;
    }

}

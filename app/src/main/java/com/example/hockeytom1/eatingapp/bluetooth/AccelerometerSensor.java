package com.example.hockeytom1.eatingapp.bluetooth;

public class AccelerometerSensor {

    private float x;
    private float y;
    private float z;

    public AccelerometerSensor(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }
}

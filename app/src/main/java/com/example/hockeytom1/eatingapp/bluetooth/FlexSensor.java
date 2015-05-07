package com.example.hockeytom1.eatingapp.bluetooth;

public class FlexSensor {
    private int id;
    private int value;

    public FlexSensor(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return this.id;
    }

    public int getValue() {
        return this.value;
    }

}

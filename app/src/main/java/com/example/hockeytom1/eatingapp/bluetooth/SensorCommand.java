package com.example.hockeytom1.eatingapp.bluetooth;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorCommand {
    public String Command;
    public String Data;
    public String Time;
    public long TimeMS;

    private AccelerometerSensor acceleration;
    private FlexSensor[] flexSensors;

    public SensorCommand(String cmd, String data, String time) {
        this.Command = cmd;
        this.Data = data;
        this.Time = time;
    }

    // Parses a raw command
    // Format for a command is:
    // [sensor1]:[data];[sensor2]:[data];...
    // Where [sensor#] is a string to identify the sensor type, and [data] is the data
    // corresponding to that sensor. e.g a:1,2,3;f0:100;f1:200 is
    //      accelerometer: x=1,y=2,z=3
    //      flex sensor 0: 100
    //      flex sensor 1: 200
    public SensorCommand(String rawCommand) {

        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s.SSS");
        String parsedDate = sdf.format(currentTime);

        this.Command = rawCommand;
        this.Time = parsedDate;
        this.TimeMS = currentTime.getTime();
        this.flexSensors = new FlexSensor[5];
        Log.d("eatingapp", "raw command: " + this.Command);
        // Parse all sensors from raw command string
        String[] commands = rawCommand.split(";");

        for (String command : commands) {
            //Log.d("eatingapp", command);
            String[] commandSplit = command.split(":");
            String sensor = commandSplit[0];
            String data = commandSplit[1].replaceAll("\\s", "");

            // Accelerometer data contains multiple values
            if (sensor.equals("a") || sensor.equals("")) {
                String[] accelData = data.split(",");
                String x = (accelData[0].length() > 0) ? accelData[0] : "0";
                String y = (accelData[1].length() > 0) ? accelData[1] : "0";
                String z = (accelData[2].length() > 0) ? accelData[2] : "0";

                this.acceleration = new AccelerometerSensor(
                        Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z)
                );
            } else if (sensor.charAt(0) == 'f') {
                // All flex sensors are labeled f#:value
                int flexSensorId = Character.getNumericValue(sensor.charAt(1));
                this.flexSensors[flexSensorId] = new FlexSensor(flexSensorId, Integer.parseInt(data));
            }
        }

    }

    public AccelerometerSensor getAcceleration() {
        return this.acceleration;
    }

    public FlexSensor[] getFlexSensors() {
        return this.flexSensors;
    }

    // Returns a flex sensor by its id
    public FlexSensor getFlex(int id) {
        return this.flexSensors[id];
    }

}
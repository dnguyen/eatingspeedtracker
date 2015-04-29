package com.example.hockeytom1.eatingapp.bluetooth;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorCommand {
    public String Command;
    public String Data;
    public String Time;
    public long TimeMS;

    public SensorCommand(String cmd, String data, String time) {
        this.Command = cmd;
        this.Data = data;
        this.Time = time;
    }

    // Parses a raw command
    // Format for a command is:
    // [command]:[data]
    // Where [command] is a string to identify the command, and [data] is also a string
    public SensorCommand(String rawCommand) {

        String[] commandSplit = rawCommand.split(":");
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s.SSS");
        String parsedDate = sdf.format(currentTime);

        this.Command = commandSplit[0];
        this.Data = commandSplit[1].replaceAll("\\s", "");
        this.Time = parsedDate;
        this.TimeMS = currentTime.getTime();

    }

}
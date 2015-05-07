package com.example.hockeytom1.eatingapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hockeytom1.eatingapp.bluetooth.BluetoothConnection;
import com.example.hockeytom1.eatingapp.bluetooth.SensorCommand;
import com.example.hockeytom1.eatingapp.storage.DatabaseConnection;


public class MainMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
/*
        BluetoothConnection newConnection = new BluetoothConnection("HC-06");
        newConnection.setCommandProcessedHandler(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                SensorCommand command = (SensorCommand) msg.obj;
                Log.d("eatingapp", "Received: " + command.Command + " " + command.Time + " (" + command.TimeMS + ")");
                Log.d(
                        "eatingapp",
                        "\t acceleration: " +
                                command.getAcceleration().getX() + "," +
                                command.getAcceleration().getY() + "," +
                                command.getAcceleration().getZ()
                );
                Log.d("eatingapp", "\t f0: " + command.getFlex(0) + " f1: " + command.getFlex(1));

                // Store last 10 commands in a queue so we can look at sensor data from the past second
                // When queue size reaches 10 pop oldest command, and push new command onto the queue
                // Look at each command in the queue
                // Calc max min acceleration of x, y, and z directions
                // Calc max min flex of all flex sensors
                // If the difference between the maxes and mins is small
                //      -> No change in movement in the last second -> not eating?
                // Else if the difference is large
                //      -> Inspect the data further, as it's possible the user ate in the past second
                //          Determining if the user ate in the past second:
                //
                //          Look at Z acceleration (Az) for each command (Z acceleration is up/down movement)
                //          If Az had a large increase from 9.8m/s^2 (9.8=no up/down movement) over the past second
                //              AND decreased back to 9.8 during the later part of the second
                //                  -> User moved hand and abruptly stopped moving their hand
                //                     This could mean that the user either put their hand to their mouth
                //                     or the user moved their hand from their mouth to their "plate"
                //                     We also know that if Az decreases then the hand is moving up. (Eg 9.8 -> 0 would mean
                //                          that hand moved up at 9.8ms2)
                //                     If this is the case, we can look at flex sensor data and if the flex sensors
                //                     are within the "eating" flex threshold, we can imply that the user ate within
                //                     the past second (?). We then log the current time as "is eating" in the database.
                //
                //                     If the difference between the last eat time and current time is smaller than some
                //                          threshold -> user is eating too fast
                //
                //                     Set lastEatTime = current time
                //          If Az did NOT have a large increase, but was also NOT constant then this implies that the user
                //              is in the middle of a movement. We can ignore this?
                //          If Az remained constant (9.6-10.2?) implies no movement -> can definitely ignore this.

//                if (command.Command.equals("a")) {
//                    String[] data = command.Data.split(",");
//                    DatabaseConnection.getInstance().getDatabase().execSQL("INSERT INTO acceleration_log (x, y, z, time) VALUES(" + data[0] + "," + data[1] + "," + data[2] + ");");
//                }
            }

        });
        newConnection.startReading();
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToMealView(View mealView)
    {
        Intent intent = new Intent(this, MealView.class);
        startActivity(intent);
    }

    public void goToHistoryView(View historyView)
    {
        Intent intent = new Intent(this, HistoryView.class);
        startActivity(intent);
    }
}

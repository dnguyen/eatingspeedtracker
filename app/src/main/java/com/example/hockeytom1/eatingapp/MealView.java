package com.example.hockeytom1.eatingapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hockeytom1.eatingapp.bluetooth.BluetoothConnection;
import com.example.hockeytom1.eatingapp.bluetooth.SensorCommand;
import com.example.hockeytom1.eatingapp.storage.DBAdapter;
import com.example.hockeytom1.eatingapp.storage.DatabaseConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Date;


public class MealView extends ActionBarActivity
{
    private MealWindow myMealWindow;
    private Button startStopButtonText;
    private TextView timeSpentEating;
    private TextView mouthfulsPerMinute;

    // Bluetooth


    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothConnection btConnection;

    float prevZ = 0;
    float currentZ = 0;
    float deltaZ = 0;
    float gravZ = 1;
    long lastEatTime = 0;
    final int MOVEMENT_UP = 1;
    final int NO_MOVEMENT = 0;
    final int MOVEMENT_DOWN = -1;
    int prevMovement = NO_MOVEMENT;
    int MIN_EAT_INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_meal_view);

        startStopButtonText = (Button) findViewById(R.id.start_button_id);
        timeSpentEating = (TextView) findViewById(R.id.time_spent_eating);
        mouthfulsPerMinute = (TextView) findViewById(R.id.mouthfuls_per_minute);
        myMealWindow = new MealWindow();

        Resources res = getResources();
        String buttonText = res.getString(R.string.start_stop_button, myMealWindow.getStartStopButton());
        String timeSpentEatingText = res.getString(R.string.time_spent_eating, myMealWindow.getTimeSpentEating());
        String mouthfulsPerMinuteText = res.getString(R.string.mouthfuls_per_minute, myMealWindow.getMouthfulsText());

        startStopButtonText.setText(buttonText);
        timeSpentEating.setText(timeSpentEatingText);
        mouthfulsPerMinute.setText(mouthfulsPerMinuteText);


        btConnection = new BluetoothConnection("HC-06");
        if (btConnection.getAdapter() != null) {

            // Check if bluetooth is enabled, if not ask user to enable it.
            if (!btConnection.getAdapter().isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                startBluetooth();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check if we're responding to enable bluetooth dialog
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                startBluetooth();
            }
        }
    }

    private void startBluetooth() {
        btConnection.findDevice();
        btConnection.setCommandProcessedHandler(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                SensorCommand command = (SensorCommand) msg.obj;

                // apply a low pass filter to remove/zero force of gravity
                final float alpha = 0.25f;
                gravZ = gravZ + alpha * (command.getAcceleration().getZ() - gravZ);
                currentZ = command.getAcceleration().getZ() - gravZ;
                deltaZ = prevZ - currentZ;

                // Calculate change in Z acceleration using the previous Z acceleration.
                // If the change is positive => hand is decelerating/"moving down"
                // If the change is negative => hand is accelerating/"moving up"
                // If no change (-1.0 to 1.0) => hand is not moving at all
                int currMovement = NO_MOVEMENT;
                if (deltaZ <= -0.75) {
                    currMovement = MOVEMENT_UP;
                } else if (deltaZ >= 0.75) {
                    currMovement = MOVEMENT_DOWN;
                }

                //Log.d("eatingapp", "deltaZ=" + deltaZ + " currentZ=" + currentZ + " prevMovement: " + prevMovement + " currMovement: " + currMovement);
                // If the previous recording recorded a up down movement and the current movement is no movement
                // then we can assume the user moved their hand to their plate and did not move their hand
                // Thus we can infer that the user ate something in this time period. We make sure the difference between
                // the current time and the time they last ate is greater than 2 second because the sensor
                // will pick up slight up/down movements when coming to a stop (acceleration increases, and
                // quickly decreases); so we can assume that if the sensor records a down -> no movement in
                // less than 2 seconds, it was caused by the sensor picking up a slight movement and we don't
                // count this as eating
                Date currentDate = new Date();
                long currentTime = currentDate.getTime();
                long deltaTime = currentTime - lastEatTime;
                if (prevMovement == MOVEMENT_DOWN && currMovement == NO_MOVEMENT && deltaTime >= 2000) {
                    // Also check flex sensor data to see if they are within eating threshold
                    if ((command.getFlex(0).getValue() >= 250 && command.getFlex(0).getValue() <= 320) && command.getFlex(1).getValue() <= 420) {
                        Log.d("acceltest", "User is eating @ " + currentDate.toString() + " time since last ate=" + deltaTime);
                        if (deltaTime <= MIN_EAT_INTERVAL) {
                            Log.d("acceltest", "\tUser is eating too fast");
                        }
                        lastEatTime = currentTime;
                    }
                }

                prevMovement = currMovement;
                prevZ = currentZ;

            }

        });

        btConnection.startReading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meal_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startStopButton(View view)
    {
        if(myMealWindow.getIsPaused() == true)
        {
            //myMealWindow.setIsPaused(false);
            //myMealWindow.setStartStopButton("Stop");
            myMealWindow.startMeal();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!myMealWindow.getIsPaused())
                    {
                        myMealWindow.timeMeal();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Resources res = getResources();
                                String timeSpentEatingText = res.getString(R.string.time_spent_eating, myMealWindow.getTimeSpentEating());
                                String mouthfulsPerMinuteText = res.getString(R.string.mouthfuls_per_minute, myMealWindow.getMouthfulsText());

                                timeSpentEating.setText(timeSpentEatingText);
                                mouthfulsPerMinute.setText(mouthfulsPerMinuteText);
                            }
                        });
                    }
                }
            }).start();
        }
        else
        {
            myMealWindow.pauseMeal();
        }
        Resources res = getResources();
        String buttonText = res.getString(R.string.start_stop_button, myMealWindow.getStartStopButton());

        startStopButtonText.setText(buttonText);
    }

    public void resetButton(View view)
    {
        myMealWindow.resetMeal();
        Resources res = getResources();
        String buttonText = res.getString(R.string.start_stop_button, myMealWindow.getStartStopButton());
        String timeSpentEatingText = res.getString(R.string.time_spent_eating, myMealWindow.getTimeSpentEating());
        String mouthfulsPerMinuteText = res.getString(R.string.mouthfuls_per_minute, myMealWindow.getMouthfulsText());

        timeSpentEating.setText(timeSpentEatingText);
        mouthfulsPerMinute.setText(mouthfulsPerMinuteText);
        startStopButtonText.setText(buttonText);
    }

    public void saveButton(View view)
    {
        myMealWindow.saveMeal();

        //HistoryElementWindow sendToHistory = new HistoryElementWindow(myMealWindow.getTimeStamp(), myMealWindow.getTimeSpentEating(), myMealWindow.getMouthfulsText());

        FileOutputStream outputStream;
        String string = myMealWindow.getTimeStamp()+" "+myMealWindow.getTimeSpentEating()+" "+myMealWindow.getMouthfulsText();

        try{
            outputStream = openFileOutput("eating_log", Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

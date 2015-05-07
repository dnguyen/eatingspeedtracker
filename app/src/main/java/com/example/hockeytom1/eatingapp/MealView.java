package com.example.hockeytom1.eatingapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

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

    private Chronometer chronometer;
    private boolean isPaused = true;
    private long timeWhenPaused = 0;
    private int totalMouthfuls = 0;
    private float avgTimeBetweenMouthfuls = 0;
    private long timeStarted = 0;

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

    // Minimum time between "bites"
    int MIN_EAT_INTERVAL = 5000;
    // Minimum value of Flex sensor 0
    int MIN_FLEX0_VALUE = 250;
    // Max value of Flex sensor 0
    int MAX_FLEX0_VALUE = 320;
    // Max value of Flex sensor 1
    int MAX_FLEX1_VALUE = 420;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_meal_view);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                long timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
                int hours = (int) (timeElapsed / 3600000);
                int minutes = (int) Math.ceil((timeElapsed - hours * 3600000) / 60000);
                if (minutes == 0) {
                    minutes = 1;
                }
                double mouthfuls = totalMouthfuls / minutes;
                Log.d("eatinglog", "total: "+ totalMouthfuls + " " + "mouthfuls: " + mouthfuls);
                mouthfulsPerMinute.setText(Double.toString(mouthfuls));
            }
        });
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
        final Context thisContext = this.getApplicationContext();
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
                    if ((command.getFlex(0).getValue() >= MIN_FLEX0_VALUE && command.getFlex(0).getValue() <= MAX_FLEX0_VALUE) && command.getFlex(1).getValue() <= MAX_FLEX1_VALUE) {
                        Log.d("acceltest", "User is eating @ " + currentDate.toString() + " time since last ate=" + deltaTime);
                        if (deltaTime <= MIN_EAT_INTERVAL) {
                            Log.d("acceltest", "\tUser is eating too fast");
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(3000);
                            Toast.makeText(thisContext, "Eating too fast!", Toast.LENGTH_SHORT).show();

                        }

                        totalMouthfuls++;
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

        if (this.isPaused) {
            this.isPaused = false;
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            chronometer.start();
            myMealWindow.setStartStopButton("Stop");
        } else {
            myMealWindow.pauseMeal();
            this.isPaused = true;
            timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
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
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenPaused = 0;
    }

    public void saveButton(View view)
    {
        myMealWindow.saveMeal();

        //HistoryElementWindow sendToHistory = new HistoryElementWindow(myMealWindow.getTimeStamp(), myMealWindow.getTimeSpentEating(), myMealWindow.getMouthfulsText());

        FileOutputStream outputStream;
        System.out.println(chronometer.getText().toString());
        String string = myMealWindow.getTimeStamp()+" "+chronometer.getText().toString()+" "+mouthfulsPerMinute.getText()+"\n";

        try{
            outputStream = openFileOutput("eating_log", Context.MODE_APPEND);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

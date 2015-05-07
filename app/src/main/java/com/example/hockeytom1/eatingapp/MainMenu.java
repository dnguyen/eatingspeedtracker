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
                Log.d("eatingapp", "Processed: " + command.Command + " " + command.Data + " " + command.Data + " " + command.Time + " (" + command.TimeMS + ")");

                if (command.Command.equals("a")) {
                    String[] data = command.Data.split(",");
                    DatabaseConnection.getInstance().getDatabase().execSQL("INSERT INTO acceleration_log (x, y, z, time) VALUES(" + data[0] + "," + data[1] + "," + data[2] + ");");
                }
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

package com.example.hockeytom1.eatingapp;

import android.bluetooth.BluetoothAdapter;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class MainMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        File eatingHistory = new File(this.getApplicationContext().getFilesDir(), "eating_log");
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

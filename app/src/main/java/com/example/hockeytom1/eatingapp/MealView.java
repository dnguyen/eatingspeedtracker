package com.example.hockeytom1.eatingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hockeytom1.eatingapp.storage.DBAdapter;
import com.example.hockeytom1.eatingapp.storage.DatabaseConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;


public class MealView extends ActionBarActivity
{
    private MealWindow myMealWindow;
    private Button startStopButtonText;
    private TextView timeSpentEating;
    private TextView mouthfulsPerMinute;

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

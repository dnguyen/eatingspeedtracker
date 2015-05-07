package com.example.hockeytom1.eatingapp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hockeytom1 on 4/21/15.
 */
public class MealWindow
{
    //Data for Timer portion
    private long milliSeconds;
    private long seconds;
    private long minutes;
    private long hours;


    private long startTime;
    private long savedTime;
    private long currentTime;

    //Data for forkfuls
    private int mouthfuls;
    private int mouthfulsPerMinute;

    //Timestamped "Title" of the meal
    private String timeStamp;
    private String startStopButton;
    private String timeSpentEating;
    private String mouthfulsText;

    //Flag for pausing the timer (true means timer is paused, false means timer is running)
    private boolean isPaused;
    //Flag for reset button

    public MealWindow()
    {
        milliSeconds=0;
        seconds=0;
        minutes=0;
        hours=0;

        startTime=0;
        savedTime=0;
        currentTime=0;

        mouthfuls=0;
        mouthfulsPerMinute=1;

        startStopButton="Start";
        timeSpentEating="00:00:00";
        mouthfulsText="0";

        isPaused=true;
    }

    public void startMeal()
    {
        startTime = System.currentTimeMillis();
        startStopButton="Stop";
        isPaused=false;
    }

    public void timeMeal()
    {
        currentTime = (System.currentTimeMillis() - startTime + savedTime)/1000;
        seconds = currentTime % 60;
        minutes = (currentTime / 60) % 60;
        hours = currentTime / 360;

        mouthfulsPerMinute = (int)(mouthfuls/(minutes+1));

        timeSpentEating = Integer.toString((int)hours/10)+Integer.toString((int)hours%10)+":"+Integer.toString((int)minutes/10)+Integer.toString((int)minutes%10)+":"+Integer.toString((int)seconds/10)+Integer.toString((int)seconds%10);
        mouthfulsText = Integer.toString(mouthfulsPerMinute);
    }

    public void pauseMeal()
    {
        isPaused=true;
        savedTime = currentTime*1000;
        startStopButton="Start";
    }

    public void resetMeal()
    {
        startStopButton="Start";
        timeSpentEating="00:00:00";
        mouthfulsText="0";

        milliSeconds=0;
        seconds=0;
        minutes=0;
        hours=0;

        startTime=0;
        savedTime=0;
        currentTime=0;

        mouthfuls=0;
        mouthfulsPerMinute=1;

        isPaused=true;
    }

    public void saveMeal()
    {
        timeStamp = new SimpleDateFormat("MM/dd/yy").format(new Date());
    }

    public String getTimeSpentEating()
    {
        return timeSpentEating;
    }

    public String getMouthfulsText()
    {
        return mouthfulsText;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public String getStartStopButton()
    {
        return startStopButton;
    }

    public void setStartStopButton(String setValue)
    {
        startStopButton = setValue;
    }

    public boolean getIsPaused()
    {
        return isPaused;
    }

    public void setIsPaused(boolean setValue)
    {
        isPaused = setValue;
    }

}



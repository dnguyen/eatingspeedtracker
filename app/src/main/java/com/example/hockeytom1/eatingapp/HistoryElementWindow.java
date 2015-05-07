package com.example.hockeytom1.eatingapp;

/**
 * Created by hockeytom1 on 5/2/15.
 */
public class HistoryElementWindow
{
    private String timeStamp;
    private String timeSpentEating;
    private String mouthfulsText;

    //Constructor should accept database element as parameter and then assign the values accordingly
    public HistoryElementWindow(String dbTimeStamp, String dbTimeSpentEating, String dbMouthfulsText)
    {
        timeStamp = dbTimeStamp;
        timeSpentEating = dbTimeSpentEating;
        mouthfulsText = dbMouthfulsText;
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
}



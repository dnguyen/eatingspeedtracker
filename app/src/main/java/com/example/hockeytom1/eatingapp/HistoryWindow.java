package com.example.hockeytom1.eatingapp;

import android.database.Cursor;

import com.example.hockeytom1.eatingapp.storage.DatabaseConnection;

import java.util.ArrayList;

/**
 * Created by hockeytom1 on 4/27/15.
 */
public class HistoryWindow
{
    private ArrayList<HistoryElementWindow> historyList;
    int numberOfRows;

    public HistoryWindow()
    {
        getRowCount();
    }

    public void getRowCount()
    {
        Cursor countCursor;
        countCursor = DatabaseConnection.getInstance().getDatabase().rawQuery("SELECT * FROM eating_log", null);
        numberOfRows = countCursor.getCount();
        countCursor.close();
    }

    public void populateList()
    {
        Cursor cursor;
        cursor = DatabaseConnection.getInstance().getDatabase().rawQuery("SELECT * FROM eating_log", null);
        if (cursor.moveToLast())
        {
            do {
                //historyList.add(new HistoryElementWindow())
            } while(cursor.moveToPrevious());
        }
    }
}

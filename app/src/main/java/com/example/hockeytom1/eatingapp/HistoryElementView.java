package com.example.hockeytom1.eatingapp;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HistoryElementView extends ActionBarActivity
{
    //private HistoryElementWindow selectedHistoryElement;
    private TextView timeStamp;
    private TextView timeSpentEating;
    private TextView mouthfulsPerMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_element_view);

        timeStamp = (TextView) findViewById(R.id.date_eaten);
        timeSpentEating = (TextView) findViewById(R.id.time_spent_eating);
        mouthfulsPerMinute = (TextView) findViewById(R.id.mouthfuls_per_minute);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history_element_view, menu);
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
}

package com.example.hockeytom1.eatingapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.hockeytom1.eatingapp.storage.DBAdapter;
import com.example.hockeytom1.eatingapp.storage.DatabaseConnection;

import java.util.ArrayList;


public class HistoryView extends ActionBarActivity {

    private ListView historyListView;
    private HistoryWindow myHistoryWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_history_view);

        historyListView = (ListView)findViewById(R.id.history_list_view);

        ArrayAdapter<HistoryElementWindow> arrayAdapter = new ArrayAdapter<HistoryElementWindow>(this, android.R.layout.simple_list_item_1, myHistoryWindow.getHistoryList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history_view, menu);
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

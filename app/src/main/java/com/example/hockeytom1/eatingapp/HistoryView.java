package com.example.hockeytom1.eatingapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.hockeytom1.eatingapp.storage.DBAdapter;
import com.example.hockeytom1.eatingapp.storage.DatabaseConnection;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class HistoryView extends ActionBarActivity {

    private ListView historyListView;
    //private HistoryWindow myHistoryWindow;
    private ArrayList<String> dateList;
    private ArrayList<String> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dateList = new ArrayList<String>();
        infoList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_history_view);
        FileInputStream inputStream;

        try{
            inputStream = openFileInput("eating_log");

            if(inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ((receiveString = bufferedReader.readLine()) != null)
                {
                    String[] tokens = receiveString.split(" ");
                    System.out.println(tokens.length);
                    tokens[1] = "T.S.E. : "+tokens[1];
                    tokens[2] = "M.P.M. : "+tokens[2];
                    tokens[1] = tokens[1]+"  "+tokens[2];
                    tokens[0] = tokens[0]+"\n"+tokens[1];
                    dateList.add(tokens[0]);
                    //infoList.add(tokens[1]);
                    System.out.println(tokens[0]);
                    //System.out.println(tokens[1]);

                    //System.out.println(tokens[2]);
                    //myHistoryWindow.getHistoryList().add(new HistoryElementWindow(tokens[0], tokens[1], tokens[2]));
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Could not read from file");
        }

        historyListView = (ListView)findViewById(R.id.history_list_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, dateList);

        historyListView.setAdapter(arrayAdapter);
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

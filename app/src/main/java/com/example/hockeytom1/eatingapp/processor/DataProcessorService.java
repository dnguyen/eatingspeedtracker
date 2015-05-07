package com.example.hockeytom1.eatingapp.processor;

import android.app.IntentService;
import android.content.Intent;

public class DataProcessorService extends IntentService {

    public DataProcessorService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        String dataString = workIntent.getDataString();
    }
}

package com.example.hockeytom1.eatingapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Singleton class for database connection
 */
public class DatabaseConnection {
    private static DatabaseConnection ourInstance;
    public static DatabaseConnection getInstance() {
        if (ourInstance == null) {
            ourInstance = new DatabaseConnection();
        }

        return ourInstance;
    }

    private SQLiteDatabase database;

    private DatabaseConnection() {
        //SqlConnection
        database = SQLiteDatabase.openOrCreateDatabase("eating_app", null);

        createTables();
    }

    private void createTables() {
//
//        //database.execSQL("CREATE TABLE IF NOT EXISTS acceleration_log(id INTEGER PRIMARY KEY AUTOINCREMENT, x REAL, y REAL, z REAL, time DATETIME);");
//        database.execSQL("CREATE TABLE IF NOT EXISTS eating_log(id INTEGER PRIMARY KEY AUTOINCREMENT, timeSpentEating REAL, mouthfulsPerMinute REAL, time REAL);");
//
//        // Create 1 table that stores history of sensor values with timestamps
//        database.execSQL(
//                "CREATE TABLE IF NOT EXISTS sensor_log(" +
//                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    "acceleration_x REAL, acceleration_y REAL, acceleration_z REAL, " +
//                    "f0 INTEGER, f1 INTEGER, f2 INTEGER, f3 INTEGER, f4 INTEGER, " +
//                    "time DATETIME" +
//                ");"
//        );
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

}

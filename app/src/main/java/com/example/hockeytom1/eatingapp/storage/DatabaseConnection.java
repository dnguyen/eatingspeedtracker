package com.example.hockeytom1.eatingapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
        database = SQLiteDatabase.openOrCreateDatabase("eatingapp", null);
        createTables();
    }

    private void createTables() {
        database.execSQL("CREATE TABLE IF NOT EXISTS acceleration_log(id INTEGER PRIMARY KEY AUTOINCREMENT, x REAL, y REAL, z REAL, time DATETIME);");
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

}

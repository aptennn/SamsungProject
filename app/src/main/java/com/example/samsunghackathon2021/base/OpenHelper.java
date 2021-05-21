package com.example.samsunghackathon2021.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



class OpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "itschool.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "MyData";

    // Название столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_TEXT = "Text";
    public static final String COLUMN_MODE = "Mode";


    // Номера столбцов
    public static final int NUM_COLUMN_ID = 0;
    public static final int NUM_COLUMN_DATE = 1;
    public static final int NUM_COLUMN_TITLE = 2;

    OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_MODE + " TEXT); ";
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
package com.example.samsunghackathon2021.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.samsunghackathon2021.State;

import java.util.ArrayList;

import static com.example.samsunghackathon2021.base.OpenHelper.COLUMN_ID;
import static com.example.samsunghackathon2021.base.OpenHelper.COLUMN_TEXT;
import static com.example.samsunghackathon2021.base.OpenHelper.COLUMN_TITLE;
import static com.example.samsunghackathon2021.base.OpenHelper.COLUMN_MODE;
import static com.example.samsunghackathon2021.base.OpenHelper.TABLE_NAME;
// import static com.example.samsunghackathon2021.base.OpenHelper.COLUMN_TEXT;
// import static com.example.samsunghackathon2021.base.OpenHelper.COLUMN_TEXT;



public class DBMatches {

    ArrayList<State> arrayList;
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_TEAMHOME = 1;
    private static final int NUM_COLUMN_TEAMGUAST = 2;
    private static final int NUM_COLUMN_GOALSHOME = 3;
    private static final int NUM_COLUMN_GOALSGUEST = 4;
    private final Context context;

    private SQLiteDatabase mDataBase;

    public DBMatches(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
        this.context = context;
    }

    public long insert(String name,String text,String mode) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE, name);
        cv.put(COLUMN_TEXT, text);
        cv.put(COLUMN_MODE, mode);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    //get the all notes
    public ArrayList<State> getNotes() {
        ArrayList<State> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;

        //SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = mDataBase.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                State noteModel = new State(1,"2", "2", "3");
                // noteModel.setID(cursor.getString(0));
                //noteModel.set(cursor.getString(1));
                //noteModel.setDes(cursor.getString(2));
                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }



    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    /*public Matches select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String TeamHome = mCursor.getString(NUM_COLUMN_TEAMHOME);
        String TeamGuest = mCursor.getString(NUM_COLUMN_TEAMGUAST);
        int GoalsHome = mCursor.getInt(NUM_COLUMN_GOALSHOME);
        int GoalsGuest=mCursor.getInt(NUM_COLUMN_GOALSGUEST);
        return new Matches(id, TeamHome, TeamGuest, GoalsHome,GoalsGuest);
    }

    public ArrayList<Matches> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Matches> arr = new ArrayList<Matches>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String TeamHome = mCursor.getString(NUM_COLUMN_TEAMHOME);
                String TeamGuest = mCursor.getString(NUM_COLUMN_TEAMGUAST);
                int GoalsHome = mCursor.getInt(NUM_COLUMN_GOALSHOME);
                int GoalsGuest=mCursor.getInt(NUM_COLUMN_GOALSGUEST);
                arr.add(new Matches(id, TeamHome, TeamGuest, GoalsHome,GoalsGuest));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    */

}
package com.example.samsunghackathon2021.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME = "11zon";
    //database version
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "tbl_notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        //creating table
        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT, Mode TEXT, use INTEGER DEFAULT 0)";
        db.execSQL(query);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add the new note
    public void addNotes(String title, String des, String mode) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Description", des);
        values.put("Mode", mode);

        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    public String selectMode(int id){
        String mode = "";
        id++;
        //String select_query= "SELECT * FROM " + TABLE_NAME + " WHERE ID=" + id;

        SQLiteDatabase db = this .getWritableDatabase();
        String idd = id + "";
        //Cursor cursor = db.rawQuery(select_query, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TRIM(ID) = '"+idd.trim()+"'", null);
        if (cursor.moveToFirst()){
            do {
                // Passing values
                String column1 = cursor.getString(0);
                String column2 = cursor.getString(1);
                String column3 = cursor.getString(2);
                String column4 = cursor.getString(3);
                mode = column4;
                Log.d("basee", "" + column1 + " " + column2 + " " + column3 + " " + column4);
                // Do something Here with values
            } while(cursor.moveToNext());
        }

        return mode;
    }

    public String[] selectUse(){
        SQLiteDatabase db = this .getWritableDatabase();
        int one = 1;
        String[] myList = new String[2];
        myList[0] = "Профиль не выбран";
        myList[1] = "Пусто";

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TRIM(use) = '"+one+"'", null);
        if (cursor.moveToFirst()){
            do {
                // Passing values
                String column1 = cursor.getString(0);
                String column2 = cursor.getString(1);
                String column3 = cursor.getString(2);
                String column4 = cursor.getString(3);
                myList[0] = column2;
                myList[1] = column3;
                Log.d("usage1", "" + column1 + " " + column2 + " " + column3 + " " + column4);
                return myList;
                // Do something Here with values
            } while(cursor.moveToNext());

        }
        return myList;
    }

    public String selectModeInFrag(int id){
        String mode = "";
        id++;
        //String select_query= "SELECT * FROM " + TABLE_NAME + " WHERE ID=" + id;

        SQLiteDatabase db = this .getWritableDatabase();
        String idd = id + "";
        //Cursor cursor = db.rawQuery(select_query, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TRIM(ID) = '"+idd.trim()+"'", null);
        int one = 1;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TRIM(use) = '"+one+"'", null);
        ContentValues values =  new ContentValues();
        values.put("use", 0);
        db.update(TABLE_NAME, values, "use=" + one, null);
        db.close();


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values2 =  new ContentValues();
        values2.put("use", 1);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values2, "ID=" + idd, null);
        sqLiteDatabase.close();


        return mode;
    }


    //get the all notes
    public ArrayList<NoteModel> getNotes() {
        ArrayList<NoteModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getString(0));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setDes(cursor.getString(2));
                noteModel.setMode(cursor.getString(3));
                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TABLE_NAME, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateNote(String title, String des, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("Title", title);
        values.put("Description", des);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }
}
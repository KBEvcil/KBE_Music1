package com.example.test;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "app.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(ID INTEGER  PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL, fullname TEXT NOT NULL)");
        db.execSQL("CREATE TABLE music(ID INTEGER PRIMARY KEY AUTOINCREMENT, filename TEXT NOT NULL, name TEXT NOT NULL, composer TEXT, date DATE )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS music");
    }

    public boolean insertUser(String username, String password, String email, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("fullname", fullName);
        long result = db.insert("user", null, contentValues);
        return result != -1;

    }

    public boolean insertMusic(String filename, String name, String composer, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("filename",filename);
        contentValues.put("name",name);
        contentValues.put("composer",composer);
        contentValues.put("date",date.toString());
        long result = db.insert("music", null, contentValues);
        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username = ?",new String[]{username});
        return cursor.getCount() == 0;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?", new String[]{username, password});
        return cursor.getCount() != 0;
    }


}

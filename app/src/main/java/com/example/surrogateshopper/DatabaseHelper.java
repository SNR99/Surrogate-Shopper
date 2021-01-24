package com.example.surrogateshopper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
  String databaseName = "userData1.db";

  public DatabaseHelper(Context context) {
    super(context, "userData5.db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("drop Table if exists User");

    db.execSQL(
        "Create table User(id INTEGER PRIMARY KEY AUTOINCREMENT,  user_id text not null unique, name text not null , surname text not null , email text not null unique, user_type text not null )");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop Table if exists User");
    onCreate(db);
  }

  public Boolean insertData(
      String user_id, String name, String surname, String email, String user_type) {
    SQLiteDatabase DB = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("user_id", user_id);
    contentValues.put("name", name);
    contentValues.put("surname", surname);
    contentValues.put("email", email);
    contentValues.put("user_type", user_type);
    long result = DB.insert("User", null, contentValues);
    if (result == -1) {
      return false;
    } else {
      return true;
    }
  }

  public void deleteAll() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("drop Table if exists User");
    onCreate(db);
  }

  public boolean checkUser(String user_id) {
    SQLiteDatabase DB = this.getWritableDatabase();
    Cursor cursor = DB.rawQuery("Select * from User where user_id =?", new String[] {user_id});

    if (cursor.moveToFirst()) {
      return true;
    } else {
      return false;
    }
  }

  public String getEmail() {
    SQLiteDatabase DB = this.getWritableDatabase();
    Cursor c1 = DB.rawQuery("Select email from User", null);

    if (c1.moveToFirst()) {
      return c1.getString(c1.getColumnIndex("email"));
    }
    return "";
  }

  public String getFullName() {
    return getName() + " " + getSurname();
  }

  public String getName() {
    String Name = "";
    SQLiteDatabase DB = this.getWritableDatabase();
    Cursor c1 = DB.rawQuery("Select name from User", null);

    if (c1.moveToFirst()) {
      Name = c1.getString(c1.getColumnIndex("name"));
    }
    return Name.substring(0, 1).toUpperCase() + Name.substring(1);
  }

  public String getSurname() {
    String Surname = "";
    SQLiteDatabase DB = this.getWritableDatabase();
    Cursor c1 = DB.rawQuery("Select surname from User", null);

    if (c1.moveToFirst()) {
      Surname = c1.getString(c1.getColumnIndex("surname"));
    }
    return Surname.substring(0, 1).toUpperCase() + Surname.substring(1);
  }

  public String getUserID() {
    SQLiteDatabase DB = this.getWritableDatabase();
    Cursor cursor = DB.rawQuery("Select user_id from User", null);
    if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex("user_id"));

    return "";
  }

  public Cursor getData() {
    SQLiteDatabase DB = this.getWritableDatabase();
    return DB.rawQuery("Select * from User", null);
  }
}

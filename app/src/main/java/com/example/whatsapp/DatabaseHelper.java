package com.example.whatsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "whatsapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_UID = "uid";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_UID + " TEXT PRIMARY KEY,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_PHONE_NUMBER + " TEXT,"
            + COLUMN_PROFILE_IMAGE + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public static void addUser(Context context, User user) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, user.getUid());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_PROFILE_IMAGE, user.getProfileImage());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public static ArrayList<User> getAllUsers(Context context) {
        ArrayList<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            User user = new User();

            user.setUid(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
            user.setProfileImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_IMAGE)));
            userList.add(user);
        }

        cursor.close();
        db.close();
        return userList;
    }


    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
}

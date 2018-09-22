package com.example.sushil.safe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.ContactContract;
import data.ContactContract.ContactEntry;

public class DetailDbHelper extends SQLiteOpenHelper {

    public static final String TAG = DetailDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "detail.db";

    private static final int DATABASE_VERSION =1;

    public DetailDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_DETAIL;
        SQL_CREATE_DETAIL = "CREATE TABLE "+ ContactContract.DetailsEntry.TABLE_NAME + " ("
                + ContactContract.DetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactContract.DetailsEntry.COLUMN_DETAIL_NAME + " TEXT NOT NULL, "
                + ContactContract.DetailsEntry.COLUMN_DETAIL_NUMBER + " INTEGER NOT NULL UNIQUE, "
                + ContactContract.DetailsEntry.COLUMN_DETAIL_EMAIL + "TEXT, "
                + ContactContract.DetailsEntry.COLUMN_DETAIL_PASSWORD + "TEXT NOT NULL, "
                + ContactContract.DetailsEntry.COLUMN_DETAIL_ADDRESS + "TEXT " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

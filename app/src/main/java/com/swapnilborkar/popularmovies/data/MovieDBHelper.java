package com.swapnilborkar.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "favoritemovies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE" + MovieContract.Favorites.TABLE_NAME + "( " +
                MovieContract.Favorites.COLUMN_MOVIEID + "TEXT NOT NULL" +
                MovieContract.Favorites.COLUMN_TITLE + "TEXT NOT NULL" +
                MovieContract.Favorites.COLUMN_POSTER + "TEXT NOT NULL" +
                MovieContract.Favorites.COLUMN_RATING + "REAL NOT NULL" +
                MovieContract.Favorites.COLUMN_POPULARITY + "REAL NOT NULL" +
                MovieContract.Favorites.COLUMN_RELEASEDATE + "REAL NOT NULL" +
                MovieContract.Favorites.COLUMN_SYNOPSIS + "TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.Favorites.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}

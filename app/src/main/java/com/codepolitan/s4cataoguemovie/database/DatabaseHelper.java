package com.codepolitan.s4cataoguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_DESCRIPTION;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_ID;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_POSTER;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_RELEASE_DATE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_TITLE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "db_catalogue";
    private static final int DB_VERSION = 1;

    public static String CREATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_NAME + " (" +
            FIELD_ID + " INTEGER primary key, " +
            FIELD_TITLE + " TEXT, " +
            FIELD_DESCRIPTION + " TEXT, " +
            FIELD_POSTER + " TEXT, " +
            FIELD_RELEASE_DATE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.rawQuery("DROP TABLE IF EXISTS " + TABLE_NAME, null);
        onCreate(db);
    }
}

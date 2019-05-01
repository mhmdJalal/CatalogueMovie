package com.codepolitan.s4cataoguemovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.codepolitan.s4cataoguemovie.model.Movie;
import com.codepolitan.s4cataoguemovie.response.MovieDetailResponse;

import java.util.ArrayList;
import java.util.List;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_DESCRIPTION;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_ID;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_POSTER;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_RELEASE_DATE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_TITLE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.TABLE_NAME;

public class FavoriteHelper {

    private static String FAVORITE = TABLE_NAME;

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase writeDatabase, readDatabase;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public FavoriteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        writeDatabase = databaseHelper.getWritableDatabase();
        readDatabase = databaseHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public boolean getData(String id) {
        boolean isExist = false;

        Cursor cursor = readDatabase.query(FAVORITE, null, FIELD_ID + "= ?", new String[]{id},null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            isExist = true;
        }
        cursor.close();
        return isExist;
    }

    public List<Movie> getAllData() {
        String sql = "SELECT * FROM " + FAVORITE + ";";

        List<Movie> movies = new ArrayList<>();
        Cursor cursor = readDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String poster = cursor.getString(3);
                String release = cursor.getString(4);

                movies.add(new Movie(id, title, description, poster, release));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movies;
    }

    public void insert(MovieDetailResponse movie) {
        ContentValues args = new ContentValues();
        args.put(FIELD_ID, movie.getId());
        args.put(FIELD_TITLE, movie.getTitle());
        args.put(FIELD_DESCRIPTION, movie.getOverview());
        args.put(FIELD_POSTER, movie.getPoster_path());
        args.put(FIELD_RELEASE_DATE, movie.getRelease_date());
        writeDatabase.insert(FAVORITE, null, args);
    }

    public void delete(int id) {
        writeDatabase.delete(FAVORITE, FIELD_ID + " = '" + id + "'", null);
    }

    /* Provider Helper */
    public Cursor queryByIdProvider(String id) {
        return readDatabase.query(
                TABLE_NAME,
                null,
                FIELD_ID + " = ?",
                new String[]{id},
                null,
                null,
                null);
    }

    public Cursor queryProvider() {
        return readDatabase.query(TABLE_NAME, null, null, null, null, null, "");
    }

    public long  insertProvider(ContentValues contentValues) {
        return writeDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public int deleteProvider(String id) {
        return writeDatabase.delete(FAVORITE, FIELD_ID + " = '" + id + "'", null);
    }
}

package com.codepolitan.s4cataoguemovie.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String AUTHORITY = "com.codepolitan.s4cataoguemovie";
    public static final String SCHEME = "content";

    public DatabaseContract() {
    }

    public static final class FavoriteColumn implements BaseColumns {
        public static String TABLE_NAME = "tb_favorite";

        public static String FIELD_ID = "idMovie";
        public static String FIELD_POSTER = "poster";
        public static String FIELD_TITLE = "title";
        public static String FIELD_DESCRIPTION = "description";
        public static String FIELD_RELEASE_DATE = "releaseDate";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}

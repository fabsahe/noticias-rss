package com.noticiasrss.data.sqlite.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDBHelper extends SQLiteOpenHelper {

    private static NewsDBHelper helper;

    private static final String DB_NAME = "news_agg_db";
    private static final int DB_VERSION = 1;

    public static final String DB_SOURCES_TABLE = "sources";
    private static final String SOURCES_TABLE_CREATION_CMD =
            "create table %s (%s integer primary key autoincrement, " +
            "%s text, " +
            "%s real, " +
            "%s real);";

    public static final String ID = "_id";
    public static final String SITE_LINK = "site_link";
    public static final String ADD_DATE = "add_date";
    public static final String LAST_DATE = "last_date";

    public NewsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static NewsDBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new NewsDBHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDB(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDB(db, oldVersion, newVersion);
    }

    private void updateDB(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL(String.format(SOURCES_TABLE_CREATION_CMD, DB_SOURCES_TABLE, ID, SITE_LINK,
                    ADD_DATE, LAST_DATE));
        }
    }
}

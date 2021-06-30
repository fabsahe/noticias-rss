package com.noticiasrss.data.sqlite.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseRepository {
    private NewsDBHelper helper;
    protected SQLiteDatabase db;

    Context context;

    public BaseRepository(Context context) {
        this.context = context;
    }

    protected void getConnectionToDB() {
        helper = NewsDBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }
}

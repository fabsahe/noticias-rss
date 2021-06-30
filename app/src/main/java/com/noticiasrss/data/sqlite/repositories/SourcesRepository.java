package com.noticiasrss.data.sqlite.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.noticiasrss.data.sqlite.entities.Source;

import java.util.ArrayList;
import java.util.List;

public class SourcesRepository extends BaseRepository{

    public SourcesRepository(Context context) {
        super(context);
    }

    public long addNewSource(String siteLink, long addDate) {
        getConnectionToDB();

        long id = -1;

        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(NewsDBHelper.SITE_LINK, siteLink);
            cv.put(NewsDBHelper.ADD_DATE, addDate);
            cv.put(NewsDBHelper.LAST_DATE, addDate);

            id = db.insert(NewsDBHelper.DB_SOURCES_TABLE, null, cv);

            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
        return id;
    }

    public List<Source> getAllSources() {
        getConnectionToDB();

        List<Source> sources = new ArrayList<>();
        Cursor c = db.query(NewsDBHelper.DB_SOURCES_TABLE, new String[] {
                NewsDBHelper.ID, NewsDBHelper.SITE_LINK, NewsDBHelper.ADD_DATE,
                NewsDBHelper.LAST_DATE}, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                Source source = new Source();
                source.setId(c.getInt(c.getColumnIndex(NewsDBHelper.ID)));
                source.setSiteLink(c.getString(c.getColumnIndex(NewsDBHelper.SITE_LINK)));
                source.setAddDate(c.getLong(c.getColumnIndex(NewsDBHelper.ADD_DATE)));
                source.setLastDate(c.getLong(c.getColumnIndex(NewsDBHelper.LAST_DATE)));

                sources.add(source);

            } while (c.moveToNext());
            c.close();
        }
        return sources;
    }

    public void editSourceLastDate(int id, long lastDate) {
        getConnectionToDB();

        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(NewsDBHelper.LAST_DATE, lastDate);

            db.update(NewsDBHelper.DB_SOURCES_TABLE, cv,
                    "_id = ?", new String[] {Integer.toString(id)});

            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

}

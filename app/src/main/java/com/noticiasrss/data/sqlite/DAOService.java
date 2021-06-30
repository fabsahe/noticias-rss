package com.noticiasrss.data.sqlite;

import android.content.Context;

import com.noticiasrss.data.sqlite.entities.Source;
import com.noticiasrss.data.sqlite.repositories.SourcesRepository;

import java.util.List;

public class DAOService {
    private Context context;
    private static DAOService daoService;

    private SourcesRepository sourcesRepository;

    private DAOService(Context context) {
        this.context = context;

        sourcesRepository = new SourcesRepository(context);
    }

    public static DAOService getInstance(Context context) {
        if (daoService == null) {
            daoService = new DAOService(context);
        }
        return daoService;
    }

    public long addNewSource(String siteLink, long addDate) {
        return sourcesRepository.addNewSource(siteLink, addDate);
    }

    public List<Source> getAllSources() {
        return sourcesRepository.getAllSources();
    }

    public void editSourceLastDate(int id, long lastDate) {
        sourcesRepository.editSourceLastDate(id, lastDate);
    }

}

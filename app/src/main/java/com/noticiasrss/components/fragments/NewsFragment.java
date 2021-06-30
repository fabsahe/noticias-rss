package com.noticiasrss.components.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noticiasrss.R;
import com.noticiasrss.components.fragments.adapters.NewsAdapter;
import com.noticiasrss.data.parsers.HttpsXmlParser;

import java.util.List;

public class NewsFragment extends Fragment implements NewsAdapter.Listener {

    private List<HttpsXmlParser.Item> items;
    private Listener listener;

    public void setItems(List<HttpsXmlParser.Item> items) {
        this.items = items;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView newsList = (RecyclerView) inflater.inflate(R.layout.fragment_news, container, false);
        newsList.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        NewsAdapter newsAdapter = new NewsAdapter(items);
        newsAdapter.setListener(this);
        newsList.setAdapter(newsAdapter);

        return newsList;
    }

    @Override
    public void onClick(String link) {
        if (listener != null) {
            listener.openArticle(link);
        }
    }

    public interface Listener {
        void openArticle(String link);
    }
}
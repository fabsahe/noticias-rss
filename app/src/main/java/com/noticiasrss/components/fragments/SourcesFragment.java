package com.noticiasrss.components.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noticiasrss.R;
import com.noticiasrss.components.fragments.adapters.SourceAdapter;
import com.noticiasrss.data.parsers.HttpsXmlParser;

import java.util.List;

public class SourcesFragment extends Fragment implements SourceAdapter.Listener {

    private Listener listener;
    private List<HttpsXmlParser.Channel> channels;

    public void setChannels(List<HttpsXmlParser.Channel> channels) {
        this.channels = channels;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView sourceList = (RecyclerView) inflater.inflate(R.layout.fragment_sources, container, false);
        sourceList.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        SourceAdapter adapter = new SourceAdapter(channels);
        adapter.setListener(this);
        sourceList.setAdapter(adapter);

        return sourceList;
    }

    @Override
    public void onClick(long sourceId, String link) {
        if (listener != null) {
            listener.openNews(sourceId, link);
        }
    }

    public interface Listener {
        void openNews(long sourceId, String link);
    }
}
package com.noticiasrss.components.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.noticiasrss.R;
import com.noticiasrss.components.fragments.SourcesFragment;
import com.noticiasrss.data.parsers.HttpsXmlParser;
import com.noticiasrss.data.sqlite.DAOService;
import com.noticiasrss.data.sqlite.entities.Source;

import java.util.Calendar;
import java.util.List;

public class SourceActivity extends BaseActivity implements SourcesFragment.Listener {

    private DAOService daoService;
    private List<HttpsXmlParser.Channel> channels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        setToolbar(R.id.toolbar, false);

        daoService = DAOService.getInstance(this);

        daoService.addNewSource("https://www.reforma.com/rss/portada.xml", Calendar.getInstance().getTimeInMillis());

        new ChannelsLoader().execute(daoService.getAllSources());
    }

    public void addNewSource(View view) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.activity_source_dialog_title))
                .setView(R.layout.add_source_dialog)
                .setNeutralButton(
                        getString(R.string.activity_source_dialog_neutral_button),
                        ((dialogInterface, i) -> dialogInterface.dismiss())
                )
                .setPositiveButton(
                        getString(R.string.activity_source_dialog_positive_button),
                        (this::saveNewAddress)
                )
                .setCancelable(false)
                .show();
    }

    private void saveNewAddress(DialogInterface dialog, int i) {
        EditText dialogEditView = ((AlertDialog) dialog)
                .findViewById(R.id.add_source_dialog_edit_text);
        String rssFeedUrl = dialogEditView.getText().toString();
        new ChannelAvailabilityChecker().execute(rssFeedUrl);
    }

    private void replaceSourceFragment() {
        SourcesFragment sourcesFragment = new SourcesFragment();
        sourcesFragment.setChannels(channels);
        sourcesFragment.setListener(this);
        replaceFragment(sourcesFragment, R.id.activity_source_fragment_container);
    }

    @Override
    public void openNews(long sourceId, String link) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra(NewsActivity.RSS_LINK, link);
        startActivity(intent);

        daoService.editSourceLastDate((int) sourceId, Calendar.getInstance().getTimeInMillis());
    }

    private class ChannelAvailabilityChecker extends AsyncTask<String, Void, Boolean> {
        String rssFeedUrl = "";
        HttpsXmlParser.Channel newChannel;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            rssFeedUrl = strings[0];
            List<HttpsXmlParser.Channel> channelList = getChannels(rssFeedUrl);
            if (!channelList.isEmpty()) {
                newChannel = channelList.get(0);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isChannelAvailable) {
            super.onPostExecute(isChannelAvailable);

            if (isChannelAvailable) {
                long id = daoService.addNewSource(rssFeedUrl, Calendar.getInstance().getTimeInMillis());
                showToast(R.string.activity_source_dialog_address_is_saved);
                newChannel.setDbSourceId(id);
                channels.add(newChannel);
                replaceSourceFragment();
            } else {
                showToast(R.string.activity_source_dialog_address_is_not_available);
            }
        }
    }

    private class ChannelsLoader extends AsyncTask<List<Source>, Void, List<HttpsXmlParser.Channel>> {
        @Override
        protected List<HttpsXmlParser.Channel> doInBackground(List<Source>... sources) {
            String[] rssFeedUrls = new String[sources[0].size()];
            for (int i = 0; i < rssFeedUrls.length; i++) {
                rssFeedUrls[i] = sources[0].get(i).getSiteLink();
            }

            return consistDBAndChannels(sources[0], getChannels(rssFeedUrls));
        }

        @Override
        protected void onPostExecute(List<HttpsXmlParser.Channel> channelList) {
            super.onPostExecute(channelList);
            channels = channelList;
            replaceSourceFragment();
        }
    }

    private List<HttpsXmlParser.Channel> consistDBAndChannels(
            List<Source> sources, List<HttpsXmlParser.Channel> channels
    ) {
        if (sources.size() == channels.size()) {
            for (int i = 0; i < sources.size(); i++) {
                channels.get(i).setDbSourceId(sources.get(i).getId());
            }
        }
        return channels;
    }
}
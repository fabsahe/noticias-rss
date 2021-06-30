package com.noticiasrss.components.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.noticiasrss.R;
import com.noticiasrss.components.fragments.NewsFragment;
import com.noticiasrss.data.parsers.HttpsXmlParser;

import java.util.List;

public class NewsActivity extends BaseActivity implements NewsFragment.Listener {

    public static final String RSS_LINK = "rss_link";
    private String rssLink;
    private List<HttpsXmlParser.Item> itemList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            rssLink = arguments.getString(RSS_LINK);
        }
        toolbar = setToolbar(R.id.toolbar_news, true);

        new NewsLoader().execute(rssLink);
    }

    private void replaceNewsFragment() {
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setItems(itemList);
        newsFragment.setListener(this);
        replaceFragment(newsFragment, R.id.activity_news_fragment_container);
    }

    @Override
    public void openArticle(String link) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.ARTICLE_LINK, link);
        startActivity(intent);
    }

    private class NewsLoader extends AsyncTask<String, Void, List<HttpsXmlParser.Channel>> {
        @Override
        protected List<HttpsXmlParser.Channel> doInBackground(String... strings) {
            String rssFeedUrl = strings[0];
            return getChannels(rssFeedUrl);
        }

        @Override
        protected void onPostExecute(List<HttpsXmlParser.Channel> channels) {
            super.onPostExecute(channels);
            itemList = channels.get(0).getItems();
            toolbar.setTitle(channels.get(0).getTitle());
            replaceNewsFragment();
        }
    }
}
package com.noticiasrss.components.activities;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.noticiasrss.R;
import com.noticiasrss.components.fragments.ArticleFragment;

public class ArticleActivity extends BaseActivity {

    public static final String ARTICLE_LINK = "article_link";
    private String articleLink;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            articleLink = arguments.getString(ARTICLE_LINK);
        }
        toolbar = setToolbar(R.id.toolbar_article, true);

        replaceArticleFragment();
    }

    private void replaceArticleFragment() {
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArticleLink(articleLink);
        replaceFragment(articleFragment, R.id.activity_article_fragment_container);
    }

    public class ArticleWebViewClient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String newTitle = view.getTitle();
            toolbar.setTitle(newTitle);
        }
    }
}
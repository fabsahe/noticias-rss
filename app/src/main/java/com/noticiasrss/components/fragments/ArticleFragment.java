package com.noticiasrss.components.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.noticiasrss.R;
import com.noticiasrss.components.activities.ArticleActivity;

import java.util.Objects;

public class ArticleFragment extends Fragment {

    private String articleLink;

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View articleView = inflater.inflate(R.layout.fragment_article, container, false);

        WebView articleWebView = articleView.findViewById(R.id.fragment_article_web_view);
        WebSettings webSettings = articleWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        articleWebView.setWebViewClient(
                ((ArticleActivity) Objects.requireNonNull(getActivity())).new ArticleWebViewClient());
        articleWebView.loadUrl(articleLink);

        return articleView;
    }
}
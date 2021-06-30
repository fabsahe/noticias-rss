package com.noticiasrss.components.activities;

import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.noticiasrss.connections.net.ConnectionService;
import com.noticiasrss.data.parsers.HttpsXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Toolbar setToolbar(int toolbarId, boolean isUpEnabled) {
        Toolbar toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isUpEnabled);
        setToolbarTitleMoving(toolbar);
        return toolbar;
    }

    protected void setToolbarTitleMoving(Toolbar toolbar) {
        TextView titleTextView = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);

            titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titleTextView.setFocusable(true);
            titleTextView.setFocusableInTouchMode(true);
            titleTextView.requestFocus();
            titleTextView.setSingleLine(true);
            titleTextView.setSelected(true);
            titleTextView.setMarqueeRepeatLimit(-1);

        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        }
    }

    protected void replaceFragment(Fragment fragment, int containerId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerId, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    protected void showToast(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    protected List<HttpsXmlParser.Channel> getChannels(String... rssFeedUrls) {
        List<HttpsXmlParser.Channel> channelList = new ArrayList<>();
        HttpsXmlParser parser = new HttpsXmlParser();
        for (String rssFeedUrl : rssFeedUrls) {
            parser.setRssLink(rssFeedUrl);
            try {
                HttpsURLConnection connection = ConnectionService.getConnection(rssFeedUrl);
                List<HttpsXmlParser.Channel> parsedSources = parser.parseChannels(connection);
                channelList.addAll(parsedSources);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        return channelList;
    }
}

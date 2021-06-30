package com.noticiasrss.data.parsers;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HttpsXmlParser {
    private static final String ns = null;
    private static final String RSS_TAG = "rss";
    private static final String CHANNEL_TAG = "channel";
    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";
    private static final String IMAGE_TAG = "image";
    private static final String URL_TAG = "url";
    private static final String PUB_DATE_TAG = "pubDate";
    private static final String LINK_TAG = "link";
    private static final String IMG_TAG = "img";
    private static final String SRC_TAG = "src";

    private String rssLink;

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public List<Channel> parseChannels(HttpsURLConnection conn) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(conn.getInputStream(), null);
            parser.nextTag();

            return readChannels(parser);
        } finally {
            conn.disconnect();
        }
    }

    private List<Channel> readChannels(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Channel> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, RSS_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals(CHANNEL_TAG)) {
                entries.add(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Channel readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, CHANNEL_TAG);
        String title = null;
        String description = null;
        String imageLink = null;
        List<Item> items = new ArrayList<>();
         while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TITLE_TAG:
                    title = readTitle(parser);
                    break;
                case DESCRIPTION_TAG:
                    description = readDescription(parser);
                    break;
                case IMAGE_TAG:
                    imageLink = readImageLink(parser);
                    break;
                case ITEM_TAG:
                    items.add(readItem(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Channel(title, description, rssLink, imageLink, items);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readSimpleTextFromTag(parser, TITLE_TAG);
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readSimpleTextFromTag(parser, DESCRIPTION_TAG);
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readSimpleTextFromTag(parser, PUB_DATE_TAG);
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readSimpleTextFromTag(parser, LINK_TAG);
    }

    private String readSimpleTextFromTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String textFromTag = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return textFromTag;
    }

    private String readImageLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String url = null;
        parser.require(XmlPullParser.START_TAG, ns, IMAGE_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(URL_TAG)) {
                url = readText(parser);
            } else {
                skip(parser);
            }
        }
        return url;
    }

    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ITEM_TAG);
        String title = null;
        String description = null;
        String pubDate = null;
        String link = null;
        String imageLink = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            switch (name) {
                case TITLE_TAG:
                    title = readTitle(parser);
                    break;
                case DESCRIPTION_TAG:
                    description = readDescription(parser);
                    break;
                case PUB_DATE_TAG:
                    pubDate = readPubDate(parser);
                    break;
                case LINK_TAG:
                    link = readLink(parser);
                    break;
                //TODO доработать, чтобы можно было картинку вставить
                case IMG_TAG:
                    if (imageLink == null) {
                        imageLink = readPostImageLink(parser);
                    }
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Item(title, description, pubDate, link, imageLink);
    }

    private String readPostImageLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, IMG_TAG);
        return parser.getAttributeValue(ns, SRC_TAG);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static class Channel {
        private String title;
        private String description;
        private String link;
        private String imageLink;
        private List<Item> items;
        private long dbSourceId;

        public Channel(String title, String description, String link, String imageLink, List<Item> items) {
            this.title = title;
            this.description = description;
            this.link = link;
            this.imageLink = imageLink;
            this.items = items;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getLink() {
            return link;
        }

        public String getImageLink() {
            return imageLink;
        }

        public List<Item> getItems() {
            return items;
        }

        public long getDbSourceId() {
            return dbSourceId;
        }

        public void setDbSourceId(long dbSourceId) {
            this.dbSourceId = dbSourceId;
        }
    }

    public static class Item {
        private String title;
        private String description;
        private String pubDate;
        private String link;
        private String imageLink;

        public Item(String title, String description, String pubDate, String link, String imageLink) {
            this.title = title;
            this.description = description;
            this.pubDate = pubDate;
            this.link = link;
            this.imageLink = imageLink;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPubDate() {
            return pubDate;
        }

        public String getLink() {
            return link;
        }

        public String getImageLink() {
            return imageLink;
        }
    }
}

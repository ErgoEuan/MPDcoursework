package org.me.gcu.EQS1707289;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    public List<Earthquake> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("channel")) {
                entries.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
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

    private List<Earthquake> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Earthquake readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");

        Earthquake newEarthquake = new Earthquake(null,null,null,
                null,null,null,null,null,null,
                null,null);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                parser.require(XmlPullParser.START_TAG, null, "title");
                newEarthquake.setTitle(readText(parser));
                parser.require(XmlPullParser.END_TAG, null, "title");
            } else if (name.equals("link")) {
                parser.require(XmlPullParser.START_TAG, null, "link");
                newEarthquake.setLink(readText(parser));
                parser.require(XmlPullParser.END_TAG, null, "link");
            } else if (name.equals("pubDate")) {
                parser.require(XmlPullParser.START_TAG, null, "pubDate");
                newEarthquake.setPublishedDate(readText(parser));
                parser.require(XmlPullParser.END_TAG, null, "pubDate");
            } else if (name.equals("category")) {
                parser.require(XmlPullParser.START_TAG, null, "category");
                newEarthquake.setCategory(readText(parser));
                parser.require(XmlPullParser.END_TAG, null, "category");
            } else if (name.equals("geo:lat")) {
                parser.require(XmlPullParser.START_TAG, null, "geo:lat");
                newEarthquake.setLocationLat(Double.parseDouble(readText(parser)));
                parser.require(XmlPullParser.END_TAG, null, "geo:lat");
            } else if (name.equals("geo:long")) {
                parser.require(XmlPullParser.START_TAG, null, "geo:long");
                newEarthquake.setLocationLong(Double.parseDouble(readText(parser)));
                parser.require(XmlPullParser.END_TAG, null, "geo:long");
            } else if (name.equals("description")) {
                parser.require(XmlPullParser.START_TAG, null, "description");
                filterDescription(readText(parser), newEarthquake);
                parser.require(XmlPullParser.END_TAG, null, "description");
            } else {
                skip(parser);
            }
        }
        return newEarthquake;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void filterDescription(String description, Earthquake newEarthquake) {
        String[] arrayOfDesc = description.split(";");
        newEarthquake.setOriginDate(arrayOfDesc[0].substring(23, arrayOfDesc[0].length() -10 ));
        newEarthquake.setLocation(arrayOfDesc[1].substring(11));
        newEarthquake.setDepth(Integer.parseInt(arrayOfDesc[3].substring(8, arrayOfDesc[3].length() -4)));
        newEarthquake.setMagnitude(Double.parseDouble(arrayOfDesc[4].substring(13)));
    }

}

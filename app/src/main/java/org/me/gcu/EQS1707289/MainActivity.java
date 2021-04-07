package org.me.gcu.EQS1707289;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import org.me.gcu.EQS1707289.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnClickListener {

    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ListView listView;
    private TextView dateText;
    private Spinner navSpinner;
    private GoogleMap mMap;
    private EarthquakeListViewAdapter adapter;
    private List<Earthquake> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        dateText = findViewById(R.id.dateText);

        navSpinner = findViewById(R.id.navSpinner);

        listView = (ListView) findViewById(R.id.listView);
        Log.e("MyTag","after startButton");
        // More Code goes here

        values = new ArrayList<>();
        adapter = new EarthquakeListViewAdapter(this, R.layout.list_item,values);
        listView.setAdapter(adapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startProgress();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng uk = new LatLng(54, -1.85);
        mMap.addMarker(new MarkerOptions()
                .position(uk)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uk));
    }

    public void onClick(View aview) {
        Log.e("MyTag","in onClick");
        startProgress();
        Log.e("MyTag","after startProgress");
    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //


    private List<Earthquake> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
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
        newEarthquake.setOriginDate(arrayOfDesc[0].substring(18));
        newEarthquake.setLocation(arrayOfDesc[1].substring(11));
        newEarthquake.setDepth(Integer.parseInt(arrayOfDesc[3].substring(8, arrayOfDesc[3].length() -4)));
        newEarthquake.setMagnitude(Double.parseDouble(arrayOfDesc[4].substring(13)));
    }


    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag","in run");

            try {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }

            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new ByteArrayInputStream(result.getBytes()), null);
                parser.nextTag();
                values.clear();
                values.addAll(readFeed(parser));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

//            adapter.
            //
            // Now that you have the xml data you can parse it
            //


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            // adapter.notifyDataSetChanged

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                    Log.d("UI thread", "I am the UI thread");
//                    rawDataDisplay.setText(result);
                }
            });
        }

    }

}
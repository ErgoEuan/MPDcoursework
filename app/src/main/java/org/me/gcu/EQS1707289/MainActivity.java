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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import org.me.gcu.EQS1707289.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnClickListener {

    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ListView listView;
    private TextView dateText;
    private Spinner navSpinner;
    private GoogleMap mMap;

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

        List<Earthquake> values = new ArrayList<>();
        values.add(
                new Earthquake(
                        "UK Earthquake alert : M  1.4 :MULL,ARGYLL AND BUTE, Thu, 01 Apr 2021 22:25:34",
                        "Origin date/time: Thu, 01 Apr 2021 22:25:34 ; Location: MULL,ARGYLL AND BUTE ; Lat/long: 56.410,-6.210 ; Depth: 7 km ; Magnitude:  1.4",
                        null, "MULL,ARGYLL AND BUTE", 7, 3.4,
                        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20210401222427.html",
                        null, "EQUK", 56.607, -6.210));
        values.add(
                new Earthquake(
                        "UK Earthquake alert : M  1.4 :MULL,ARGYLL AND BUTE, Thu, 01 Apr 2021 22:25:34",
                        "Origin date/time: Thu, 01 Apr 2021 22:25:34 ; Location: MULL,ARGYLL AND BUTE ; Lat/long: 56.410,-6.210 ; Depth: 7 km ; Magnitude:  1.4",
                        null, "MULL,ARGYLL AND BUTE", 7, 2.8,
                        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20210401222427.html",
                        null, "EQUK", 56.607, -6.210));
        values.add(
                new Earthquake(
                        "UK Earthquake alert : M  1.4 :MULL,ARGYLL AND BUTE, Thu, 01 Apr 2021 22:25:34",
                        "Origin date/time: Thu, 01 Apr 2021 22:25:34 ; Location: MULL,ARGYLL AND BUTE ; Lat/long: 56.410,-6.210 ; Depth: 7 km ; Magnitude:  1.4",
                        null, "MULL,ARGYLL AND BUTE", 7, 1.2,
                        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20210401222427.html",
                        null, "EQUK", 56.607, -6.210));
        values.add(
                new Earthquake(
                        "UK Earthquake alert : M  1.4 :MULL,ARGYLL AND BUTE, Thu, 01 Apr 2021 22:25:34",
                        "Origin date/time: Thu, 01 Apr 2021 22:25:34 ; Location: MULL,ARGYLL AND BUTE ; Lat/long: 56.410,-6.210 ; Depth: 7 km ; Magnitude:  1.4",
                        null, "MULL,ARGYLL AND BUTE", 7, 0.6,
                        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20210401222427.html",
                        null, "EQUK", 56.607, -6.210));

        EarthquakeListViewAdapter adapter = new EarthquakeListViewAdapter(this,
                R.layout.list_item,values);

        listView.setAdapter(adapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

            //
            // Now that you have the xml data you can parse it
            //


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            // adapter.notifyDataSetChanged

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
//                    rawDataDisplay.setText(result);
                }
            });
        }

    }

}
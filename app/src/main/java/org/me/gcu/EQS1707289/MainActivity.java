package org.me.gcu.EQS1707289;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
        // More Code goes here

        values = new ArrayList<>();
        adapter = new EarthquakeListViewAdapter(this, R.layout.list_item,values);
        listView.setAdapter(adapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                updateData();
            }
        };
        timer.schedule(timerTask, 0, 60000);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void onClick(View aview) {
//        Log.e("MyTag","in onClick");
//        startProgress();
//        Log.e("MyTag","after startProgress");
    }

    public void updateData() {

        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";

        Log.e("MyTag","in run");

        try {
            Log.e("MyTag","in try");
            aurl = new URL(urlSource);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            Log.e("MyTag","after ready");

            while ((inputLine = in.readLine()) != null)
            {
                result = result + inputLine;
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
            XMLParser xmlParser = new XMLParser();
            values.clear();
            values.addAll(xmlParser.readFeed(parser));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();

                LatLng uk = new LatLng(55, -2);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uk, 4.0f));
                mMap.clear();

                for (Earthquake e : values) {
                    index = R.drawable.marker_circle_white;
                    LatLng eLatLng = new LatLng(e.getLocationLat(), e.getLocationLong());
                    mMap.addMarker(new MarkerOptions().position(eLatLng)
                            .title(e.getLocation())
                            .icon(BitmapDescriptorFactory.fromResource(colourManager.GetMarkerResourceIndex()))
                    );
                }

                Toast.makeText(getApplicationContext(),
                        "Data Updated",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}

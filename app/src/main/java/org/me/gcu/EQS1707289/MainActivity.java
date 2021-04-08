package org.me.gcu.EQS1707289;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import org.me.gcu.EQS1707289.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ListView listView;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private String selectedDate=null;
    private Spinner navSpinner;
    private GoogleMap mMap;
    private EarthquakeListViewAdapter adapter;
    private ArrayAdapter filterAdapter;
    private List<Earthquake> values;
    private List<Earthquake> dateValues;
    private List<Earthquake> displayValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Date
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        // Spinner
        navSpinner = (Spinner) findViewById(R.id.navSpinner);
        filterAdapter = ArrayAdapter.createFromResource(
                this, R.array.filter_array, R.layout.ghost_text);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        navSpinner.setAdapter(filterAdapter);
        navSpinner.setOnItemSelectedListener(this);

        // ListView
        listView = (ListView) findViewById(R.id.listView);
        values = new ArrayList<>();
        dateValues = new ArrayList<>();
        displayValues = new ArrayList<>();
        adapter = new EarthquakeListViewAdapter(this, R.layout.list_item,displayValues);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Earthquake earthquake = (Earthquake) listView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, InfoExpandActivity.class);
                intent.putExtra("earthquake", earthquake);
                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Data Update
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == navSpinner) {
            updateList();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                selectedDate = date;
                updateList();
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void updateList() {

        dateValues.addAll(values);

        displayValues.clear();
        Log.e("selectedDate", "Current date in update list "+selectedDate);

        String text = (String)navSpinner.getSelectedItem();
        if (text.equals("Show All")) {
            displayValues.addAll(dateValues);
        }
        else if (text.equals("Largest Magnitude")) {
            Earthquake lMagnitude = null;
            for (Earthquake e : dateValues) {
                if (lMagnitude == null || lMagnitude.getMagnitude() < e.getMagnitude()) {
                    lMagnitude = e;
                }
            }
            displayValues.add(lMagnitude);
        }
        else if (text.equals("Deepest Earthquake")) {
            Earthquake deepestEarthquake = null;
            for (Earthquake e : dateValues) {
                if (deepestEarthquake == null || deepestEarthquake.getDepth() < e.getDepth()) {
                    deepestEarthquake = e;
                }
            }
            displayValues.add(deepestEarthquake);
        }
        else if (text.equals("Most Northerly")) {
            Earthquake mostNortherly = null;
            for (Earthquake e : dateValues) {
                if (mostNortherly == null || mostNortherly.getLocationLat() < e.getLocationLat()) {
                    mostNortherly = e;
                }
            }
            displayValues.add(mostNortherly);
        }
        else if (text.equals("Most Easterly")) {
            Earthquake mostEasterly = null;
            for (Earthquake e : dateValues) {
                if (mostEasterly == null || mostEasterly.getLocationLong() < e.getLocationLong()) {
                    mostEasterly = e;
                }
            }
            displayValues.add(mostEasterly);
        }
        else if (text.equals("Most Southerly")) {
            Earthquake mostSoutherly = null;
            for (Earthquake e : dateValues) {
                if (mostSoutherly == null || mostSoutherly.getLocationLat() > e.getLocationLat()) {
                    mostSoutherly = e;
                }
            }
            displayValues.add(mostSoutherly);
        }
        else if (text.equals("Most Westerly")) {
            Earthquake mostWesterly = null;
            for (Earthquake e : dateValues) {
                if (mostWesterly == null || mostWesterly.getLocationLong() > e.getLocationLong()) {
                    mostWesterly = e;
                }
            }
            displayValues.add(mostWesterly);
        }

        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();

                if (mMap != null) {
                    LatLng uk = new LatLng(55, -2);
                    mMap.clear();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uk, 4.0f));

                    for (Earthquake e : displayValues) {
                        LatLng eLatLng = new LatLng(e.getLocationLat(), e.getLocationLong());
                        mMap.addMarker(new MarkerOptions().position(eLatLng)
                                .title(e.getLocation())
                                .icon(BitmapDescriptorFactory.fromResource(ColourCoder.getMarkerColour(e.getMagnitude())))
                        );
                    }
                }

                Toast.makeText(getApplicationContext(),
                        "Data Updated",
                        Toast.LENGTH_SHORT).show();
            }
        });

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

        updateList();

    }

}

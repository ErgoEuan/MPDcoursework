package org.me.gcu.EQS1707289;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

public class InfoExpandActivity extends AppCompatActivity {

    private TextView locationTxt;
    private TextView magnitudeTxt;
    private TextView depthTxt;
    private TextView originDateTxt;
    private TextView categoryTxt;
    private TextView latitudeTxt;
    private TextView longitudeTxt;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_expand);

        locationTxt = findViewById(R.id.locationTxt);
        magnitudeTxt = findViewById(R.id.magnitudeTxt);
        depthTxt = findViewById(R.id.depthTxt);
        originDateTxt = findViewById(R.id.originDateTxt);
        categoryTxt = findViewById(R.id.categoryTxt);
        latitudeTxt = findViewById(R.id.latitudeTxt);
        longitudeTxt = findViewById(R.id.longitudeTxt);
        exitButton = findViewById(R.id.exitButton);

        Earthquake earthquake = (Earthquake)getIntent().getSerializableExtra("earthquake");

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locationTxt.setText(earthquake.getLocation());
        magnitudeTxt.setText(String.valueOf(earthquake.getMagnitude()));
//        magnitudeTextView.setTextColor(Color.parseColor(magColour));
        depthTxt.setText(String.valueOf(earthquake.getDepth()));
        originDateTxt.setText(earthquake.getOriginDate());
        categoryTxt.setText(earthquake.getCategory());
        latitudeTxt.setText(String.valueOf(earthquake.getLocationLat()));
        longitudeTxt.setText(String.valueOf(earthquake.getLocationLong()));

    }

}
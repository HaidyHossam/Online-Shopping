package com.example.onlineshopping1;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText currentLocation;
    FloatingActionButton getLocation;
    LocationManager manager;
    MyLocationListener listener;
    String chosenAddress = "";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocation = (FloatingActionButton) findViewById(R.id.getLocation);
        currentLocation = (EditText) findViewById(R.id.currentLocation);
        FloatingActionButton proceed = (FloatingActionButton) findViewById(R.id.next);
        id = getIntent().getExtras().getInt("id");
        listener = new MyLocationListener(this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,0,listener);
        }
        catch (SecurityException ex){
            Toast.makeText(getApplicationContext(),"Access location denied!!!!",Toast.LENGTH_SHORT).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, OrderActivity.class);
                i.putExtra("address", chosenAddress);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.04441960,31.235711600),8));

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressList;

                Location location = null;

                try {
                    location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException ex) {
                    Toast.makeText(getApplicationContext(), "Access location denied", Toast.LENGTH_SHORT).show();
                }
                if (location != null) {
                    LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
                    try {
                        addressList = coder.getFromLocation(myPos.latitude, myPos.longitude, 1);
                        if (!addressList.isEmpty()) {
                            String myAddress = "";
                            for (int i = 0; i <= addressList.get(0).getMaxAddressLineIndex()-1; i++) {
                                myAddress+=addressList.get(0).getAddressLine(i) + ", ";
                            }
                            myAddress+=addressList.get(0).getAddressLine(addressList.get(0).getMaxAddressLineIndex());
                            chosenAddress = myAddress;
                            mMap.addMarker(new MarkerOptions().position(myPos).title("My Location").snippet(myAddress)).setDraggable(true);
                            currentLocation.setText(myAddress);
                        }
                    }
                    catch (IOException e) {
                        mMap.addMarker(new MarkerOptions().position(myPos).title("My Location"));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos,15));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wait for location to be determined", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressList;

                try {
                    addressList = coder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    if (!addressList.isEmpty()) {
                        String myAddress = "";
                        for (int i = 0; i <= addressList.get(0).getMaxAddressLineIndex(); i++) {
                            myAddress+=addressList.get(0).getAddressLine(i) + ", ";
                        }
                        chosenAddress = myAddress;
                        currentLocation.setText(myAddress);
                        marker.setSnippet(myAddress);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No address", Toast.LENGTH_SHORT).show();
                        currentLocation.getText().clear();
                    }
                }
                catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Can't get location!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
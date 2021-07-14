package com.example.onlineshopping1;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyLocationListener implements LocationListener {
    private Context context;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(context,"GPS Enabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(context,"GPS Disabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //Toast.makeText(context,location.getLatitude()+", "+location.getLongitude(),Toast.LENGTH_SHORT).show();
    }
}

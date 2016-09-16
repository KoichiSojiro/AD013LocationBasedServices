package com.example.trannh08.ad013locationbasedservices;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/*
1. Add Google Play Service Dependency:
    Go to File\Project Structure (Ctrl + Alt + Shift + S)
    Modules\app\Dependencies\[Add]\Library Dependency
        search for: com.google.android.gms:play-services

2. Remember to add permission to Manifest.xml and check permission in code
3. Code run best in real device because virtual one will get trouble with GPS
 */

public class MainActivity extends AppCompatActivity {

    private final String DEBUG_TAG = "DEBUG TAG";
    private TextView textView_address;
    private TextView textView_location;
    private Button button_getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isLocationServiceOn = checkLocationServices();
        if (!isLocationServiceOn) askForLocationServices();

        textView_location = (TextView) findViewById(R.id.textView_location);
        textView_address = (TextView) findViewById(R.id.textView_address);

        button_getLocation = (Button) findViewById(R.id.button_getLocation);
        button_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCurrentLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "ACCESS_FINE_LOCATION is OK", Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "ACCESS_COARSE_LOCATION is OK", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocationServices() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForLocationServices() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this, "ACCESS_COARSE_LOCATION permission is needed", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "ACCESS_FINE_LOCATION permission is needed", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
    }

    public void displayCurrentLocation() {
        try {
            boolean isLocationServiceOn = checkLocationServices();
            if (isLocationServiceOn) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    textView_location.setText("Raw location data: " + location.toString());

                    String addressName = "";
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0)
                            addressName = addresses.get(0).getFeatureName();
                        addressName += ", " + addresses.get(0).getSubLocality();
                        addressName += ", " + addresses.get(0).getSubAdminArea();
                        addressName += ", " + addresses.get(0).getAdminArea();
                        addressName += ", " + addresses.get(0).getCountryName();

                        textView_address.setText("Address: " + addressName);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                textView_address.setText("Location services were turned off.");
            }
        } catch (Exception ex) {
            textView_address.setText("Cannot detect current location.");
            Log.d(DEBUG_TAG, ex.getMessage());
            Log.d(DEBUG_TAG, ex.getStackTrace().toString());
        }
    }
}

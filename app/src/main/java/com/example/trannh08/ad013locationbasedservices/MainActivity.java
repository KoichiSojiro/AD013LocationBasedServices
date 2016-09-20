package com.example.trannh08.ad013locationbasedservices;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
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

import java.io.IOException;
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
    private TextView textView_addressGps;
    private TextView textView_locationGps;
    private TextView textView_addressNetwork;
    private TextView textView_locationNetwork;
    private TextView textView_addressBestAccuracy;
    private TextView textView_locationBestAccuracy;
    private Button button_getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isLocationServiceOn = checkLocationServices();
        if (!isLocationServiceOn) askForLocationServices();

        textView_locationGps = (TextView) findViewById(R.id.textView_locationGps);
        textView_addressGps = (TextView) findViewById(R.id.textView_addressGps);
        textView_locationNetwork = (TextView) findViewById(R.id.textView_locationNetwork);
        textView_addressNetwork = (TextView) findViewById(R.id.textView_addressNetwork);
        textView_locationBestAccuracy = (TextView) findViewById(R.id.textView_locationBestAccuracy);
        textView_addressBestAccuracy = (TextView) findViewById(R.id.textView_addressBestAccuracy);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ACCESS_FINE_LOCATION permission granted!", Toast.LENGTH_SHORT).show();
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ACCESS_COARSE_LOCATION permission granted!", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ACCESS_FINE_LOCATION permission granted!", Toast.LENGTH_SHORT).show();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ACCESS_COARSE_LOCATION permission granted!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkLocationServices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void askForLocationServices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "ACCESS_COARSE_LOCATION permission is needed.", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                }
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION permission is needed.", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                }
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "ACCESS_COARSE_LOCATION permission is needed.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                }
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION permission is needed.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                }
            }
        }
    }

    public void displayCurrentLocation() {
        Location locationGps = getCurrentLocation(LocationManager.GPS_PROVIDER);
        if (locationGps != null) {
            textView_locationGps.setText("Using GPS: " + locationGps.toString());
            textView_addressGps.setText(getCurrentAddress(locationGps));
        } else {
            textView_locationGps.setText("Cannot detect location using GPS service.");
            textView_locationGps.setText(getCurrentAddress(locationGps));
        }

        Location locationNetwork = getCurrentLocation(LocationManager.NETWORK_PROVIDER);
        if (locationNetwork != null) {
            textView_locationNetwork.setText("Using Network: " + locationNetwork.toString());
            textView_addressNetwork.setText(getCurrentAddress(locationNetwork));
        } else {
            textView_locationNetwork.setText("Cannot detect location using Network service.");
            textView_addressNetwork.setText(getCurrentAddress(locationNetwork));
        }

        Location locationBetterAccuracy = getBetterLocation(locationGps, locationNetwork);
        if (locationBetterAccuracy != null) {
            textView_locationBestAccuracy.setText("Best Accuracy using " + locationBetterAccuracy.getProvider().toUpperCase() + ": " + locationBetterAccuracy.toString());
            textView_addressBestAccuracy.setText(getCurrentAddress(locationBetterAccuracy));
        } else {
            textView_locationBestAccuracy.setText("Cannot detect best location accuracy.");
            textView_addressBestAccuracy.setText(getCurrentAddress(locationBetterAccuracy));
        }
    }

    public Location getCurrentLocation(String provider) {
        Location location = null;
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(provider);
            }
            return location;
        } catch (Exception ex) {
            Log.d(DEBUG_TAG, ex.getMessage());
            Log.d(DEBUG_TAG, ex.getStackTrace().toString());
            return location;
        }
    }

    public Location getBetterLocation(Location location01, Location location02) {
        int TWO_MINUTES = 1000 * 60 * 2;

        if(location01 == null) {
            return location02;
        } else {
            long timeDelta = location01.getTime() - location02.getTime();
            boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
            boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
            boolean isNewer = timeDelta > 0;

            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location01.getAccuracy() - location02.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;

            // Check if the old and new location are from the same provider
            boolean isFromSameProvider;
            if(location01.getProvider() == null && location02.getProvider() == null
                    || location01.getProvider().equals(location02.getProvider())) {
                isFromSameProvider = true;
            } else {
                isFromSameProvider = false;
            }

            if(isSignificantlyNewer) {
                return location01;
            } else if(isSignificantlyOlder) {
                return location02;
            } else {
                // Determine location quality using a combination of timeliness and accuracy
                if (isMoreAccurate
                        || isNewer && !isLessAccurate
                        || isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                    return location01;
                } else {
                    return location02;
                }
            }
        }
    }

    public String getCurrentAddress(Location location) {
        if (location != null) {
            String address = "";
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0)
                    address = addresses.get(0).getFeatureName();
                address += ", " + addresses.get(0).getSubLocality();
                address += ", " + addresses.get(0).getSubAdminArea();
                address += ", " + addresses.get(0).getAdminArea();
                address += ", " + addresses.get(0).getCountryName();

                return "Address: " + address;
            } catch (IOException ex) {
                return "Cannot parse location data right now.";
            } catch (Exception ex) {
                return "Service error.";
            }
        } else {
            return "Cannot get the address.";
        }
    }
}

package com.example.trannh08.ad013locationbasedservices;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

// File\Project Structure (Ctrl + Alt + Shift + S)
// Modules\app\Dependencies\[Add]\Library Dependency
// search for: com.google.android.gms:play-services

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private TextView textView_address;
    private TextView textView_locaion;
    private Button button_getLocation;
    private Button button_connect;
    private Button button_disconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_locaion = (TextView) findViewById(R.id.textView_location);
        textView_address = (TextView) findViewById(R.id.textView_address);

        button_getLocation = (Button) findViewById(R.id.button_getLocation);
        button_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCurrentLocation();
            }
        });

        button_connect = (Button) findViewById(R.id.button_connect);
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleApiClient.connect();
                textView_locaion.setText("Got connected....");
            }
        });

        button_disconnect = (Button) findViewById(R.id.button_disconnect);
        button_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleApiClient.disconnect();
                textView_locaion.setText("Got disconnected....");
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onDisconnected() {
//        // Display the connection status
//        Toast.makeText(this, "Disconnected. Please re-connect.",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
        textView_locaion.setText("Got disconnected! Try to connect again.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failure : " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    public void displayCurrentLocation() {
        String provider;
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        // => why this fucking shit return null?

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Location location = locationManager.getLastKnownLocation(provider);
            textView_address.setText(location.toString());
            //return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        textView_address.setText(location.toString());
    }

//    public void displayCurrentLocation() {
//        String cityName = "";
//        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(1.1111, 1.222222, 1);
//            if (addresses.size() > 0)
//                System.out.println(addresses.get(0).getLocality());
//            cityName = addresses.get(0).getLocality();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String s = longitude + "\n" + latitude + "\n\nMy Currrent City is: " + cityName;
//        textView_address.setText(s);
//    }

//    public void displayCurrentLocation() {
//        // Get the current location's latitude & longitude
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//
//        String msg = "Current Location: " +
//                Double.toString(currentLocation.getLatitude()) + "," +
//                Double.toString(currentLocation.getLongitude());
//
//        // Display the current location in the UI
//        textView_locaion.setText(msg);
//
//        // To display the current address in the UI
//        (new GetAddressTask(this)).execute(currentLocation);
//    }
//
//
//    private class GetAddressTask extends AsyncTask<Location, Void, String> {
//        Context mContext;
//
//        public GetAddressTask(Context context) {
//            super();
//            mContext = context;
//        }
//        /*
//        * When the task finishes, onPostExecute() displays the address.
//        */
//        @Override
//        protected void onPostExecute(String address) {
//            // Display the current address in the UI
//            textView_address.setText(address);
//        }
//
//        @Override
//        protected String doInBackground(Location... params) {
//            Geocoder geocoder =new Geocoder(mContext, Locale.getDefault());
//
//            // Get the current location from the input parameter list
//            Location loc = params[0];
//
//            // Create a list to contain the result address
//            <Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
//            }
//
//            catch (IOException e1) {
//                Log.e("LocationSampleActivity",e1.getMessage());
//                e1.printStackTrace();
//                return ("IO Exception trying to get address");
//            }
//
//            catch (IllegalArgumentException e2) {
//                // Error message to post in the log
//                String errorString = "Illegal arguments " +
//                        Double.toString(loc.getLatitude()) +" , " +Double.toString(loc.getLongitude()) +" passed to address service";
//                Log.e("LocationSampleActivity", errorString);
//                e2.printStackTrace();
//                return errorString;
//            }
//            // If the reverse geocode returned an address
//            if (addresses != null && addresses.size() > 0) {
//                // Get the first address
//                Address address = addresses.get(0);
//
//            /*
//            * Format the first line of address (if available),
//            * city, and country name.
//            */
//                String addressText = String.format("%s, %s, %s");
//
//                // If there's a street address, add it
//                address.getMaxAddressLineIndex() > 0 ?
//                        address.getAddressLine(0) : "",
//
//                        // Locality is usually a city
//                        address.getLocality(),
//
//                        // The country of the address
//                        address.getCountryName());
//
//                // Return the text
//                return addressText;
//            }
//            else {
//                return "No address found";
//            }
//        }
//    }// AsyncTask class
}

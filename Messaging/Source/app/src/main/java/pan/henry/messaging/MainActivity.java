package pan.henry.messaging;

import pan.henry.messaging.SecureRandomString;

import java.util.Random;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
Can't see the rest of the messages? Try scrolling!
You can do this by touching the screen and moving your finger.

Funny how I need to include this comment...
Scrolling is a difficult concept to some, apparently.
 */

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private LocationData locationData = LocationData.getLocationData();

    //Some excellent random names in case people are too lazy to put in their own.
    private final String[] random_names = {"Jonathan Joestar", "Joseph Joestar", "Jotaro Kujo", "Josuke Higashikata", "Giorno Giovanna", "Jolyne Kujo", "Johnny Joestar"};
    EditText user_input;
    TextView status_text;
    Button button;
    private String user_id;
    private String nickname;

    //Controls the state of the app.
    //0 - setting nickname, 1 - check location, 2 - enabled chat
    int app_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_state = 0;
        status_text = (TextView) findViewById(R.id.statusText);
        button = (Button) findViewById(R.id.button);
        user_input = (EditText) findViewById(R.id.editText);

        //Hand out a random nickname.
        //User can change this if they'd like.
        int rindex = new Random().nextInt(random_names.length);
        user_input.setText(random_names[rindex]);

        // Gets the settings, and creates a random user id if missing.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);
        if (user_id == null) {
            // Creates a random one, and sets it.
            SecureRandomString srs = new SecureRandomString();
            user_id = srs.nextString();
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_id", user_id);
            e.commit();
        }
    }

    /*
    The button has multiple behaviors depending on the app_state.
     */
    public void onClick(View v) {
        hideKeyboard();
        switch(app_state){
            case 0:
                setNickname();
                break;
            case 1:
                requestLocationUpdate();
                break;
            default:
                Intent chat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(chat);
                break;
        }
    }

    /*
    Take the nickname from user_input.
    */
    public void setNickname() {
        nickname = user_input.getText().toString();
        if (nickname.isEmpty()) {
            status_text.setText("Please enter a username at least one character long.");
            status_text.setVisibility(View.VISIBLE);
        } else {
            TextView upper = (TextView) findViewById(R.id.textView);
            upper.setText("Welcome,");
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_nickname", nickname);
            e.commit();
            user_input.setKeyListener(null);
            user_input.setClickable(false);
            app_state = 1;
            button.setText("Update");
            status_text.setVisibility(View.VISIBLE);
            requestLocationUpdate();
        }
    }

    /*
    Hide the keyboard.
     */
    public void hideKeyboard() {
        if(getCurrentFocus()!= null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /*
    Shobhit's FindRestaurant trimmed code samples below.
    You can ignore this; it is not necessary to view.
     */
    @Override
    public void onResume() {
        //check if user already gave permission to use location
        if (checkLocationAllowed())
            requestLocationUpdate();
        super.onResume();
    }

    /*
    Check users location sharing setting
    */
    private boolean checkLocationAllowed() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("location_allowed", false);
    }

    /*
    Persist users location sharing setting
     */
    private void setLocationAllowed(boolean allowed) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("location_allowed", allowed);
        editor.commit();
    }


    private void requestLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        status_text.setText("Checking Location, please wait...\nPress Update if it is taking too long.");
        if (locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }
            }
        } else {
            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

                    } else {
                        throw new RuntimeException("permission not granted still callback fired");
                    }

                }
                return;
            }
        }
    }

    /*
    Remove location update. This must be called in onPause if the user has allowed location sharing
	 */
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }

    @Override
    public void onPause() {
        if (checkLocationAllowed())
            removeLocationUpdate(); //if the user has allowed location sharing we must disable location updates now
        super.onPause();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Location lastLocation = locationData.getLocation();
            double newAccuracy = location.getAccuracy();
            long newTime = location.getTime();

            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                locationData.setLocation(location);

                if(newAccuracy < 100) {
                    button.setText("Start Chat");
                    app_state = 2;
                    status_text.setText("All set. You may enter the chat.");
                } else {
                    button.setText("Update");
                    app_state = 1;
                    status_text.setText("Checking Location, please wait...\nPress Update if it is taking too long.");
                }

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };
}

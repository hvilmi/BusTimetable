package esim.busstopwidget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

//API KEY: AIzaSyBN2rnahDKnRnQpFQOGUC_S123FeRy6shE

public class MainActivity extends AppCompatActivity {

    String base = "https://maps.googleapis.com/maps/api/directions/json?";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BusLineFinder busLineFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BusLineFinder testObject = new BusLineFinder();
        testObject.getBusLine("disneyland", "disneyland");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String place = "disneyland";
        base = base + "origin=" + place;

        busLineFinder = new BusLineFinder();

    }

    // gps
    public LatLng getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        // Tarkistetaan lupa
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},3);
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 3:

            case 2:
        }
    }

    public void displayBusLines(BusLineInfoContainer busLines) {
        for(int i=0;i<busLines.getSize();i++) {
            //Näyttää ennalta määrätyn määrän bussilinjoja
            BusLineInfo busLineInfo = busLines.getBusLine(i);

        }
    }

    public void getBusLinesFromCurLocation(String endLocation) {
        String latLngString = getLocation().toString();
        busLineFinder.getBusLine(latLngString, endLocation);
    }


}

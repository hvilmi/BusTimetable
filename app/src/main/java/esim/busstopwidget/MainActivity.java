package esim.busstopwidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;

//API KEY: AIzaSyBN2rnahDKnRnQpFQOGUC_S123FeRy6shE

public class MainActivity extends AppCompatActivity {
    GoogleApiClient mGoogleApiClient;
    String base="https://maps.googleapis.com/maps/api/directions/json?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String place = "disneyland";
        base=base+"origin="+place;


    }
}

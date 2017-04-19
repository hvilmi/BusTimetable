package esim.busstopwidget;

import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import layout.BusStopWidget;

/**
 * Created by hvilmi on 3/29/17.
 */

public class BusLineService extends IntentService {

    public static final String DESTINATION = "destination";
    public static final String NOTIFICATION = "busLineServiceNotification";
    public static final String BUSLINES = "busLines";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    String destinationString;

    public BusLineService() {
        super("BusLineService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        destinationString = intent.getStringExtra(DESTINATION);
        destinationString = destinationString.replace(",", "").replace(" ", "+");
        LatLng location = getLocation();
        String originString = Double.toString(location.latitude) +","+ Double.toString(location.longitude);
        getBusLine(originString, destinationString);

    }

    public LatLng getLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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

            //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},3);
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        try {
            Log.d("location",location.toString());
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

    public void getBusLine(String startLocation, String endLocation) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?";
        url = url + "origin=" + startLocation + "&destination=" + endLocation +"&mode=transit&key=AIzaSyBN2rnahDKnRnQpFQOGUC_S123FeRy6shE";

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("test", response.toString());
                        JSONArray routes;
                        try {
                            BusLineInfoContainer busLineInfoContainer = new BusLineInfoContainer();
                            routes = response.getJSONArray("routes");
                            for(int i=0;i < routes.length();i++) {
                                JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");
                                for(int j=0;j < legs.length();j++) {
                                    JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");
                                    BusLineInfo busLineInfo = null;
                                    for(int k=0;k < steps.length();k++) {
                                        JSONObject step = steps.getJSONObject(k);
                                        if(Objects.equals(step.getString("travel_mode"), "TRANSIT")) {
                                            JSONObject busLineDetails = step.getJSONObject("transit_details");
                                            if (busLineInfo == null) {
                                                busLineInfo = new BusLineInfo(busLineDetails.getJSONObject("line").getString("short_name"),
                                                        busLineDetails.getJSONObject("line").getString("name"),
                                                        busLineDetails.getJSONObject("departure_time").getString("text"),
                                                        busLineDetails.getJSONObject("arrival_time").getString("text"),
                                                        busLineDetails.getJSONObject("departure_stop").getString("name"),
                                                        busLineDetails.getJSONObject("arrival_stop").getString("name"));
                                            }
                                            else {
                                                busLineInfo.addSwitch(busLineDetails.getJSONObject("line").getString("short_name"));
                                                busLineInfo.setArrivalTime(busLineDetails.getJSONObject("arrival_time").getString("text"));
                                            }
                                            busLineInfoContainer.addBusLine(busLineInfo);
                                        }
                                        else if(Objects.equals(step.getString("travel_mode"), "WALKING")) {
                                            JSONObject walkDistance = step.getJSONObject("distance");
                                            //walkDistance sisältää kentät text (etäisyys String-muodossa, esim 0.4 km) ja value (kokonaislukuna metreissä, esim 389)

                                        }
                                    }

                                }
                            }

                            publishResult(busLineInfoContainer);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error with Volley request");
                    }
                }
        );

        mQueue.add(jsonObjectRequest);

    }

    private void publishResult(BusLineInfoContainer busLines) {
        Intent i = new Intent(this, BusStopWidget.class);
        i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {R.xml.bus_stop_widget_info};
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        i.putExtra(BUSLINES,busLines);
        sendBroadcast(i);
    }
    /*private void publishResult(BusLineInfoContainer busLines) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(BUSLINES, busLines);
        sendBroadcast(intent);


    }*/
}

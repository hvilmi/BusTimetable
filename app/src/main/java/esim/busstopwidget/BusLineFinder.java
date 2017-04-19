package esim.busstopwidget;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Hannes on 9.3.2017.
 */

public class BusLineFinder {
    private Context mContext;
    MainActivity mainActivity;

    public BusLineFinder(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mContext = mainActivity.getApplicationContext();
    }


    public void getBusLine(String startLocation, String endLocation) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?";
        url = url + "origin=" + startLocation + "&destination=" + endLocation +"&mode=transit&key=AIzaSyBN2rnahDKnRnQpFQOGUC_S123FeRy6shE";

        RequestQueue mQueue = Volley.newRequestQueue(mContext);

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

                                    mainActivity.displayBusLines(busLineInfoContainer);
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


}

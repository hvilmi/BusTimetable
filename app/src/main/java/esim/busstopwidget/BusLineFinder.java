package esim.busstopwidget;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hannes on 9.3.2017.
 */

public class BusLineFinder {
    private Context mContext;

    public BusLineFinder() {

    }

    public void getBusLine(String startLocation, String endLocation) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?";
        url = url + "origin=" + startLocation + "&destination=" + endLocation;

        RequestQueue mQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray routes = response.getJSONArray("routes");

                        for(int i=0;i < routes.length();i++) {
                            JSONArray legs = routes.getJSONObject(i).getJSONArray();
                            ArrayList<String> busLines = new ArrayList<String>();
                            for(int j=0;j > legs.length();j++) {
                                JSONObject leg = legs.getJSONObject(j);
                                if(leg.getString("travel_mode") == "TRANSIT") {
                                    
                                }
                            }
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

        mQueue.add(stringRequest);

    }


}

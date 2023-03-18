package com.fokakefir.linkhub.logic;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlacesApi implements RequestTask.OnResponseListener {

    private static final String TAG = "PlacesApi";
    private static final int REQUEST_CITY = 1;
    private static final int REQUEST_PLACES = 2;

    public static final int RADIUS = 1200;

    public PlacesApi() {

    }

    public void getPlacesData(String cityName) {
        String url = "https://opentripmap-places-v1.p.rapidapi.com/en/places/geoname?name=" + cityName;
        new RequestTask(REQUEST_CITY, this).execute(url);
    }

    private void getPlacesList(int radius, int lon, int lat) {
        String url = "https://opentripmap-places-v1.p.rapidapi.com/en/places/radius?radius=" + radius + "&lon=" + lon + "&lat=" + lat;
        new RequestTask(REQUEST_PLACES, this).execute(url);
    }

    @Override
    public void onResponse(String response, int requestCode) {
        if (requestCode == REQUEST_CITY) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String name = jsonResponse.getString("name");
                int lat = jsonResponse.getInt("lat");
                int lon = jsonResponse.getInt("lon");
                getPlacesList(RADIUS, lon, lat);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } else if (requestCode == REQUEST_PLACES) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonPlaces =  jsonResponse.getJSONArray("features");
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    @Override
    public void onFailed(String errorMessage) {
        Log.d(TAG, errorMessage);
    }
}

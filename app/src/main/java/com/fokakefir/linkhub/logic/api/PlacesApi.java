package com.fokakefir.linkhub.logic.api;

import android.util.Log;

import com.fokakefir.linkhub.model.FilterOps;
import com.fokakefir.linkhub.model.Place;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesApi implements CityRequestTask.OnResponseListener, PlacesRequestTask.OnResponseListener, PlaceRequestTask.OnResponseListener {

    private static final String TAG = "PlacesApi";
    private static final int REQUEST_CITY = 1;
    private static final int REQUEST_PLACES = 2;
    private static final int REQUEST_PLACE = 3;

    private static final int RADIUS = 10000;
    private static final int LIMIT = 50;
    private static final int RATE = 3;


    private int size;

    private OnResponseListener listener;

    public PlacesApi(OnResponseListener listener) {
        this.listener = listener;
    }

    public void sendCityDataRequest(String cityName) {
        new CityRequestTask( this).execute(cityName);
    }

    private void sendPlacesListRequest(int radius, double lon, double lat) {
        new PlacesRequestTask(this).execute(
                String.valueOf(radius),
                String.valueOf(lon),
                String.valueOf(lat),
                String.valueOf(LIMIT),
                String.valueOf(RATE)
        );
    }

    private void sendPlaceRequest(String xid) {
        new PlaceRequestTask(this).execute(xid);
    }

    @Override
    public void onResponseCity(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            double lat = jsonResponse.getDouble("lat");
            double lon = jsonResponse.getDouble("lon");
            sendPlacesListRequest(RADIUS, lon, lat);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onResponsePlaces(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonPlaces =  jsonResponse.getJSONArray("features");
            this.size = jsonPlaces.length();

            for (int i = 0; i < jsonPlaces.length(); ++i) {
                JSONObject jsonFeature = jsonPlaces.getJSONObject(i);
                String xid = jsonFeature.getJSONObject("properties").getString("xid");
                sendPlaceRequest(xid);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }


    @Override
    public void onResponsePlace(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            String id = jsonResponse.getString("xid");

            String imageUrl = null;

            if(!jsonResponse.isNull("preview")){
                if(!jsonResponse.getJSONObject("preview").isNull("source")){
                    imageUrl = jsonResponse.getJSONObject("preview").getString("source");
                }

                else if (!jsonResponse.isNull("image")) {

                    imageUrl = jsonResponse.getString("image");
                }

            }


            String desc = "";
            if (!jsonResponse.isNull("wikipedia_extracts") && !jsonResponse.getJSONObject("wikipedia_extracts").isNull("text"))
                desc = jsonResponse.getJSONObject("wikipedia_extracts").getString("text");

            String name = "";
            if (!jsonResponse.isNull("name"))
                name = jsonResponse.getString("name");
            else
                return;

            if (name.isEmpty())
                return;

            Place place = new Place(id, name, desc, imageUrl);
            this.listener.onPlaceAdded(place);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onFailed(String errorMessage) {
        Log.d(TAG, errorMessage);
    }

    public interface OnResponseListener {
        void onPlaceAdded(Place place);
    }
}

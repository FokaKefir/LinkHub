package com.fokakefir.linkhub.logic.api;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlacesRequestTask extends AsyncTask<String, Void, String> {

    private OnResponseListener listener;

    public PlacesRequestTask(OnResponseListener listener) {
        this.listener = listener;
    }

    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        String strRadius = params[0];
        String strLon = params[1];
        String strLat = params[2];
        String strLimit = params[3];
        String strRate = params[4];
        String url = "https://opentripmap-places-v1.p.rapidapi.com/en/places/radius?radius="
                + strRadius + "&lon=" + strLon + "&lat=" + strLat + "&limit=" + strLimit + "&rate=" + strRate;


        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "c4eb35dca4mshb289a8dfa65d4cbp1c0543jsndc610081d86c")
                .addHeader("X-RapidAPI-Host", "opentripmap-places-v1.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            listener.onFailed(e.toString());
            return null;
        }
    }

    protected void onPostExecute(String result) {
        if (result == null)
            return;
        listener.onResponsePlaces(result);
    }

    public interface OnResponseListener {
        void onResponsePlaces(String response);
        void onFailed(String errorMessage);
    }
}

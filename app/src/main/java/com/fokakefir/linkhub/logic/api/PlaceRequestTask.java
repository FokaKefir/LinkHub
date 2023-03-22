package com.fokakefir.linkhub.logic.api;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlaceRequestTask extends AsyncTask<String, Void, String>  {

    private OnResponseListener listener;

    public PlaceRequestTask(OnResponseListener listener) {
        this.listener = listener;
    }

    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        String xid = params[0];
        String url = "https://opentripmap-places-v1.p.rapidapi.com/en/places/xid/" + xid;


        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "a5f56adafcmsh7ce5a1a92a15775p1f17d2jsn6e3a27afad40")
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
        listener.onResponsePlace(result);
    }

    public interface OnResponseListener {
        void onResponsePlace(String response);
        void onFailed(String errorMessage);
    }
}

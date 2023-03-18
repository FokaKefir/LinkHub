package com.fokakefir.linkhub.logic;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestTask extends AsyncTask<String, Void, String> {

    public static final int REQUEST_FAILED = -1;

    private int requestCode;
    private OnResponseListener listener;

    public RequestTask(int requestCode, OnResponseListener listener) {
        this.requestCode = requestCode;
        this.listener = listener;
    }

    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        String url = params[0];

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
        listener.onResponse(result, this.requestCode);
    }

    public interface OnResponseListener {
        void onResponse(String response, int requestCode);
        void onFailed(String errorMessage);
    }

}

package com.fokakefir.linkhub.logic.api;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fokakefir.linkhub.model.FilterOps;

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
        //String strRate = params[4];
        String url = "https://opentripmap-places-v1.p.rapidapi.com/en/places/radius?radius="
                + strRadius + "&lon=" + strLon + "&lat=" + strLat + "&limit=" + strLimit;


        if(FilterOps.getInstance().getKinds()!=null & !FilterOps.getInstance().getKinds().isEmpty()){

            url += "&kinds=";

            for (int i = 0; i < FilterOps.getInstance().getKinds().size(); i++) {

                url += FilterOps.getInstance().getKinds().get(i);

                if(i+1!=FilterOps.getInstance().getKinds().size()){
                    url += ",";
                }
            }
        }
        if(FilterOps.getInstance().getRatings()!=null && !FilterOps.getInstance().getRatings().isEmpty()){

            url += "&rate=";

            for (int i = 0; i < FilterOps.getInstance().getRatings().size(); i++) {
                url += FilterOps.getInstance().getRatings().get(i);

                if(i+1!=FilterOps.getInstance().getRatings().size()){
                    url += ",";
                }
            }
        }

        if(FilterOps.getInstance().getExclude() != null && !FilterOps.getInstance().getExclude().isEmpty()){

            url += "&exclude=";

            for (int i = 0; i < FilterOps.getInstance().getExclude().size(); i++) {

                url += FilterOps.getInstance().getExclude().get(i);

                if(i+1 != FilterOps.getInstance().getExclude().size()){
                    url += ",";
                }

            }
        }

        if(FilterOps.getInstance().isSorted()){
            url += "&sort=" + FilterOps.getInstance().getSortByThisField();
        }
        Log.d("URL", url);

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

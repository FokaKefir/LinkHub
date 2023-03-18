package com.fokakefir.linkhub.gui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.logic.api.PlacesApi;
import com.fokakefir.linkhub.model.Place;


public class HomeFragment extends Fragment implements View.OnClickListener, PlacesApi.OnResponseListener {


    private MainActivity activity;

    private View view;

    private Button btnSearch;

    private PlacesApi placesApi;

    public HomeFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        this.btnSearch = view.findViewById(R.id.btn_search);
        this.btnSearch.setOnClickListener(this);

        this.placesApi = new PlacesApi(this);

        return this.view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
            this.placesApi.sendCityDataRequest("Budapest");
        }
    }

    @Override
    public void onPlaceAdded(Place place) {

    }
}
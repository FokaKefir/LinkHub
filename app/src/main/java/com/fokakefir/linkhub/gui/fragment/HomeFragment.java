package com.fokakefir.linkhub.gui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.recyclerview.PlaceAdapter;
import com.fokakefir.linkhub.logic.api.PlacesApi;
import com.fokakefir.linkhub.logic.database.HomeDatabaseManager;
import com.fokakefir.linkhub.model.Place;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements PlaceAdapter.OnHolderListener, HomeDatabaseManager.OnHomeListener {

    private MainActivity activity;

    private View view;

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;

    private List<Place> places;

    private HomeDatabaseManager databaseManager;

    public HomeFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        this.places = new ArrayList<>();

        this.recyclerView = this.view.findViewById(R.id.recycler_view_home);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.adapter = new PlaceAdapter(this.activity, places, this);
        this.recyclerView.setAdapter(this.adapter);

        this.databaseManager = new HomeDatabaseManager(this);

        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.databaseManager.setSnapshotListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.databaseManager.removeSnapshotListener();
    }

    @Override
    public void onPlaceClick(int pos) {
        Place place = this.places.get(pos);
        this.activity.addToFragments(new ReviewFragment(this.activity, place));
    }


    @Override
    public void onFailed(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaceAdded(Place place) {
        int pos = this.places.size();
        this.places.add(place);
        this.adapter.notifyItemInserted(pos);
    }
}
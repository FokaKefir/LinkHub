package com.fokakefir.linkhub.gui.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.dialog.FilterDialog;
import com.fokakefir.linkhub.gui.recyclerview.PlaceAdapter;
import com.fokakefir.linkhub.logic.api.PlacesApi;
import com.fokakefir.linkhub.model.FilterOps;
import com.fokakefir.linkhub.model.Place;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener, PlacesApi.OnResponseListener, PlaceAdapter.OnHolderListener {


    // region 1. Declaration

    private MainActivity activity;

    private RecyclerView recyclerView;
    private EditText txtSearch;
    private ImageView btnSearch;

    private ImageView filterSearch;
    private PlaceAdapter adapter;

    private View view;

    private List<Place> places;

    private PlacesApi api;

    // endregion

    // region 2. Constructor and Lifecycle

    public SearchFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_search, container, false);
        this.txtSearch = this.view.findViewById(R.id.txt_frag_search);

        this.btnSearch = this.view.findViewById(R.id.btn_frag_search);
        this.filterSearch = this.view.findViewById(R.id.btn_frag_filter);

        this.btnSearch.setOnClickListener(this);
        this.filterSearch.setOnClickListener(this);

        this.places = new ArrayList<>();

        this.recyclerView = this.view.findViewById(R.id.recycler_Post);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.adapter = new PlaceAdapter(this.getContext(), this.places, this);
        this.recyclerView.setAdapter(adapter);


        FilterOps filterOps = FilterOps.getInstance();

        ArrayList<String>testKinds = new ArrayList<>();

        ArrayList<String>testExclude = new ArrayList<>();

        ArrayList<String>testRatings = new ArrayList<>();

        filterOps.setKinds(testKinds);
        filterOps.setRatings(testRatings);
        filterOps.setExclude(testExclude);

        filterOps.setSorted(false);

        //testExclude.add("historic");
        //testExclude.add("cultural");

        //testKinds.add("adult");
        //testKinds.add("-historic");
        //testKinds.add("casino");

        //testRatings.add("3");


        this.api = new PlacesApi(this);

        return this.view;
    }

    // endregion

    // region 3. Button listener

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_frag_search) {
            String city = this.txtSearch.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(activity, "Text field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                this.places.clear();
                this.adapter.notifyDataSetChanged();
                this.api.sendCityDataRequest(city);
            }
        }
        if(view.getId() == R.id.btn_frag_filter){
            openFilterDialog();
        }
    }


    // endregion

    public  void  openFilterDialog(){
        FilterDialog dialog = new FilterDialog();
        dialog.show(activity.getSupportFragmentManager(), "Filter");
    }

    // region 4. Api listener

    @Override
    public void onPlaceAdded(Place place) {
        this.places.add(place);
        int pos = this.places.size();
        this.adapter.notifyItemInserted(pos);
    }

    // endregion

    // region 5. RecyclerView listener

    @Override
    public void onPlaceClick(int pos) {
        Place place = this.places.get(pos);
        this.activity.addToFragments(new ReviewFragment(this.activity, place));
    }

    // endregion
}
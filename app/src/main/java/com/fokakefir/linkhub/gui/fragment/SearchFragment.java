package com.fokakefir.linkhub.gui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.recyclerview.PlaceAdapter;
import com.fokakefir.linkhub.model.Place;

import java.util.ArrayList;


public class SearchFragment extends Fragment  implements View.OnClickListener{

    private MainActivity activity;

    private RecyclerView recyclerView;
    private EditText txtSearch;
    private Button btnSearch;
    private PlaceAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private View view;

    public SearchFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        this.view = inflater.inflate(R.layout.fragment_search, container, false);
        this.txtSearch = this.view.findViewById(R.id.txt_frag_search);
        this.btnSearch = this.view.findViewById(R.id.btn_frag_search);

        btnSearch.setOnClickListener(this);

        ArrayList<Place> places = new ArrayList<>();

        places.add(new Place("Halasz Bastya","Johely","https://firebasestorage.googleapis.com/v0/b/tempapp-55605.appspot.com/o/images%2F1678901257164.jpg?alt=media&token=8dd390ab-1755-46cc-86b5-0c061807c94a"));
        places.add(new Place("Bazilika","Johely","https://firebasestorage.googleapis.com/v0/b/tempapp-55605.appspot.com/o/images%2F1678901257164.jpg?alt=media&token=8dd390ab-1755-46cc-86b5-0c061807c94a"));
        places.add(new Place("Parlament","Johely","https://firebasestorage.googleapis.com/v0/b/tempapp-55605.appspot.com/o/images%2F1678901257164.jpg?alt=media&token=8dd390ab-1755-46cc-86b5-0c061807c94a"));

        this.recyclerView = this.view.findViewById(R.id.recycler_Post);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.adapter = new PlaceAdapter(this.getContext(),places);
        this.recyclerView.setAdapter(adapter);



        return this.view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_frag_search){
            Toast.makeText(activity, this.txtSearch.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
package com.fokakefir.linkhub.gui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.recyclerview.ReviewAdapter;
import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReviewFragment extends Fragment {

    // region 1. Declaration

    private MainActivity activity;


    private FloatingActionButton fabCreateReview;
    private TextView txtTitle;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    private List<Review> reviews;

    private View view;

    private Place place;

    // endregion

    // region 2. Constructor and Lifecycle

    public ReviewFragment(MainActivity activity, Place place) {
        this.activity = activity;
        this.place = place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_review, container, false);

        this.reviews = new ArrayList<>();
        this.reviews.add(new Review("123", "Sanyi", "Nagyon vagany hely\n meg fogok jonni", null, 6, new Timestamp(new Date())));
        this.reviews.add(new Review("123", "Sanyi", "Nagyon vagany hely\n meg fogok jonni", null, 6, new Timestamp(new Date())));
        this.reviews.add(new Review("123", "Sanyi", "Nagyon vagany hely\n meg fogok jonni", "http://fokakefir.go.ro/lemon_app/images/post_3f766558-a8dd-4a13-86d2-e0ae790c12aa.jpg", 6, new Timestamp(new Date())));


        this.txtTitle = this.view.findViewById(R.id.txt_review_place_title);
        this.txtTitle.setText(place.getName());

        this.recyclerView = this.view.findViewById(R.id.recyclre_view_reviews);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.adapter = new ReviewAdapter(this.activity, this.reviews);
        this.recyclerView.setAdapter(this.adapter);

        return this.view;
    }

    // endregion
}
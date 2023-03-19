package com.fokakefir.linkhub.gui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.fokakefir.linkhub.logic.database.ReviewsDatabaseManager;
import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReviewFragment extends Fragment implements View.OnClickListener, ReviewsDatabaseManager.OnResponseListener, ReviewAdapter.OnReviewListener {

    // region 1. Declaration

    private MainActivity activity;

    private FloatingActionButton fabCreateReview;
    private TextView txtTitle;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    private List<Review> reviews;

    private View view;

    private Place place;

    private ReviewsDatabaseManager databaseManager;

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

        this.txtTitle = this.view.findViewById(R.id.txt_review_place_title);
        this.txtTitle.setText(place.getName());

        this.recyclerView = this.view.findViewById(R.id.recyclre_view_reviews);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.adapter = new ReviewAdapter(this.activity, this, this.reviews);
        this.recyclerView.setAdapter(this.adapter);

        this.fabCreateReview = this.view.findViewById(R.id.fab_add_review);
        this.fabCreateReview.setOnClickListener(this);

        this.databaseManager = new ReviewsDatabaseManager(this, this.place);

        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.databaseManager.setSnapshotListener();
        this.databaseManager.checkReviewStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.databaseManager.removeSnapshotListener();
    }

    // endregion

    // region 3. Button listener

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add_review) {
            this.activity.writeReview(this.place);
        }
    }

    // endregion

    // region 4. Database listener

    @Override
    public void onAdded(Review review, int pos) {
        this.reviews.add(review);
        this.adapter.notifyItemInserted(pos);
        this.databaseManager.checkReviewStatus();
    }

    @Override
    public void onDeleted(int pos) {
        this.reviews.remove(pos);
        this.adapter.notifyItemRemoved(pos);
        this.databaseManager.checkReviewStatus();
    }

    @Override
    public void onEdited(Review review, int pos) {
        this.reviews.set(pos, review);
        this.adapter.notifyItemChanged(pos);
    }

    @Override
    public void onReviewStatus(boolean alreadyReviewed) {
        if (alreadyReviewed)
            this.fabCreateReview.setVisibility(View.GONE);
        else
            this.fabCreateReview.setVisibility(View.VISIBLE);
    }

    // endregion

    // region 5. RecyclerView listener

    @Override
    public void onDeleteReview(int pos) {
        Review review = this.reviews.get(pos);
        this.databaseManager.deleteReview(review);
    }

    // endregion
}
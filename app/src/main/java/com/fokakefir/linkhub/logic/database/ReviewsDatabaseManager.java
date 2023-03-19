package com.fokakefir.linkhub.logic.database;

import androidx.annotation.Nullable;

import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.Review;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class ReviewsDatabaseManager implements EventListener<QuerySnapshot> {
    private static final String TAG = "ReviewDatabaseManager";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reviewsRef;
    private ListenerRegistration snapshotListener;

    private Place place;

    private OnResponseListener listener;

    public ReviewsDatabaseManager(OnResponseListener listener, Place place) {
        this.place = place;
        this.listener = listener;

        this.reviewsRef = db.collection("places").document(place.getId()).collection("reviews");
    }

    public void setSnapshotListener() {
        if (this.snapshotListener != null)
            return;
        this.snapshotListener = this.reviewsRef.addSnapshotListener(this);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            return;
        }
    }

    public interface OnResponseListener {
        void onReviewAdded(Review review, int pos);
    }

}

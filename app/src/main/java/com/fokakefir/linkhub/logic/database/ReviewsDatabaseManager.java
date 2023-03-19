package com.fokakefir.linkhub.logic.database;

import androidx.annotation.Nullable;

import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.Review;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
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
        this.snapshotListener = this.reviewsRef.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(this);
    }

    public void removeSnapshotListener () {
        if (this.snapshotListener == null)
            return;
        this.snapshotListener.remove();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            return;
        }

        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            DocumentSnapshot documentSnapshot = dc.getDocument();

            Review review = documentSnapshot.toObject(Review.class);
            review.setDocumentId(documentSnapshot.getId());

            int oldIndex = dc.getOldIndex();
            int newIndex = dc.getNewIndex();

            switch (dc.getType()) {
                case ADDED:
                    listener.onAdded(review, newIndex);
                    break;
                case REMOVED:
                    listener.onDeleted(oldIndex);
                    break;
                case MODIFIED:
                    listener.onEdited(review, newIndex);
                    break;
            }
        }
    }

    public interface OnResponseListener {
        void onAdded(Review review, int pos);
        void onDeleted(int pos);
        void onEdited(Review review, int pos);
    }

}

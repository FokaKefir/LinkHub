package com.fokakefir.linkhub.logic.database;

import androidx.annotation.Nullable;

import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.Review;
import com.fokakefir.linkhub.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReviewsDatabaseManager implements EventListener<QuerySnapshot> {
    private static final String TAG = "ReviewDatabaseManager";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DocumentReference userRef = db.collection("users").document(auth.getUid());
    private CollectionReference reviewsRef;
    private ListenerRegistration snapshotListener;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("review_images");


    private Place place;

    private OnResponseListener listener;

    public ReviewsDatabaseManager(OnResponseListener listener, Place place) {
        this.place = place;
        this.listener = listener;

        this.reviewsRef = db.collection("places").document(place.getId()).collection("reviews");
    }

    public void checkReviewStatus() {
        this.userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getPlaceIds().contains(place.getId())) {
                            listener.onReviewStatus(true);
                        } else {
                            listener.onReviewStatus(false);
                        }
                    }
                });
    }

    public void setSnapshotListener() {
        if (this.snapshotListener != null)
            return;
        this.snapshotListener = this.reviewsRef.orderBy("timestamp").addSnapshotListener(this);
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

    public void deleteReview(Review review) {
        String docId = review.getDocumentId();

        DocumentReference reviewRef = this.reviewsRef.document(docId);
        if (review.getImageUrl() == null) {
            this.userRef.update("placeIds", FieldValue.arrayRemove(place.getId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    reviewRef.delete();
                }
            });
        } else {
            StorageReference imageRef = this.storageReference.getStorage().getReferenceFromUrl(review.getImageUrl());
            imageRef.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            userRef.update("placeIds", FieldValue.arrayRemove(place.getId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    reviewRef.delete();
                                }
                            });
                        }
                    });
        }

    }

    private void updateUserRef() {

    }

    public interface OnResponseListener {
        void onReviewStatus(boolean alreadyReviewed);
        void onAdded(Review review, int pos);
        void onDeleted(int pos);
        void onEdited(Review review, int pos);
    }

}

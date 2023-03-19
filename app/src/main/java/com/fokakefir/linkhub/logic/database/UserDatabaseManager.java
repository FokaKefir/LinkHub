package com.fokakefir.linkhub.logic.database;

import androidx.annotation.Nullable;

import com.fokakefir.linkhub.logic.api.PlacesApi;
import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Collections;
import java.util.List;

public class UserDatabaseManager implements EventListener<DocumentSnapshot> {

    private PlacesApi api;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference userRef;

    private ListenerRegistration snapshotListener;
    private String userId;

    private OnResponseListener listener;

    public UserDatabaseManager(OnResponseListener listener) {
        this.listener = listener;
        this.userRef = db.collection("users").document(auth.getUid());
        this.api = new PlacesApi(this.listener);
    }

    public UserDatabaseManager(OnResponseListener listener, String userId) {
        this.listener = listener;
        this.userRef = db.collection("users").document(userId);
        this.api = new PlacesApi(this.listener);
        this.userId = userId;
    }

    public void setSnapshotListener() {
        if (this.snapshotListener != null)
            return;

        this.snapshotListener = this.userRef.addSnapshotListener(this);
    }

    public void removeSnapshotListener() {
        this.snapshotListener.remove();
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
        if (error != null)
            return;

        User user = value.toObject(User.class);
        listener.onUserDataChanged(user);

        checkRelation();

        List<String> placeIds = user.getPlaceIds();
        Collections.reverse(placeIds);
        for (String placeId : placeIds) {
            this.api.sendPlaceRequest(placeId);
        }
    }

    private void checkRelation() {
        if (this.userId == null) {
            return;
        }
        this.db.collection("users").document(auth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    return;

                User user = documentSnapshot.toObject(User.class);
                List<String> followingIds = user.getFollowingIds();
                if(followingIds.contains(userId)) {
                    listener.onFollowing(true);
                } else {
                    listener.onFollowing(false);
                }
            }
        });

    }

    public void followUser(String userId) {
        DocumentReference userFollowerRef = this.db.collection("users").document(auth.getUid());
        DocumentReference userFollowingRef = db.collection("users").document(userId);
        userFollowerRef.update("followingIds", FieldValue.arrayUnion(userId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userFollowingRef.update("followerIds", FieldValue.arrayUnion(auth.getUid()));
                    }
                });
    }

    public void unfollowUser(String userId) {
        DocumentReference userFollowerRef = this.db.collection("users").document(auth.getUid());
        DocumentReference userFollowingRef = db.collection("users").document(userId);
        userFollowerRef.update("followingIds", FieldValue.arrayRemove(userId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userFollowingRef.update("followerIds", FieldValue.arrayRemove(auth.getUid()));
                    }
                });
    }


    public interface OnResponseListener extends PlacesApi.OnResponseListener {
        void onUserDataChanged(User user);
        void onFollowing(boolean following);
    }
}

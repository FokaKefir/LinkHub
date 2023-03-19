package com.fokakefir.linkhub.logic.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fokakefir.linkhub.logic.api.PlacesApi;
import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeDatabaseManager {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private DocumentReference userRef = db.collection("users").document(auth.getUid());
    private ListenerRegistration snapshotListener;

    private PlacesApi api;

    private OnHomeListener listener;

    public HomeDatabaseManager(OnHomeListener listener) {
        this.listener = listener;
        this.api = new PlacesApi(this.listener);

    }

    public void setSnapshotListener() {
        if (this.snapshotListener != null)
            return;
        this.snapshotListener = this.userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    return;

                User user = value.toObject(User.class);
                List<String> userIds = user.getFollowingIds();
                getUserIdsData(userIds);
            }
        });
    }

    public void removeSnapshotListener() {
        if (this.snapshotListener == null)
            return;
        this.snapshotListener.remove();
    }

    public void getUserIdsData(List<String> userIds) {
        List<String> pIds = new ArrayList<>();

        List<Task> tasks = new ArrayList<>();
        tasks.add(this.userRef.get());
        for (String userId : userIds) {
            tasks.add(this.usersRef.document(userId).get());
        }

        Task<List<QueryDocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QueryDocumentSnapshot>>() {
            @Override
            public void onSuccess(List<QueryDocumentSnapshot> queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    List<String> placeIds = user.getPlaceIds();
                    Collections.reverse(placeIds);
                    for (String placeId : placeIds) {
                        if (!pIds.contains(placeId)) {
                            pIds.add(placeId);
                        }
                    }
                }

                getAllPlaces(pIds);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailed(e.toString());
            }
        });
    }


    private void getAllPlaces(List<String> placeIds) {
        for (String placeId : placeIds) {
            this.api.sendPlaceRequest(placeId);
        }
    }

    public interface OnHomeListener extends PlacesApi.OnResponseListener {
        void onFailed(String errorMessage);
    }
}

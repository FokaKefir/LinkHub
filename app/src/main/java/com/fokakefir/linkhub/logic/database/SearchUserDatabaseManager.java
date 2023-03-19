package com.fokakefir.linkhub.logic.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fokakefir.linkhub.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchUserDatabaseManager {

    public static final String TAG = "SearchUserDatabaseManager";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");

    private OnSearchUserListener listener;

    public SearchUserDatabaseManager(OnSearchUserListener listener) {
        this.listener = listener;
    }

    public void searchForUsers(String input) {
        this.usersRef
                .whereEqualTo("name",  input)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            User user = documentSnapshot.toObject(User.class);
                            user.setDocId(documentSnapshot.getId());
                            listener.onAddUser(user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                        listener.onFailed(e.toString());
                    }
                });
    }

    public interface OnSearchUserListener {
        void onAddUser(User user);
        void onFailed(String errorMessage);
    }

}

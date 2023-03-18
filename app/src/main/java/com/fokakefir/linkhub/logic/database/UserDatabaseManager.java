package com.fokakefir.linkhub.logic.database;

import androidx.annotation.Nullable;

import com.fokakefir.linkhub.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class UserDatabaseManager implements EventListener<DocumentSnapshot> {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference userRef = db.collection("users").document(auth.getUid());

    private ListenerRegistration snapshotListener;

    private OnResponseListener listener;

    public UserDatabaseManager(OnResponseListener listener) {
        this.listener = listener;
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
    }

    public interface OnResponseListener {
        void onUserDataChanged(User user);
    }
}

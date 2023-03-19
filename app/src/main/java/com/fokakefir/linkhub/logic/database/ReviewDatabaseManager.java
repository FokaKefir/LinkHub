package com.fokakefir.linkhub.logic.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.content.Context;


import androidx.annotation.NonNull;

import com.fokakefir.linkhub.model.Review;
import com.fokakefir.linkhub.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class ReviewDatabaseManager {

    public static final String TAG = "ReviewDatabaseManager";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("review_images");
    private DocumentReference userRef = db.collection("users").document(auth.getUid());
    private CollectionReference reviewsRef;

    private UploadTask uploadTask;

    private User user;
    private String placeId;
    private OnResponseListener listener;

    private Context context;

    public ReviewDatabaseManager(OnResponseListener listener, Context context, String placeId) {
        this.listener = listener;
        this.placeId = placeId;
        this.context = context;
        this.reviewsRef = db.collection("places").document(placeId).collection("reviews");
    }

    public void getUserData() {
        this.userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        setUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                        listener.onFailed(e.toString());
                    }
                });
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void uploadReview(String content, int rate, String url) {
        Review review = new Review(
                this.auth.getUid(),
                this.user.getName(),
                content,
                url,
                rate, new Timestamp(new Date())
        );
        this.user.addPlaceId(this.placeId);

        this.reviewsRef.add(review)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                        listener.onFailed(e.toString());
                    }
                });

    }

    private void updateUser() {
        this.userRef.set(this.user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onUploaded();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                        listener.onFailed(e.toString());
                    }
                });
    }

    public void uploadReviewWithImage(String content, int rate, Uri uri) {

        if (uri == null) {
            uploadReview(content, rate, null);
        } else {
            StorageReference fileReference = this.storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));


            this.uploadTask = fileReference.putFile(uri);
            Task<Uri> urlTask = this.uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String url = task.getResult().toString();
                            uploadReview(content, rate, url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.toString());
                            listener.onFailed(e.toString());
                        }
                    });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public Boolean isUploadInProgress() {
        return (this.uploadTask != null && this.uploadTask.isInProgress());
    }

    public interface OnResponseListener {
        void onUploaded();

        void onFailed(String errorMessage);
    }
}

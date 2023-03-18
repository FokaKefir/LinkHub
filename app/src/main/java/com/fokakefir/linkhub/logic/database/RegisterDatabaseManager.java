package com.fokakefir.linkhub.logic.database;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.fokakefir.linkhub.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class RegisterDatabaseManager {

    private static final String TAG = "RegisterDatabaseManager";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private OnResponseListener listener;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

    private StorageTask uploadTask;
    private Context context;

    public RegisterDatabaseManager(OnResponseListener listener) {
        this.listener = listener;
    }

    public void createAccount(String username, String email, String password, Uri uri) {
        this.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserData(username, uri);

                        } else {
                            Log.d(TAG, task.getException().getMessage());
                            listener.onFailed(task.getException().getMessage());
                        }
                    }
                });

    }

    private void addUserData(String username, Uri uri) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onRegistered();
                            uploadImage(uri);
                            sendEmailVerification();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                            listener.onFailed(task.getException().getMessage());
                        }
                    }
                });
        User user = new User(username, null);
        this.usersRef.document(this.auth.getCurrentUser().getUid()).set(user);

    }

    private void uploadImage(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileReference = this.storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            this.uploadTask = fileReference.putFile(imageUri);
            Task<Uri> urlTask = this.uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileReference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String url = task.getResult().toString();
                            addUrlToUser(url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.toString());
                        }
                    });
        }
    }

    private void addUrlToUser(String url) {

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void sendEmailVerification() {
        final FirebaseUser user = this.auth.getCurrentUser();
        if (user == null)
            return;
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "email is sent");
                    }
                });
    }

    public interface OnResponseListener {
        void onRegistered();
        void onFailed(String errorMessage);
    }
}

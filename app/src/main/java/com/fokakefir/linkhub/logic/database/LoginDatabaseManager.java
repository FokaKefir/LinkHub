package com.fokakefir.linkhub.logic.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginDatabaseManager {
    private static final String TAG = "LoginDatabaseManager";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private OnResponseListener listener;
    public LoginDatabaseManager(OnResponseListener listener) {
        this.listener = listener;
    }
    public void signIn(String email, String password) {
        this.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user.isEmailVerified())
                                listener.onSignedIn(user);
                            else
                                listener.onFailed("Email is not verified");
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                            listener.onFailed(task.getException().getMessage());
                        }
                    }
                });
    }
    public void checkSignedInUser() {
        FirebaseUser currentUser = this.auth.getCurrentUser();
        if(currentUser != null){
            if (currentUser.isEmailVerified())
                listener.onSignedIn(currentUser);
            else
                listener.onFailed("Email is not verified");
        }
    }
    public interface OnResponseListener {
        void onSignedIn(FirebaseUser user);
        void onFailed(String errorMessage);
    }
}

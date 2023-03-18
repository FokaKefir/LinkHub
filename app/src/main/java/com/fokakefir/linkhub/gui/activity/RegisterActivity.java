package com.fokakefir.linkhub.gui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.logic.database.RegisterDatabaseManager;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RegisterDatabaseManager.OnResponseListener{
    private static final Pattern PASSWORD_PATTERS =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,20}" +             //at least 6 characters
                    "$");

    public static final Pattern USERNAME_PATTERS = Pattern.compile("^[A-Za-z]\\w{0,16}$");

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private TextInputLayout txtUsername;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;
    private TextInputLayout txtConfirmPassword;
    private CircleImageView userImage;
    private Uri userImageUri;
    private Button btnRegister;

    private RegisterDatabaseManager registerDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.txtUsername = findViewById(R.id.txt_new_username);
        this.txtEmail = findViewById(R.id.txt_new_email);
        this.txtPassword = findViewById(R.id.txt_new_password);
        this.txtConfirmPassword = findViewById(R.id.txt_confirm_password);
        this.btnRegister = findViewById(R.id.btn_register);
        this.userImage = findViewById(R.id.img_register_user);

        this.userImage.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);

        this.registerDatabaseManager = new RegisterDatabaseManager(this);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                openFileChooser();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            this.userImageUri = data.getData();

            Picasso.with(this).load(this.userImageUri).into(this.userImage);
        }
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            String username = this.txtUsername.getEditText().getText().toString().trim();
            String email = this.txtEmail.getEditText().getText().toString().trim();
            String password = this.txtPassword.getEditText().getText().toString().trim();
            String confirmPassword = this.txtConfirmPassword.getEditText().getText().toString().trim();


            if (!validateName(username) | !validateEmail(email) | !validatePassword(password) | !validatePasswordAgain(password, confirmPassword)) {
                return;
            }
            this.registerDatabaseManager.createAccount(username, email, password, userImageUri);

        } else if (view.getId() == R.id.img_register_user) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                requestStoragePermission();
            }
        }
    }

    private boolean validateName(String strName) {
        if (strName.isEmpty()) {
            this.txtUsername.setError("Field can't be empty");
            return false;
        } else if (strName.length() > 16) {
            this.txtUsername.setError("Username too long");
            return false;
        } else if (!USERNAME_PATTERS.matcher(strName).matches()) {
            this.txtUsername.setError("Invalid characters");
            return false;
        } else {
            this.txtUsername.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String strEmail) {
        if (strEmail.isEmpty()) {
            this.txtEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            this.txtEmail.setError("Please enter a valid email address");
            return false;
        } else {
            this.txtEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String strPassword) {
        if (strPassword.isEmpty()) {
            this.txtPassword.setError("Field can't be empty");
            return false;
        } else if (strPassword.length() > 20) {
            this.txtPassword.setError("Password too long");
            return false;
        } else if (!PASSWORD_PATTERS.matcher(strPassword).matches()) {
            this.txtPassword.setError("Password too weak");
            return false;
        } else {
            this.txtPassword.setError(null);
            return true;
        }
    }

    private boolean validatePasswordAgain(String strPassword, String strPasswordAgain) {
        if (!strPassword.equals(strPasswordAgain)){
            this.txtConfirmPassword.setError("Passwords need to match");
            return false;
        } else {
            this.txtConfirmPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onRegistered() {
        Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public void onFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}
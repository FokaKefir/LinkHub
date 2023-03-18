package com.fokakefir.linkhub.gui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
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

    private TextInputLayout txtUsername;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;
    private TextInputLayout txtConfirmPassword;
    private CircleImageView userImage;
    private Button btnRegister;

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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            String username = this.txtUsername.getEditText().getText().toString().trim();
            String email = this.txtEmail.getEditText().getText().toString().trim();
            String password = this.txtPassword.getEditText().getText().toString().trim();
            String confirmPassword = this.txtConfirmPassword.getEditText().getText().toString().trim();

            if (view.getId() == R.id.btn_register) {
                uploadUserData();
            }

            if (username.isEmpty()) {
                Toast.makeText(this, "username cant be empty", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(this, "email cant be empty", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "password cant be empty", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "password must be longer", Toast.LENGTH_SHORT).show();
            } else if (!password.matches(confirmPassword)) {
                Toast.makeText(this, "passwords doesn't match", Toast.LENGTH_SHORT).show();
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
    private void onnRegistered() {
        Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void uploadUserData() {
        String strName = this.txtUsername.getEditText().getText().toString().trim();
        String strEmail = this.txtEmail.getEditText().getText().toString().trim();
        String strPassword = this.txtPassword.getEditText().getText().toString().trim();
        String strPasswordAgain = this.txtConfirmPassword.getEditText().getText().toString().trim();

        if (!validateName(strName) | !validateEmail(strEmail) | !validatePassword(strPassword) | !validatePasswordAgain(strPassword, strPasswordAgain)) {
            return;
        }
    }

//    @Override
//    public void onFailed(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
//    }
}
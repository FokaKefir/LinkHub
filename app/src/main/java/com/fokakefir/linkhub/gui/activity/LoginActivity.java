package com.fokakefir.linkhub.gui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.logic.database.LoginDatabaseManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginDatabaseManager.OnResponseListener {
    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;
    private Button btnLogin;
    private TextView txtCreateAccount;
    private CircleImageView logoLoginImage;
    private LoginDatabaseManager loginDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.txtEmail = findViewById(R.id.txt_email);
        this.txtPassword = findViewById(R.id.txt_password);
        this.btnLogin = findViewById(R.id.btn_login);
        this.txtCreateAccount = findViewById(R.id.txt_create_account);
        this.logoLoginImage = findViewById(R.id.img_login_user_logo);

        this.btnLogin.setOnClickListener(this);
        this.txtCreateAccount.setOnClickListener(this);
        Glide.with(this).load(R.mipmap.ic_launcher_login).into(this.logoLoginImage);

        this.loginDatabaseManager = new LoginDatabaseManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.loginDatabaseManager.checkSignedInUser();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            String strEmail = this.txtEmail.getEditText().getText().toString().trim();
            String strPassword = this.txtPassword.getEditText().getText().toString().trim();
            if (strEmail.isEmpty()) {
                Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
            } else if (strPassword.isEmpty()) {
                Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
            } else {
                this.loginDatabaseManager.signIn(strEmail, strPassword);
            }
        } else if (view.getId() == R.id.txt_create_account) {
            createAccount();
        }
    }

    private void createAccount() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignedIn(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
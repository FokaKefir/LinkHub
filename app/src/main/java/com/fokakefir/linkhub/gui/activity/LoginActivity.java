package com.fokakefir.linkhub.gui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;
    private Button btnLogin;
    private TextView txtCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.txtEmail = findViewById(R.id.txt_email);
        this.txtPassword = findViewById(R.id.txt_password);
        this.btnLogin = findViewById(R.id.btn_login);
        this.txtCreateAccount = findViewById(R.id.txt_create_account);

        this.btnLogin.setOnClickListener(this);
        this.txtCreateAccount.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            }
        }else if (view.getId() == R.id.txt_create_account) {
            createAccount();
        }
    }

    private void createAccount() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onSignedIn(FirebaseUser user) {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void onFailed(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
//    }
}
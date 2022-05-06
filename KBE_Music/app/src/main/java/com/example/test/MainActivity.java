package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button btnLogin,btnRegister;

    DatabaseHelper db;

    TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String phoneInput = editPhone.getText().toString().trim();
            String passwordInput = editPassword.getText().toString().trim();

            btnLogin.setEnabled(!phoneInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        editPhone = findViewById(R.id.edit_phone);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_registerMain);

        editPhone.addTextChangedListener(loginTextWatcher);
        editPassword.addTextChangedListener(loginTextWatcher);

        btnLogin.setEnabled(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editPhone.getText().toString(), password = editPassword.getText().toString();

                if(db.checkLogin(username, password)) {
                    Intent switchMP = new Intent(MainActivity.this, MusicListActivity.class);
                    startActivity(switchMP);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(switchRegister);
            }
        });

    }

    public void login() {

    }
}
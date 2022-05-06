package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText editName, editPhone, editPass, editConfirm, editEmail;
    TextView textWrongPass;
    Button btnRegister;

    DatabaseHelper db;

    TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nameInput = editName.getText().toString().trim();
            String phoneInput = editPhone.getText().toString().trim();
            String passInput = editPass.getText().toString().trim();
            String confirmInput = editConfirm.getText().toString().trim();
            String emailInput = editEmail.getText().toString().trim();

            btnRegister.setEnabled(!nameInput.isEmpty() && !phoneInput.isEmpty() && !passInput.isEmpty() && !confirmInput.isEmpty() && !emailInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher passwordCheckTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void afterTextChanged(Editable editable) {
            String confirmInput = editConfirm.getText().toString();
            String passInput = editPass.getText().toString();
            if(passInput != confirmInput)
                textWrongPass.setVisibility(View.VISIBLE);
            else
                textWrongPass.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editPass = findViewById(R.id.edit_password);
        editConfirm = findViewById(R.id.edit_confirm);
        editEmail = findViewById(R.id.edit_email);

        textWrongPass = findViewById(R.id.text_wrong_pass);

        btnRegister = findViewById(R.id.btn_register);

        editName.addTextChangedListener(registerTextWatcher);
        editPhone.addTextChangedListener(registerTextWatcher);
        editPass.addTextChangedListener(registerTextWatcher);
        editConfirm.addTextChangedListener(registerTextWatcher);
        editConfirm.addTextChangedListener(passwordCheckTextWatcher);
        editEmail.addTextChangedListener(registerTextWatcher);

        btnRegister.setEnabled(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString(),
                        phone = editPhone.getText().toString(),
                        password = editPass.getText().toString(),
                        email = editEmail.getText().toString();
                if(db.checkUsername(phone))
                    if(db.insertUser(phone, password, email, name))
                        Toast.makeText(getApplicationContext(), "Registered successfully!",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "An issue occurred!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "This phone number already exists!", Toast.LENGTH_LONG).show();








                // Sending Email
                /*
                String emailSend = editEmail.getText().toString();
                String emailSubject = "KBE MUSIC";
                String emailBody =
                        editName.getText().toString() + "\n" +
                        editPhone.getText().toString();

                Intent intRegister = new Intent(Intent.ACTION_SEND);
                intRegister.setType("message/rfc822");
                intRegister.putExtra(Intent.EXTRA_EMAIL, new String[] {emailSend});
                intRegister.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intRegister.putExtra(Intent.EXTRA_TEXT, emailBody);

                startActivity(Intent.createChooser(intRegister, "Choose an Email client: "));

                 */
            }
        });
    }

}
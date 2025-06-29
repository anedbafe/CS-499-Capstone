package com.zybooks.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.inventoryapp.model.User;
import com.zybooks.inventoryapp.network.ApiService;
import com.zybooks.inventoryapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    // UI ELEMENTS
    EditText getUsername, getRegisterEmail, getRegisterPassword;
    Button btnSubmitRegister, btnBack;

    // ON CREATE METHOD TO INITIALIZE ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // LINK UI ELEMENTS WITH LAYOUT
        getUsername = findViewById(R.id.getUsername);
        getRegisterEmail = findViewById(R.id.getRegisterEmail);
        getRegisterPassword = findViewById(R.id.getRegisterPassword);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
        btnBack = findViewById(R.id.btnCancel);

        // BACK BUTTON RETURNS TO LOGIN SCREEN
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // REGISTER BUTTON SUBMITS FORM TO API
        btnSubmitRegister.setOnClickListener(view -> {
            String username = getUsername.getText().toString().trim();
            String email = getRegisterEmail.getText().toString().trim();
            String password = getRegisterPassword.getText().toString().trim();

            // VALIDATE EMPTY FIELDS
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Complete all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // CALL METHOD TO REGISTER USER
            registerUser(username, email, password);
        });
    }

    // SEND USER REGISTRATION DATA TO API USING RETROFIT
    private void registerUser(String username, String email, String password) {
        ApiService apiService = RetrofitClient.getApiService();
        User user = new User(username, email, password);

        Call<Void> call = apiService.registerUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registration completed!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Email already registered or error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "API ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

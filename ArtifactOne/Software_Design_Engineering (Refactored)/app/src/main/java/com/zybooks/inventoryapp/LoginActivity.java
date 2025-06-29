package com.zybooks.inventoryapp;

// IMPORT REQUIRED PACKAGES
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

public class LoginActivity extends AppCompatActivity {

    // UI ELEMENT VARIABLES
    EditText getEmail, getPassword;
    Button btnLogin, btnRegister;

    // INITIALIZE ACTIVITY AND SET LAYOUT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // LINK UI ELEMENTS WITH XML IDs
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // LOGIN BUTTON CLICK EVENT
        btnLogin.setOnClickListener(view -> {
            String email = getEmail.getText().toString().trim();
            String password = getPassword.getText().toString().trim();

            // VALIDATE EMAIL AND PASSWORD FIELDS
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email and Password are required!", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // REGISTER BUTTON CLICK EVENT
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // LOGIN USER USING RETROFIT API
    private void loginUser(String email, String password) {
        ApiService apiService = RetrofitClient.getApiService();
        User user = new User(email, password); // CREATE USER OBJECT WITH EMAIL AND PASSWORD

        Call<User> call = apiService.loginUser(user);
        call.enqueue(new Callback<User>() {

            // HANDLE SUCCESSFUL RESPONSE
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Welcome, login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login denied. \nVerify your Email or Password!", Toast.LENGTH_LONG).show();
                }
            }

            // HANDLE API CALL FAILURE
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "API ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

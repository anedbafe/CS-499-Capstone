package com.zybooks.inventoryapp;

// packages importing
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.zybooks.inventoryapp.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    // activity main elements vars
    private EditText getEmail, getPassword;
    private Button btnLogin, btnRegister;
    private DBHelper dbHelper; // db instance

    // .XML ACTIVITY_LOGIN CALL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // METHOD CALLS
        setElements();
        setLoginListener();
        setRegisterListener();
    }

    // LINKING LAYOUT ELEMENTS TO VARIABLES
    private void setElements() {
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DBHelper(this); // Initialize DBHelper
    }

    // LOGIN BUTTON LISTENER
    private void setLoginListener() {
        btnLogin.setOnClickListener(view -> handleLogin());
    }

    // REGISTER BUTTON LISTENER
    private void setRegisterListener() {
        btnRegister.setOnClickListener(view -> openRegisterActivity());
    }

    // HANDLE LOGIN LOGIC
    private void handleLogin() {
        String email = getEmail.getText().toString().trim();
        String password = getPassword.getText().toString().trim();

        // CHECK IF EMAIL OR PASSWORD IS EMPTY
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Check your Email and/or Password.", Toast.LENGTH_SHORT).show();
        } else {
            boolean loginSuccess = dbHelper.loginUserCheck(email, password);
            if (loginSuccess) {
                Toast.makeText(this, "Welcome, login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Prevent back navigation to login
            } else {
                Toast.makeText(this, "Login denied.\nCheck your Email and/or Password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // REDIRECT TO REGISTER SCREEN
    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

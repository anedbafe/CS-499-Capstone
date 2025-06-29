package com.zybooks.inventoryapp;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.zybooks.inventoryapp.database.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText getFirstName, getRegisterEmail, getRegisterPassword;
    Button btnSubmitRegister, btnBack;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // xml layout elements
        getFirstName = findViewById(R.id.getFirstName);
        getRegisterEmail = findViewById(R.id.getRegisterEmail);
        getRegisterPassword = findViewById(R.id.getRegisterPassword);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
        btnBack = findViewById(R.id.btnCancel);
        dbHelper = new DBHelper(this);

        // btn cancel return to login
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // btn register submit method
        btnSubmitRegister.setOnClickListener(view -> {
            String username = getFirstName.getText().toString().trim();
            String email = getRegisterEmail.getText().toString().trim();
            String password = getRegisterPassword.getText().toString().trim();

            // check if fields are empty
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Complete all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if user in the db already in db only check for email, since a user can have same name but email differ
            boolean userExists = dbHelper.userEmailInDB(email);
            if (userExists) {
                Toast.makeText(RegisterActivity.this, "Email already registered, use other email.", Toast.LENGTH_SHORT).show();
            } else {
                // register user in db
                boolean insertSuccess = dbHelper.registerUser(username, email, password);
                if (insertSuccess) {
                    Toast.makeText(RegisterActivity.this, "User registration completed!", Toast.LENGTH_SHORT).show();

                    // After user is register will and success will return to login screen
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration process failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

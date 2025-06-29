package com.zybooks.inventoryapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST = 1;
    private ToggleButton toggleSMS;
    private SharedPreferences.Editor editor;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        toggleSMS = findViewById(R.id.toggleSMS);
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        editor = preferences.edit();

        // when app load call previously user preference for sms
        boolean isSMSEnabled = preferences.getBoolean("sms_enabled", false);
        toggleSMS.setChecked(isSMSEnabled);

        // bnt sms authorization toggle
        toggleSMS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // ask for permit if never been asked for return values
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST);
                } else {
                    enableSMS(true);
                }
            } else {
                enableSMS(false);
            }
        });

        // menu bar
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_modify) {
                startActivity(new Intent(SettingsActivity.this, ModifyActivity.class));
                return true;
            } else
                return id == R.id.nav_settings;
        });

        bottomNavigation.setSelectedItemId(R.id.nav_settings);
    }

    // Check when user granted or denied sms permit and toast message
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableSMS(true);
                Toast.makeText(this, "SMS Granted!", Toast.LENGTH_SHORT).show();
            } else {
                enableSMS(false);
                toggleSMS.setChecked(false);
                Toast.makeText(this, "SMS Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // saved sms user preference
    private void enableSMS(boolean isEnabled) {
        editor.putBoolean("sms_enabled", isEnabled);
        editor.apply();
    }
}

package com.zybooks.inventoryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zybooks.inventoryapp.database.DBHelper;

import java.util.ArrayList;

// Class segregation into multiple method for easier future maintenance
public class MainActivity extends AppCompatActivity {

    // NO ALERT SMS DEFAULT
    private static final String DEFAULT_SMS_ALERT = "No item with minimum stock";

    // SMS PERMISSION
    private static final int SMS_PERMIT_CODE = 1;

    // .XML ELEMENTS
    private BottomNavigationView bottomNavigation;
    private ListView productListView;
    private EditText searchProduct;
    private TextView smsAlerts;

    // DB / Adapters
    private DBHelper dbHelper;
    ArrayList<String> productList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSMSPermit(); // CHECK PERMIT GRANTED
        setElements();    // LINK XML ELEMENTS
        setListeners();   // ON REAL-TIME LISTENER : PRODUCT SEARCH
        loadSMSAlerts();  // APP LOAD PREVIOUS SMS ALERTS
        setLoadProducts(""); // LOAD PRODUCT AND FILTER
    }

    // CHECK PERMIT GRANTED
    private void checkSMSPermit() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // IF NO PERMIT IS GRANTED PERMIT WILL BE REQUEST
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMIT_CODE);
        }
    }

    // LINK .XML ELEMENTS
    private void setElements() {
        // ELEMENTS FROM XML ACTIVITY_MAIN
        productListView = findViewById(R.id.productListView);
        searchProduct = findViewById(R.id.searchProduct);
        smsAlerts = findViewById(R.id.smsAlerts);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        dbHelper = new DBHelper(this); // DB instance
        productList = new ArrayList<>(); // future replacement with HashMap
    }

    // ON REAL-TIME LISTENER : PRODUCT SEARCH
    private void setListeners() {
        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setLoadProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // BOTTOM MENU SETTING LISTENER
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_modify) {
                startActivity(new Intent(MainActivity.this, ModifyActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    // FILTER PRODUCTS METHOD
    private void setLoadProducts(String filter) {
        productList.clear();

        smsAlerts.setText(DEFAULT_SMS_ALERT);
        smsAlerts.setVisibility(TextView.GONE);

        Cursor cursor = dbHelper.readProducts();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String quantity = cursor.getString(2);
                String location = cursor.getString(3);

                // product display information
                String productInfo = name + " - Qty: " + quantity + " - Loc: " + location;

                // when user type in the filter text this will look up for product
                if (filter.isEmpty() || name.toLowerCase().contains(filter.toLowerCase())) {
                    productList.add(productInfo);
                }

                // check stock availability and send alert
                int qty = Integer.parseInt(quantity);
                if (qty <= 1) {
                    updateSmsAlerts("Low stock: " + name + " (" + qty + " available)");
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    // METHOD TO STORE ALL ALERTS FOR PRODUCT LOW AVAILABILITY
    @SuppressLint("SetTextI18n")
    private void updateSmsAlerts(String message) {
        String currentText = smsAlerts.getText().toString();
        if (currentText.equals("No item with minimum stock availability")) {
            smsAlerts.setText(message);
        } else {
            smsAlerts.setText(currentText + "\n" + message);
        }
        smsAlerts.setVisibility(TextView.VISIBLE);
    }

    // APP START LOAD PREVIOUS SMS ALERT MESS
    @SuppressLint("SetTextI18n")
    private void loadSMSAlerts() {
        smsAlerts.setText("No item with minimum stock availability");
        smsAlerts.setVisibility(TextView.GONE);
    }

    @Override
    // WHEN USER IS ON A DIFFER ACTIVITY AND COME BACK TO THE PREVIOUS ONE
    protected void onResume() {
        super.onResume();
        setLoadProducts("");
    }

    // CHECK SMS ALERT IS GRANTED OR DENIED
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMIT_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permit granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permit denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

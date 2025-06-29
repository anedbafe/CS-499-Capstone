package com.zybooks.inventoryapp;

// IMPORTS
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.zybooks.inventoryapp.model.Product;
import com.zybooks.inventoryapp.network.ApiService;
import com.zybooks.inventoryapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // UI ELEMENTS
    BottomNavigationView bottomNavigation;
    ListView productListView;
    EditText searchProduct;
    TextView smsAlerts;
    ArrayList<String> productList;
    ArrayAdapter<String> adapter;

    // INITIALIZE MAIN ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // REQUEST SMS PERMISSION
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }

        // LINK UI ELEMENTS
        productListView = findViewById(R.id.productListView);
        searchProduct = findViewById(R.id.searchProduct);
        smsAlerts = findViewById(R.id.smsAlerts);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        productList = new ArrayList<>();

        // SET TEXT WATCHER FOR SEARCH FILTER
        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProducts(s.toString());
            }
        });

        // HANDLE BOTTOM NAVIGATION EVENTS
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

        // INITIALIZE SMS ALERT TEXT
        loadSmsAlerts();
    }

    // LOAD PRODUCTS FROM API AND APPLY SEARCH FILTER
    private void loadProducts(String filter) {
        productList.clear();

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Product product : response.body()) {
                        String name = product.getProduct_name();
                        int quantity = product.getProduct_quantity();
                        String location = product.getProduct_location();

                        // APPLY SEARCH FILTER
                        if (filter.isEmpty() || name.toLowerCase().contains(filter.toLowerCase())) {
                            String info = name + " - Qty: " + quantity + " - Location: " + location;
                            productList.add(info);
                        }

                        // LOW STOCK ALERT
                        if (quantity <= 1) {
                            updateSmsAlerts("Low stock: " + name + " (" + quantity + " available)");
                        }
                    }

                    adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, productList);
                    productListView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load products: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // UPDATE SMS ALERT TEXT FOR LOW STOCK PRODUCTS
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

    // INITIALIZE ALERT TEXT AS DEFAULT MESSAGE
    @SuppressLint("SetTextI18n")
    private void loadSmsAlerts() {
        smsAlerts.setText("No item with minimum stock availability");
        smsAlerts.setVisibility(TextView.GONE);
    }

    // REFRESH PRODUCT LIST WHEN RETURNING TO ACTIVITY
    @Override
    protected void onResume() {
        super.onResume();
        loadProducts("");
    }

    // HANDLE RESULT OF PERMISSION REQUEST
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permit granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS denied authorization", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

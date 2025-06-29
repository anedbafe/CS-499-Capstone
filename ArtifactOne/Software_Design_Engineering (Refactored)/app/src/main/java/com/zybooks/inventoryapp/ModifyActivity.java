package com.zybooks.inventoryapp;

// IMPORTS
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zybooks.inventoryapp.model.Product;
import com.zybooks.inventoryapp.network.ApiService;
import com.zybooks.inventoryapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyActivity extends AppCompatActivity {

    // UI ELEMENTS
    EditText searchProduct, productName, productQuantity, productLocation;
    Button btnAddProduct, btnUpdateProduct, btnDeleteProduct, btnCleanForm;
    ListView productListView;
    ArrayList<Product> productList;
    ArrayAdapter<Product> adapter;
    String selectedProductId = null;
    BottomNavigationView bottomNavigation;

    // ON CREATE METHOD TO INITIALIZE THE ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        // ENABLE BACK BUTTON IN ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // LINK UI ELEMENTS TO LAYOUT
        searchProduct = findViewById(R.id.searchProduct);
        productName = findViewById(R.id.productName);
        productQuantity = findViewById(R.id.productQuantity);
        productLocation = findViewById(R.id.productLocation);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnCleanForm = findViewById(R.id.btnCleanForm);
        productListView = findViewById(R.id.productListView);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        productList = new ArrayList<>();

        // LOAD ALL PRODUCTS INITIALLY
        loadProducts("");

        // FILTER PRODUCTS WHILE TYPING
        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProducts(s.toString());
            }
        });

        // ADD PRODUCT BUTTON CLICK
        btnAddProduct.setOnClickListener(view -> {
            String name = productName.getText().toString().trim();
            String qtyStr = productQuantity.getText().toString().trim();
            String location = productLocation.getText().toString().trim();

            // VALIDATE INPUT FIELDS
            if (name.isEmpty() || qtyStr.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Complete all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(qtyStr);
            Product newProduct = new Product(name, quantity, location);

            // SEND API CALL TO ADD PRODUCT
            ApiService api = RetrofitClient.getApiService();
            api.addProduct(newProduct).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyActivity.this, "Product added!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        loadProducts("");
                    } else {
                        Toast.makeText(ModifyActivity.this, "Add failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ModifyActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // UPDATE PRODUCT BUTTON CLICK
        btnUpdateProduct.setOnClickListener(view -> {
            if (selectedProductId == null) {
                Toast.makeText(this, "Select a product!", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = productName.getText().toString().trim();
            String qtyStr = productQuantity.getText().toString().trim();
            String location = productLocation.getText().toString().trim();

            // VALIDATE INPUT FIELDS
            if (name.isEmpty() || qtyStr.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Complete all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(qtyStr);
            Product updatedProduct = new Product(name, quantity, location);

            // SEND API CALL TO UPDATE PRODUCT
            ApiService api = RetrofitClient.getApiService();
            api.updateProduct(selectedProductId, updatedProduct).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyActivity.this, "Product updated!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        loadProducts("");
                    } else {
                        Toast.makeText(ModifyActivity.this, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ModifyActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // DELETE PRODUCT BUTTON CLICK
        btnDeleteProduct.setOnClickListener(view -> {
            if (selectedProductId == null) {
                Toast.makeText(this, "Select a product!", Toast.LENGTH_SHORT).show();
                return;
            }

            // SEND API CALL TO DELETE PRODUCT
            ApiService api = RetrofitClient.getApiService();
            api.deleteProduct(selectedProductId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyActivity.this, "Product deleted!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        loadProducts("");
                    } else {
                        Toast.makeText(ModifyActivity.this, "Delete failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ModifyActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // CLEAN FORM BUTTON CLICK
        btnCleanForm.setOnClickListener(view -> clearFields());

        // LIST ITEM CLICK TO SELECT PRODUCT
        productListView.setOnItemClickListener((parent, view, position, id) -> {
            Product p = productList.get(position);
            selectedProductId = p.get_id();
            android.util.Log.d("DEBUG_ID", "Selected product _id: " + selectedProductId);

            productName.setText(p.getProduct_name());
            productQuantity.setText(String.valueOf(p.getProduct_quantity()));
            productLocation.setText(p.getProduct_location());
        });

        // BOTTOM NAVIGATION ITEM SELECTION HANDLER
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ModifyActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_modify) {
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(ModifyActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    // LOAD PRODUCTS FROM API AND FILTER LOCALLY
    private void loadProducts(String filter) {
        ApiService api = RetrofitClient.getApiService();
        api.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> all = response.body();
                    productList.clear();

                    for (Product p : all) {
                        if (p.get_id() != null &&
                                (filter.isEmpty() || p.getProduct_name().toLowerCase().contains(filter.toLowerCase()))) {
                            productList.add(p);
                        }
                    }

                    adapter = new ArrayAdapter<>(
                            ModifyActivity.this,
                            android.R.layout.simple_list_item_1,
                            productList
                    );
                    productListView.setAdapter(adapter);
                } else {
                    Toast.makeText(ModifyActivity.this, "Load failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ModifyActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // CLEAR FORM FIELDS
    private void clearFields() {
        productName.setText("");
        productQuantity.setText("");
        productLocation.setText("");
        selectedProductId = null;
    }

    // REFRESH PRODUCTS WHEN RESUMING ACTIVITY
    @Override
    protected void onResume() {
        super.onResume();
        loadProducts("");
    }

    // HANDLE BACK BUTTON FROM ACTION BAR
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

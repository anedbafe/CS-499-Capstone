package com.zybooks.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
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
import com.zybooks.inventoryapp.database.DBHelper;
import java.util.ArrayList;

public class ModifyActivity extends AppCompatActivity {

    EditText searchProduct, productName, productQuantity, productLocation;
    Button btnAddProduct, btnUpdateProduct, btnDeleteProduct, btnCleanForm;
    ListView productListView;
    BottomNavigationView bottomNavigation;
    DBHelper dbHelper;
    ArrayList<String> productList;
    ArrayAdapter<String> adapter;
    String selectedProductId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // METHOD CALLS
        setElements();
        setListeners();
        loadProducts("");
    }

    //  CALLS .XML ELEMENTS FROM LAYOUT
    private void setElements() {
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
        dbHelper = new DBHelper(this);
    }

    // FILTERING PRODUCT IN REAL-TIME
    private void setListeners() {
        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProducts(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // ASSIGNING BUTTON INSTANCE
        btnCleanForm.setOnClickListener(view -> clearFields());
        btnAddProduct.setOnClickListener(view -> addProduct());
        btnUpdateProduct.setOnClickListener(view -> updateProduct());
        btnDeleteProduct.setOnClickListener(view -> deleteProduct());

        // PRODUCT LIST VIEW LISTENER
        productListView.setOnItemClickListener((parent, view, position, id) -> selectProduct(position));

        // MENU NAV BAR
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

    // ADDING PROD TO DB
    private void addProduct() {
        String name = productName.getText().toString().trim();
        String quantity = productQuantity.getText().toString().trim();
        String location = productLocation.getText().toString().trim();

        // CHECK FOR ALL FIELD WHEN ADDING A PRODUCT
        if (name.isEmpty() || quantity.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Complete all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ADDING PRODUCT TO DB WITH CONDITION
        boolean insertSuccess = dbHelper.insertProduct(name, Integer.parseInt(quantity), location);
        if (insertSuccess) {
            Toast.makeText(this, "Product saved!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadProducts("");
        } else {
            Toast.makeText(this, "Check product information!", Toast.LENGTH_SHORT).show();
        }
    }

    // UPDATE PRODUCT METHOD
    private void updateProduct() {
        if (selectedProductId == null || selectedProductId.isEmpty()) {
            Toast.makeText(this, "Select a product!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ASSIGN PRODUCT INFO TO VARS FROM USER INPUT ELIMINATE WHITESPACES
        String name = productName.getText().toString().trim();
        String quantityStr = productQuantity.getText().toString().trim();
        String location = productLocation.getText().toString().trim();

        // CHECK FOR ANY EMPTY FIELD
        if (name.isEmpty() || quantityStr.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Complete all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // CHECK STRING CONVERSION
        try {
            int productId = Integer.parseInt(selectedProductId);
            int quantity = Integer.parseInt(quantityStr);

            // CONTINUE WITH IF/ELSE IF CONVERSION IS TRUE
            boolean updateSuccess = dbHelper.updateProduct(productId, name, quantity, location);
            if (updateSuccess) {
                Toast.makeText(this, "Product modified!", Toast.LENGTH_SHORT).show();
                clearFields();
                loadProducts("");
            } else {
                Toast.makeText(this, "Check product information!", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid data type!", Toast.LENGTH_SHORT).show();
        }
    }

    // DELETE PRODUCT WITH IF/ELSE CHECK
    private void deleteProduct() {
        if (selectedProductId == null) {
            Toast.makeText(this, "Select a product!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean deleteSuccess = dbHelper.deleteProduct(Integer.parseInt(selectedProductId));
        if (deleteSuccess) {
            Toast.makeText(this, "Product deleted!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadProducts("");
        } else {
            Toast.makeText(this, "Check product information!", Toast.LENGTH_SHORT).show();
        }
    }

    // SELECT A PRODUCT OPTION FOR UPDATING PRODUCT INFO
    private void selectProduct(int position) {
        String selectedItem = productList.get(position);
        String[] parts = selectedItem.split(" - ");

        if (parts.length >= 4) {
            selectedProductId = parts[0].trim();
            productName.setText(parts[1].trim());
            productQuantity.setText(parts[2].trim());
            productLocation.setText(parts[3].trim());
        } else {
            Toast.makeText(this, "Error! Select a product.", Toast.LENGTH_SHORT).show();
        }
    }

    // LOAD PRODUCT INFO
    private void loadProducts(String filter) {
        productList = new ArrayList<>();
        Cursor cursor = dbHelper.readProducts();

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String quantity = cursor.getString(2);
                String location = cursor.getString(3);

                String productInfo = id + " - " + name + " - " + quantity + " - " + location;

                if (filter.isEmpty() || name.toLowerCase().contains(filter.toLowerCase())) {
                    productList.add(productInfo);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    // CLEAR FIELD BUTTON WHEN PRESS
    private void clearFields() {
        productName.setText("");
        productQuantity.setText("");
        productLocation.setText("");
        selectedProductId = null;
    }

    // LOAD DATA WHEN BACK TO SCREEN
    @Override
    protected void onResume() {
        super.onResume();
        loadProducts("");
    }

    // METHOD FOR DATA SELECTED
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

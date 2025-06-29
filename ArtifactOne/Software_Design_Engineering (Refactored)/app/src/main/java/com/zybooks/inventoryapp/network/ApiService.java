package com.zybooks.inventoryapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Body;
import retrofit2.http.Path;

import com.zybooks.inventoryapp.model.Product;
import com.zybooks.inventoryapp.model.User;

public interface ApiService {

    // ==== USER AUTHENTICATION ENDPOINTS ====

    // REGISTER NEW USER
    @POST("/api/users/register")
    Call<Void> registerUser(@Body User user);

    // LOGIN EXISTING USER
    @POST("/api/users/login")
    Call<User> loginUser(@Body User user);

    // ==== PRODUCT CRUD ENDPOINTS ====

    // GET ALL PRODUCTS (USED TO DISPLAY AND SEARCH LOCALLY)
    @GET("api/products")
    Call<List<Product>> getProducts();

    // ADD A NEW PRODUCT
    @POST("api/products")
    Call<Product> addProduct(@Body Product product);

    // UPDATE A PRODUCT BY ID
    @PUT("api/products/{id}")
    Call<Product> updateProduct(@Path("id") String id, @Body Product product);

    // DELETE A PRODUCT BY ID
    @DELETE("api/products/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}

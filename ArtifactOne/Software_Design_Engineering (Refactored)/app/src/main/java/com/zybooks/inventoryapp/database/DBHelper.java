package com.zybooks.inventoryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // dbName and version in use
    private static final String DBName = "inventory_app";
    private static final int DBVersion = 1;

    // tables implementation for user and inventory

    // user private vars
    private static final String TABLE_USERS = "users"; // table name
    private static final String COLUMN_USERID = "id"; // id for users
    private static final String COLUMN_NAME = "username"; // added to identify user by name
    private static final String COLUMN_EMAIL = "email"; // will be use to login instead of name
    private static final String COLUMN_PASSWORD = "password"; // user password

    // products private vars
    private static final String TABLE_PRODUCTS = "products"; // table name
    private static final String COLUMN_PRODUCT_ID = "product_id"; // id for products
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    private static final String COLUMN_PRODUCT_LOCATION = "product_location";

    // db constructor
    public DBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    // db creation method
    @Override
    public void onCreate(SQLiteDatabase db) {
        // user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // product table
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                + COLUMN_PRODUCT_LOCATION + " TEXT)";
        db.execSQL(CREATE_PRODUCT_TABLE);
    }

    // bd upgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // any change in the db structure this block must be implemented
    }

    // create user
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // to write into the db
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long sendToDB = db.insert(TABLE_USERS, null, values);
        return sendToDB != -1;
    }

    // method to check if user exists in the db when creating
    public boolean userEmailInDB(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "
                        + COLUMN_EMAIL + "=?",
                new String[]{email});

        // check cursor size
        boolean inDB = cursor.getCount() > 0;
        cursor.close();
        return inDB;
    }

    public boolean loginUserCheck(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "
                        + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password});

        /* email and pass in db when attempting to login user would be authenticated */
        boolean inDB = cursor.getCount() > 0;
        cursor.close();
        return inDB;
    }

    // create product
    public boolean insertProduct(String productName, Integer productQuantity, String productLocation) {
        SQLiteDatabase db = this.getWritableDatabase(); // to write into the db
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(COLUMN_PRODUCT_LOCATION, productLocation);

        long sendToDB = db.insert(TABLE_PRODUCTS, null, values);
        return sendToDB != -1;
    }

    // read all products from the db
    public Cursor readProducts() {
        SQLiteDatabase db = this.getReadableDatabase(); // read into the db
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // update product in db
    public boolean updateProduct(int id, String name, int quantity, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("product_name", name);
        values.put("product_quantity", quantity);
        values.put("product_location", location);

        int rowsUpdated = db.update("products", values, "product_id=?", new String[]{String.valueOf(id)});
        return rowsUpdated > 0;  // return true if there is any change in any of the field
    }


    // delete a product from db
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}

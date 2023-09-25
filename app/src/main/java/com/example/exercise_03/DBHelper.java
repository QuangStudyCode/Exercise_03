package com.example.exercise_03;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "product.db";
    public static final int VERSION = 2;

    public static final String TABLE_NAME = "products";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_TITLE = "title";
    public static final String PRODUCT_DES = "description";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DISCOUNT = "discountPercentage";
    public static final String PRODUCT_RATING = "rating";
    public static final String PRODUCT_STOCK = "stock";
    public static final String PRODUCT_BRAND = "brand";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_THUMBNAIL = "thumbnail";
    public static final String PRODUCT_IMAGES = "images";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME +
                "(" + PRODUCT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                PRODUCT_TITLE + " TEXT NOT NULL," +
                PRODUCT_DES + " TEXT NOT NULL," +
                PRODUCT_PRICE + " TEXT NOT NULL," +
                PRODUCT_DISCOUNT + " TEXT NOT NULL," +
                PRODUCT_RATING + " TEXT NOT NULL," +
                PRODUCT_STOCK + " TEXT NOT NULL," +
                PRODUCT_BRAND + " TEXT NOT NULL," +
                PRODUCT_CATEGORY + " TEXT NOT NULL," +
                PRODUCT_THUMBNAIL + " TEXT NOT NULL," +
                PRODUCT_IMAGES + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addProduct(Product product) {
        if (product != null) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(PRODUCT_TITLE, product.getTitle());
            contentValues.put(PRODUCT_DES, product.getDescription());
            contentValues.put(PRODUCT_PRICE, product.getPrice());
            contentValues.put(PRODUCT_DISCOUNT, product.getDiscountPercentage());
            contentValues.put(PRODUCT_RATING, product.getRating());
            contentValues.put(PRODUCT_STOCK, product.getStock());
            contentValues.put(PRODUCT_BRAND, product.getBrand());
            contentValues.put(PRODUCT_CATEGORY, product.getCategory());
            contentValues.put(PRODUCT_THUMBNAIL, product.getThumbnail());
            Gson gson = new Gson();

//          lưu trữ thông tin của kiểu dl trong quá trình chuyển đổi dữ liệu sang json , nằm trong
//            lớp json
//            cung cấp kiểu dữ liệu cho json trong quá trình chuyển đổi.

            Type typeToken = new TypeToken<List<String>>() {
            }.getType();

            String data = gson.toJson(product.getImages(), typeToken);
            contentValues.put(PRODUCT_IMAGES, data);

            long response = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();

            if (response > -1) {
                return false;
            }

            return true;
        }
        return false;
    }

    public boolean updateProduct(int productId, Product product) {
        if (productId > 0 && product != null) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            String whereClause = PRODUCT_ID + " =?";
            String[] whereArgs = {productId + ""};

            ContentValues contentValues = new ContentValues();

            contentValues.put(PRODUCT_TITLE, product.getTitle());
            contentValues.put(PRODUCT_DES, product.getDescription());
            contentValues.put(PRODUCT_PRICE, product.getPrice());
            contentValues.put(PRODUCT_DISCOUNT, product.getDiscountPercentage());
            contentValues.put(PRODUCT_RATING, product.getRating());
            contentValues.put(PRODUCT_STOCK, product.getStock());
            contentValues.put(PRODUCT_BRAND, product.getBrand());
            contentValues.put(PRODUCT_CATEGORY, product.getCategory());
            contentValues.put(PRODUCT_THUMBNAIL, product.getThumbnail());

            Gson gson = new Gson();
            Type typeToken = new TypeToken<List<String>>() {
            }.getType();

            String data = gson.toJson(product.getImages(), typeToken);
            contentValues.put(PRODUCT_IMAGES, data);

            int response = sqLiteDatabase.update(TABLE_NAME, contentValues, whereClause, whereArgs);
            sqLiteDatabase.close();
            if (response > 0) {
                return true;
            }
            return false;
        }

        return false;
    }

    public boolean deleteProduct(int productID) {
        if (productID > 0) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            String whereClause = PRODUCT_ID + " =?";
            String[] whereArgs = {productID + ""};
            sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
            sqLiteDatabase.close();
            return true;
        }
        return false;
    }

    @SuppressLint("Range")
    public List<Product> getAllProduct() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndex(PRODUCT_ID)));
            product.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_TITLE)));
            product.setDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DES)));

            product.setPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_PRICE)));
            product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndex(PRODUCT_DISCOUNT)));
            product.setRating(cursor.getDouble(cursor.getColumnIndex(PRODUCT_RATING)));

            product.setStock(cursor.getInt(cursor.getColumnIndex(PRODUCT_STOCK)));
            product.setBrand(cursor.getString(cursor.getColumnIndex(PRODUCT_BRAND)));
            product.setCategory(cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY)));
            product.setThumbnail(cursor.getString(cursor.getColumnIndex(PRODUCT_THUMBNAIL)));

            Gson gson = new Gson();
//           cung cấp kiểu dữ liệu json trong quá trình chuyển đổi
            Type type = new TypeToken<List<String>>() {
            }.getType();

            List<String> stringList = gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGES)), type);
            product.setImages(stringList);

            productList.add(product);
        }
        sqLiteDatabase.close();
        return productList;
    }

    @SuppressLint("Range")
    public List<Product> getProductsByTitle(String titleSearch) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Product> productList = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_TITLE + " = ?";
//        placeholer nghĩa là truyền vào một giá trị nó thay cho ? bên trên câu lệnh.

        String[] selectionArgs = new String[]{titleSearch};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndex(PRODUCT_ID)));
            product.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_TITLE)));
            product.setDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DES)));

            product.setPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_PRICE)));
            product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndex(PRODUCT_DISCOUNT)));
            product.setRating(cursor.getDouble(cursor.getColumnIndex(PRODUCT_RATING)));

            product.setStock(cursor.getInt(cursor.getColumnIndex(PRODUCT_STOCK)));
            product.setBrand(cursor.getString(cursor.getColumnIndex(PRODUCT_BRAND)));
            product.setCategory(cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY)));
            product.setThumbnail(cursor.getString(cursor.getColumnIndex(PRODUCT_THUMBNAIL)));

            Gson gson = new Gson();
//           cung cấp kiểu dữ liệu json trong quá trình chuyển đổi
            Type type = new TypeToken<List<String>>() {
            }.getType();

            List<String> stringList = gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGES)), type);
            product.setImages(stringList);
            productList.add(product);
        }
        return productList;
    }

    @SuppressLint("Range")
    public List<Product> getProductByBrand(String nameBrand) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Product> productListFilteredBrand = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_BRAND + " =?";
        String[] selectionArgs = new String[]{nameBrand};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);

        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndex(PRODUCT_ID)));
            product.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_TITLE)));
            product.setDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DES)));

            product.setPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_PRICE)));
            product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndex(PRODUCT_DISCOUNT)));
            product.setRating(cursor.getDouble(cursor.getColumnIndex(PRODUCT_RATING)));

            product.setStock(cursor.getInt(cursor.getColumnIndex(PRODUCT_STOCK)));
            product.setBrand(cursor.getString(cursor.getColumnIndex(PRODUCT_BRAND)));
            product.setCategory(cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY)));
            product.setThumbnail(cursor.getString(cursor.getColumnIndex(PRODUCT_THUMBNAIL)));

            Gson gson = new Gson();
//           cung cấp kiểu dữ liệu json trong quá trình chuyển đổi
            Type type = new TypeToken<List<String>>() {
            }.getType();

            List<String> stringList = gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGES)), type);
            product.setImages(stringList);
            productListFilteredBrand.add(product);
        }
        Log.d("TAG", "getProductByBrand: " + productListFilteredBrand.toString());
        return productListFilteredBrand;
    }
}

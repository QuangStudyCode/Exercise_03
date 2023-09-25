package com.example.exercise_03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exercise_03.Adapter.AdapterProduct;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IItemClickListener {

    private DBHelper dbHelper;

    private RecyclerView recyclerView, rcIphone;

    private List<Product> productList;
    private List<Product> productListIphone;

    private AdapterProduct adapterProduct;

    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        rcIphone = findViewById(R.id.rcIphone);
        initDataForDB();

        initData();
        initView();
        srearchProduct();
        initViewBrandIphone();
    }

    private void initViewBrandIphone() {
        dbHelper = new DBHelper(this);

        // Lấy danh sách sản phẩm iPhone từ database
        List<Product> productsIphone = dbHelper.getProductByBrand("Iphone");

        // Tạo Adapter và LinearLayoutManager
        AdapterProduct adapterProductPhone = new AdapterProduct(this, productsIphone);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Thiết lập LayoutManager và Adapter cho RecyclerView của iPhone
        rcIphone.setLayoutManager(linearLayoutManager);
        rcIphone.setAdapter(adapterProductPhone);

        Log.d("TAG", "initViewBrandIphone: " + productsIphone.toString());
    }

    private void srearchProduct() {
        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchByTitle(s.toString());
            }
        });
    }

    private void searchByTitle(String title) {
        List<Product> productListSearch = new ArrayList<>();
        for (Product ProductSelected : productList) {
            if (ProductSelected.getTitle().toLowerCase().contains(title.toLowerCase())) {
                productListSearch.add(ProductSelected);
            }
        }
        adapterProduct.updateProductList(productListSearch);
    }

    private void initView() {
        if (productList != null) { // Kiểm tra productList có null hay không
            adapterProduct = new AdapterProduct(this, productList);
            adapterProduct.setiItemClickListener(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapterProduct);
        } else {
            Log.e("TAG", "check");
        }
    }

    private void initData() {
        dbHelper = new DBHelper(this);
        productList = new ArrayList<>();
        productList.clear();
        List<Product> products = dbHelper.getAllProduct();
        productList.addAll(products);
        Log.d("TAG", "initData: " + productList.size());
//        Log.d("TAG", "initData: " + productList.get(0).getThumbnail());
    }

    private void initDataForDB() {
        dbHelper = new DBHelper(this);
        Product productModel = new Product();
        productModel.setTitle("IPhone 15 Promax");
        productModel.setDescription("Iphone is amazing!");
        productModel.setPrice(988);
        productModel.setDiscountPercentage(15.0);
        productModel.setRating(4.5);
        productModel.setStock(94);
        productModel.setBrand("Iphone");
        productModel.setCategory("smartphones");
        productModel.setThumbnail("https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-7inch-naturaltitanium?wid=5120&hei=2880&fmt=p-jpg&qlt=80&.v=1692845702708");

        List<String> images = new ArrayList<>();
        images.add("https://i.dummyjson.com/data/products/1/1.jpg");
        images.add("https://i.dummyjson.com/data/products/1/2.jpg");
        images.add("https://i.dummyjson.com/data/products/1/3.jpg");
        images.add("https://i.dummyjson.com/data/products/1/4.jpg");
        productModel.setImages(images);

        dbHelper.addProduct(productModel);

//        int i = 10;
//        while (i < 21) {
//            dbHelper.deleteProduct(i);
//            i++;
//        }

//        dbHelper.updateProduct(3, productModel);
//        Log.d("TAG", "initData: " + dbHelper.getAllProduct().get(1));
//        Log.d("TAG", "initData: " + productList.size());
    }

    @Override
    public void deleteProduct(int pos) {
        dbHelper = new DBHelper(this);
        Product product = productList.get(pos);

//        delete from db
        dbHelper.deleteProduct(product.getId());

//        delete from list
        productList.remove(pos);
        adapterProduct.notifyItemRemoved(pos);
        Toast.makeText(this, "Delete success!", Toast.LENGTH_SHORT).show();
    }
}
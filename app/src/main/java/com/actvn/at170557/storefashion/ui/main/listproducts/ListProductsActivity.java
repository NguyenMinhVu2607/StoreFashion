package com.actvn.at170557.storefashion.ui.main.listproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityListProductsBinding;
import com.actvn.at170557.storefashion.ui.detailproduct.DetailProductActivity;
import com.actvn.at170557.storefashion.ui.main.home.adapter.PopularAdapter;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListProductsActivity extends AppCompatActivity {
    private ActivityListProductsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = getApplicationContext();
        hideNavigationBar();
        String cateID = getIntent().getStringExtra("CATE");
        String BrandID = getIntent().getStringExtra("Brand");
        Log.d("ListProductsActivity", "cateID: " + cateID);
        if (cateID != null) {
            loadProductsCateFromFirestore(cateID);
            binding.tvCate.setText(cateID);

        }
        if (BrandID != null) {
            loadProductsFromFirestore(BrandID);
            binding.tvCate.setText(BrandID);

        }


        binding.imgBack.setOnClickListener(v -> finish());
    }

    private void loadProductsFromFirestore(String BrandID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");

        productsRef.whereEqualTo("brand", BrandID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ProductItem> itemList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductItem product = document.toObject(ProductItem.class);
                                product.setId(document.getId());

                                itemList.add(product);

                                // Ghi dữ liệu vào log
                                Log.d("ProductItem", "Brand: " + product.getBrand());
                                Log.d("ProductItem", "Description: " + product.getDescription());
                                Log.d("ProductItem", "Name: " + product.getName());
                                Log.d("ProductItem", "Price: " + product.getPrice());
                                Log.d("ProductItem", "Sizes: " + (product.getSize() != null ? product.getSize().toString() : "No sizes available"));
                            }
                            PopularAdapter popularAdapter = new PopularAdapter(context, itemList, item -> {
                                Intent intent = new Intent(context, DetailProductActivity.class);
                                intent.putExtra("PRODUCT_ID", item.getId());
                                startActivity(intent);
                            });
                            binding.recListCate.setLayoutManager(new GridLayoutManager(context, 2));
                            binding.recListCate.setAdapter(popularAdapter);
                        } else {
                            Log.d("ProductItem", "Lỗi khi lấy tài liệu: ", task.getException());
                        }
                    }
                });
    }

    private void loadProductsCateFromFirestore(String cateID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");

        productsRef.whereEqualTo("cate", cateID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ProductItem> itemList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductItem product = document.toObject(ProductItem.class);
                                product.setId(document.getId());

                                itemList.add(product);

                                Log.d("ProductItem", "Brand: " + product.getBrand());
                                Log.d("ProductItem", "Description: " + product.getDescription());
                                Log.d("ProductItem", "Name: " + product.getName());
                                Log.d("ProductItem", "Price: " + product.getPrice());
                                Log.d("ProductItem", "Sizes: " + (product.getSize() != null ? product.getSize().toString() : "No sizes available"));
                            }

                            // Đặt adapter sau khi lấy dữ liệu
                            PopularAdapter popularAdapter = new PopularAdapter(context, itemList, item -> {
                                Intent intent = new Intent(context, DetailProductActivity.class);
                                intent.putExtra("PRODUCT_ID", item.getId());
                                startActivity(intent);
                            });
                            binding.recListCate.setLayoutManager(new GridLayoutManager(context, 2));
                            binding.recListCate.setAdapter(popularAdapter);
                        } else {
                            // Xử lý lỗi
                            Log.d("ProductItem", "Lỗi khi lấy tài liệu: ", task.getException());
                        }
                    }
                });
    }

    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
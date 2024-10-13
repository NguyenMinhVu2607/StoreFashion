package com.actvn.at170557.storefashion.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivitySearchBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>(); // Danh sách sản phẩm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();

        recyclerView = findViewById(R.id.recyclerViewResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        binding.imgBack.setOnClickListener(v -> finish());

        // Tạo TextWatcher cho ô tìm kiếm
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String query = charSequence.toString();
                if (!query.isEmpty()) {
                    searchProducts(query);
                } else {
                    productList.clear();
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // Hàm tìm kiếm từ Firebase Firestore
    private void searchProducts(String query) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("SearchActivity", "Searching for products with query: " + query); // Log the query being searched

        // Tạo truy vấn tìm kiếm sản phẩm theo tên hoặc thương hiệu
        db.collection("Products")
                .orderBy("name") // Sắp xếp theo tên sản phẩm
                .startAt(query)
                .endAt(query + "\uf8ff") // Tìm kiếm theo tên bắt đầu từ query
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("SearchActivity", "Query successful. Found " + queryDocumentSnapshots.size() + " products."); // Log how many products are found
                    productList.clear(); // Xóa danh sách cũ
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Product product = snapshot.toObject(Product.class);
                        Log.d("SearchActivity", "Found product: " + product.getName()); // Log product name
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged(); // Cập nhật lại adapter với sản phẩm tìm được
                })
                .addOnFailureListener(e -> {
                    Log.e("SearchActivity", "Error fetching products: " + e.getMessage()); // Log error if any
                    Toast.makeText(SearchActivity.this, "Error fetching products", Toast.LENGTH_SHORT).show();
                });
    }



    // Ẩn thanh điều hướng
    protected void hideNavigationBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}

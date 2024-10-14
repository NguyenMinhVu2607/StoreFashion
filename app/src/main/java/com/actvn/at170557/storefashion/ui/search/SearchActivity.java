package com.actvn.at170557.storefashion.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.actvn.at170557.storefashion.ui.detailproduct.DetailProductActivity;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener{
    private ActivitySearchBinding binding;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();

        recyclerView = findViewById(R.id.recyclerViewResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this,productList,this);
        recyclerView.setAdapter(productAdapter);

        binding.imgBack.setOnClickListener(v -> finish());

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
    private void searchProducts(String query) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Product product = snapshot.toObject(Product.class);
                        product.setId(snapshot.getId());
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                });
    }

    protected void hideNavigationBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }


    @Override
    public void onItemClick(Product item) {
        Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
        intent.putExtra("PRODUCT_ID", item.getId());
        startActivity(intent);
    }
}

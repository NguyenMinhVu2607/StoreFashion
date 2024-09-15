package com.actvn.at170557.storefashion.ui.detailproduct;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityDetailProductBinding;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailProductActivity extends AppCompatActivity {
    private ActivityDetailProductBinding binding;
    private TextView textViewName, textViewBrand, textViewPrice, textViewDescription;
    private int currentValue = 1;
    private final int MIN_VALUE = 1;
    private final int MAX_VALUE = 10; // Đặt giá trị tối đa bạn muốn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();
        textViewName = findViewById(R.id.textViewName);
        textViewBrand = findViewById(R.id.textViewBrand);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewDescription = findViewById(R.id.textViewDescription);
        String productId = getIntent().getStringExtra("PRODUCT_ID");
        Log.d("DetailProductActivity", "productId: " + productId);
        if (productId != null) {
            loadProductDetails(productId);
        }
        binding.imgBack.setOnClickListener(v -> finish());
        updateValueDisplay();

        binding.imageViewMinus.setOnClickListener(v -> {
            if (currentValue > MIN_VALUE) {
                currentValue--;
                updateValueDisplay();
            }
        });

        binding.imageViewPlus.setOnClickListener(v -> {
            if (currentValue < MAX_VALUE) {
                currentValue++;
                updateValueDisplay();
            }
        });
    }

    private void loadProductDetails(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference productRef = db.collection("Products").document(productId);

        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ProductItem product = document.toObject(ProductItem.class);
                    if (product != null) {
                        textViewName.setText(product.getName());
                        textViewBrand.setText("Brand: " + product.getBrand());
                        textViewPrice.setText("$" + product.getPrice());
                        String description = product.getDescription();

                        // Thay thế "\\n" nếu bạn lưu trữ nó như một chuỗi thoát
                        description = description.replace("\\n", "\n");

                        // Đặt văn bản cho TextView
                        textViewDescription.setText(description);
                        Log.d("DetailProductActivity", "getDescription: " + product.getDescription());

                    }
                }
            } else {
                Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateValueDisplay() {
        binding.textViewValue.setText(String.valueOf(currentValue));
    }

    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
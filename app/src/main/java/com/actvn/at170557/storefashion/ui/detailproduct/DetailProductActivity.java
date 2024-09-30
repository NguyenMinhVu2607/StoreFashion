package com.actvn.at170557.storefashion.ui.detailproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DetailProductActivity extends AppCompatActivity {
    private ActivityDetailProductBinding binding;
    private TextView textViewName, textViewBrand, textViewPrice, textViewDescription;
    private int currentValue = 1;
    private final int MIN_VALUE = 1;
    private final int MAX_VALUE = 10; // Đặt giá trị tối đa bạn muốn
    private String userId; // ID của người dùng hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy userId từ FirebaseAuth

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
        checkIfFavorite(productId);

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
        // Tạo một phương thức để đổi nền khi TextView được click
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đặt tất cả TextView về trạng thái nền mặc định
                binding.textViewS.setBackgroundResource(R.drawable.circular_background);
                binding.textViewM.setBackgroundResource(R.drawable.circular_background);
                binding.textViewL.setBackgroundResource(R.drawable.circular_background);
                binding.textViewXL.setBackgroundResource(R.drawable.circular_background);
//                binding.textViewS.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//                binding.textViewM.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//                binding.textViewL.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//                binding.textViewXL.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));


                // Đổi nền của TextView đã được click
                view.setBackgroundResource(R.drawable.circular_background_selected);
            }
        };

// Đặt sự kiện click cho từng TextView
        binding.textViewS.setOnClickListener(onClickListener);
        binding.textViewM.setOnClickListener(onClickListener);
        binding.textViewL.setOnClickListener(onClickListener);
        binding.textViewXL.setOnClickListener(onClickListener);

        binding.imageViewLikeProduct.setOnClickListener(v -> {
            toggleFavoriteStatus(productId);
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


    private void checkIfFavorite(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Lấy danh sách favorites hiện tại
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");
                if (favorites != null && favorites.contains(productId)) {
                    // Đổi icon thành đã yêu thích
                    binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_selectd);
                } else {
                    // Đổi icon thành chưa yêu thích
                    binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_unselectd);
                }
            }
        });
    }

    // Thêm hoặc xóa sản phẩm khỏi danh sách yêu thích
    private void toggleFavoriteStatus(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                if (favorites != null && favorites.contains(productId)) {
                    // Nếu sản phẩm đã trong danh sách yêu thích, xóa nó
                    userRef.update("favorites", FieldValue.arrayRemove(productId))
                            .addOnSuccessListener(aVoid -> {
                                // Cập nhật icon và hiển thị thông báo
                                binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_unselectd);
                                Toast.makeText(getApplicationContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Nếu sản phẩm chưa có, thêm nó vào danh sách yêu thích
                    userRef.update("favorites", FieldValue.arrayUnion(productId))
                            .addOnSuccessListener(aVoid -> {
                                // Cập nhật icon và hiển thị thông báo
                                binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_selectd);
                                Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                            });
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
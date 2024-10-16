package com.actvn.at170557.storefashion.ui.detailproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.net.Uri;
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
import com.actvn.at170557.storefashion.utils.FirebaseStorageHelper;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailProductActivity extends AppCompatActivity {
    private ActivityDetailProductBinding binding;
    private TextView textViewName, textViewBrand, textViewPrice, textViewDescription;
    private int currentValue = 1;
    private final int MIN_VALUE = 1;
    private final int MAX_VALUE = 10;
    private String userId;
    private String selectedSize = "S";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        textViewName = findViewById(R.id.textViewName);
        textViewBrand = findViewById(R.id.textViewBrand);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewDescription = findViewById(R.id.textViewDescription);

        String productId = getIntent().getStringExtra("PRODUCT_ID");
        Log.d("DetailProductActivity", "productId: " + productId);
        String pathIMG = "/img_product/" + productId + ".jpg";

        FirebaseStorageHelper.getImageUri(pathIMG, new FirebaseStorageHelper.OnImageUriCallback() {
            @Override
            public void onImageUriReceived(Uri uri) {
                if (uri != null) {
                    String imageUrl = uri.toString();
                    Log.d("imageUrl", "imageUrl:: " + imageUrl);
                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.bg_load)
                            .error(R.mipmap.ic_launcher)
                            .into(binding.imageViewProduct);
                }
            }
        });

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

        // Size selection listener
        View.OnClickListener onClickListener = view -> {
            resetSizeSelection();
            view.setBackgroundResource(R.drawable.circular_background_selected);

            if (view.getId() == R.id.textViewS) {
                selectedSize = "S";
            } else if (view.getId() == R.id.textViewM) {
                selectedSize = "M";
            } else if (view.getId() == R.id.textViewL) {
                selectedSize = "L";
            } else if (view.getId() == R.id.textViewXL) {
                selectedSize = "XL";
            }

        };

        binding.textViewS.setOnClickListener(onClickListener);
        binding.textViewM.setOnClickListener(onClickListener);
        binding.textViewL.setOnClickListener(onClickListener);
        binding.textViewXL.setOnClickListener(onClickListener);

        binding.imageViewLikeProduct.setOnClickListener(v -> {
            toggleFavoriteStatus(productId);
        });
        binding.AddToCard.setOnClickListener(v -> {
            String productName = textViewName.getText().toString();
            double productPrice = Double.parseDouble(textViewPrice.getText().toString().replace("$", ""));
            String imageUrl = pathIMG;

            addToCart(productId, productName, selectedSize, productPrice, currentValue, imageUrl);
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
                        description = description.replace("\\n", "\n");
                        textViewDescription.setText(description);
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

    private void resetSizeSelection() {
        binding.textViewS.setBackgroundResource(R.drawable.circular_background);
        binding.textViewM.setBackgroundResource(R.drawable.circular_background);
        binding.textViewL.setBackgroundResource(R.drawable.circular_background);
        binding.textViewXL.setBackgroundResource(R.drawable.circular_background);
    }

    private void checkIfFavorite(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");
                if (favorites != null && favorites.contains(productId)) {
                    binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_selectd);
                } else {
                    binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_unselectd);
                }
            }
        });
    }

    private void toggleFavoriteStatus(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                if (favorites != null && favorites.contains(productId)) {
                    userRef.update("favorites", FieldValue.arrayRemove(productId))
                            .addOnSuccessListener(aVoid -> {
                                binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_unselectd);
                                Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    userRef.update("favorites", FieldValue.arrayUnion(productId))
                            .addOnSuccessListener(aVoid -> {
                                binding.imageViewLikeProduct.setImageResource(R.drawable.ic_favourite_selectd);
                                Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    // Add product to cart
    private void addToCart(String productId, String productName, String productSize, double productPrice, int quantity, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cartRef = db.collection("Cart").document(userId);

        cartRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> cartItems = (List<Map<String, Object>>) document.get("items");

                    if (cartItems != null) {
                        boolean isProductFound = false;

                        for (Map<String, Object> item : cartItems) {
                            // Kiểm tra item không null và các trường không null
                            if (item != null && item.get("productId") != null && item.get("size") != null) {
                                if (item.get("productId").equals(productId) && item.get("size").equals(productSize)) {
                                    int currentQuantity = ((Long) item.get("quantity")).intValue(); // Chuyển đổi Long thành int
                                    item.put("quantity", currentQuantity + quantity); // Cộng dồn số lượng
                                    isProductFound = true;
                                    break; // Thoát vòng lặp nếu đã tìm thấy
                                }
                            }
                        }

                        if (!isProductFound) {
                            // Nếu sản phẩm không tồn tại, thêm sản phẩm mới vào giỏ hàng
                            Map<String, Object> newItem = new HashMap<>();
                            newItem.put("productId", productId);
                            newItem.put("name", productName);
                            newItem.put("size", productSize);
                            newItem.put("price", productPrice);
                            newItem.put("quantity", quantity);
                            newItem.put("imageUrl", imageUrl);
                            cartItems.add(newItem); // Thêm sản phẩm mới vào danh sách
                        }

                        // Cập nhật giỏ hàng trong Firestore
                        cartRef.update("items", cartItems)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error adding product", Toast.LENGTH_SHORT).show());
                    } else {
                        // Xử lý trường hợp cartItems là null
                        List<Map<String, Object>> newCartItems = new ArrayList<>();
                        Map<String, Object> newItem = new HashMap<>();
                        newItem.put("productId", productId);
                        newItem.put("name", productName);
                        newItem.put("size", productSize);
                        newItem.put("price", productPrice);
                        newItem.put("quantity", quantity);
                        newItem.put("imageUrl", imageUrl);
                        newCartItems.add(newItem);

                        Map<String, Object> cartData = new HashMap<>();
                        cartData.put("items", newCartItems);

                        cartRef.set(cartData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cart created and product added", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error creating cart", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Trường hợp document không tồn tại
                    List<Map<String, Object>> newCartItems = new ArrayList<>();
                    Map<String, Object> newItem = new HashMap<>();
                    newItem.put("productId", productId);
                    newItem.put("name", productName);
                    newItem.put("size", productSize);
                    newItem.put("price", productPrice);
                    newItem.put("quantity", quantity);
                    newItem.put("imageUrl", imageUrl);
                    newCartItems.add(newItem);

                    Map<String, Object> cartData = new HashMap<>();
                    cartData.put("items", newCartItems);

                    cartRef.set(cartData)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cart created and product added", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Error creating cart", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "Error fetching cart data", Toast.LENGTH_SHORT).show();
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

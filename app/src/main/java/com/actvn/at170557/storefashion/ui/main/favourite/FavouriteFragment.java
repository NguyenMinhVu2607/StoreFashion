package com.actvn.at170557.storefashion.ui.main.favourite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentFavouriteBinding;
import com.actvn.at170557.storefashion.databinding.FragmentSettingsBinding;
import com.actvn.at170557.storefashion.ui.detailproduct.DetailProductActivity;
import com.actvn.at170557.storefashion.ui.main.home.adapter.PopularAdapter;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.actvn.at170557.storefashion.utils.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends BaseFragment {
    private FragmentFavouriteBinding binding; // Use the generated binding class
    private Context context;
    private SharedViewModel sharedViewModel;

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_favourite;
    }

    @Override
    protected ViewBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        return binding;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        // Khởi tạo SharedViewModel từ Activity
//        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//        Log.d("FavouriteFragment", "ViewModel initialized");

        // Quan sát dữ liệu UserID và cập nhật giao diện khi dữ liệu thay đổi
//        sharedViewModel.getUserId().observe(getViewLifecycleOwner(), userId -> {
        // Cập nhật giao diện hoặc lấy dữ liệu yêu thích dựa trên userId
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy userId từ FirebaseAuth
        Log.d("FavouriteFragment", "userId : " + userId);

        loadFavoriteProducts(userId);
//        });
        // Initialize RecyclerView using ViewBinding
        RecyclerView recyclerView = binding.recFavourite;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2)); // 2 columns

    }

    private void loadFavoriteProducts(String userId) {
        // Reference to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("FavouriteFragment", "Loading favorite products for user ID: " + userId);

        // Get the user's favorites list
        db.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> favoriteProductIds = (List<String>) documentSnapshot.get("favorites"); // assuming 'favorites' is an array of product IDs
                        if (favoriteProductIds != null && !favoriteProductIds.isEmpty()) {
                            // Fetch details for each product in the favorites list
                            List<ProductItem> favoriteProducts = new ArrayList<>();
                            for (String productId : favoriteProductIds) {
                                db.collection("Products").document(productId).get()
                                        .addOnSuccessListener(productSnapshot -> {
                                            if (productSnapshot.exists()) {
                                                ProductItem product = productSnapshot.toObject(ProductItem.class);
                                                if (product != null) {
                                                    // Set the ID from the document
                                                    product.setId(productSnapshot.getId());

                                                    favoriteProducts.add(product);
                                                }
                                                // Once all products are loaded, update the RecyclerView adapter
                                                if (favoriteProducts.size() == favoriteProductIds.size()) {
                                                    Log.d("FavouriteFragment", "All favorite products loaded, updating RecyclerView");
                                                    displayFavoriteProducts(favoriteProducts);
                                                }
                                            } else {
                                                Log.e("FavouriteFragment", "Product document does not exist for ID: " + productId);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("FavouriteFragment", "Error fetching product details for ID: " + productId, e);
                                        });
                            }
                        } else {
                            Log.d("FavouriteFragment", "No favorite products found.");
                        }
                    } else {
                        Log.d("FavouriteFragment", "User document does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FavouriteFragment", "Error fetching user document.", e);
                });
    }


    private void displayFavoriteProducts(List<ProductItem> favoriteProducts) {
        Log.d("FavouriteFragment1", "favoriteProducts:  " + favoriteProducts.size());

        PopularAdapter adapter = new PopularAdapter(context, favoriteProducts, item -> {
            // Xử lý khi item được click
            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("PRODUCT_ID", item.getId()); // Chuyển ID sản phẩm
//            Log.d("FavouriteFragment1", "Id(): " + item.getId());
            Log.d("FavouriteFragment1", "item: " + item.getPrice());

            startActivity(intent);
        });
        binding.recFavourite.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadFavoriteProducts(userId);
    }


}

package com.actvn.at170557.storefashion.ui.main.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentHomeBinding; // Import the generated binding class
import com.actvn.at170557.storefashion.ui.detailproduct.DetailProductActivity;
import com.actvn.at170557.storefashion.ui.main.home.adapter.PopularAdapter;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.actvn.at170557.storefashion.ui.main.listproducts.ListProductsActivity;
import com.actvn.at170557.storefashion.ui.search.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding binding; // Use the generated binding class
    private Context context;
    FirebaseFirestore db;

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_home;
    }

    @Override
    protected ViewBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView using ViewBinding
        RecyclerView recyclerView = binding.recPopular;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2)); // 2 columns

        // Load products and set adapter
        loadProductsFromFirestore();
    }

    private void loadProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");

        // Hiện ProgressBar
//        binding.progressBar.setVisibility(View.VISIBLE);

        productsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // Ẩn ProgressBar
                        binding.progress.setVisibility(View.GONE);
                        binding.recPopular.setVisibility(View.VISIBLE);

                        if (task.isSuccessful()) {
                            List<ProductItem> itemList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductItem product = document.toObject(ProductItem.class);
                                product.setId(document.getId());

                                // Tạo đường dẫn hình ảnh
                                String itemId = document.getId();
                                String imagePath = "/img_product/" + itemId + ".jpg"; // Đường dẫn hình ảnh
                                product.setLinkImg(imagePath); // Thiết lập linkImg

                                itemList.add(product);

                                // Log thông tin
                                Log.d("ProductItem", "Brand: " + product.getBrand());
                                Log.d("ProductItem", "Description: " + product.getDescription());
                                Log.d("ProductItem", "Name: " + product.getName());
                                Log.d("ProductItem", "Price: " + product.getPrice());
                                Log.d("ProductItem", "Sizes: " + (product.getSize() != null ? product.getSize().toString() : "No sizes available"));
                                Log.d("ProductItem", "Image Link: " + product.getLinkImg()); // Log đường dẫn hình ảnh
                            }

                            // Kiểm tra danh sách có rỗng không
                            if (itemList.isEmpty()) {
                                Log.d("ProductItem", "No products available.");
                                // Hiện thông báo rằng không có sản phẩm
                            } else {
                                PopularAdapter popularAdapter = new PopularAdapter(context, itemList, item -> {
                                    Intent intent = new Intent(context, DetailProductActivity.class);
                                    intent.putExtra("PRODUCT_ID", item.getId());
                                    startActivity(intent);
                                });
                                binding.recPopular.setAdapter(popularAdapter);
                            }
                        } else {
                            Log.d("ProductItem", "Error getting documents: ", task.getException());
                            // Hiện thông báo lỗi cho người dùng
                        }
                    }
                });
    }



    @Override
    public void onClickViews() {
        super.onClickViews();
        binding.searchView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
        });
        binding.layoutAll.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("CATE", "ALL"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutMen.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("CATE", "MAN"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutWomen.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("CATE", "WOMAN"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutKids.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("CATE", "KIDS"); // Pass the product ID

            startActivity(intent);
        });


        binding.layoutStussy.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("Brand", "Stussy"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutZara.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("Brand", "Zara"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutGucci.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("Brand", "Gucci"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutNike.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("Brand", "Nike"); // Pass the product ID

            startActivity(intent);
        });
        binding.layoutChanel.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra("Brand", "Chanel"); // Pass the product ID

            startActivity(intent);
        });


        binding.imgFilter.setOnClickListener(v -> {
            BottomFilter bottomFilter = new BottomFilter(context);

            // Show the BottomFilter
            bottomFilter.show();
        });
    }


}

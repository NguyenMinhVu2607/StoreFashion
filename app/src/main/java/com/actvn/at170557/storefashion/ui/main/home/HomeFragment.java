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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding binding;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserNameFromFirestore(new FirestoreCallback() {
            @Override
            public void onCallback(String userName) {
                binding.textViewWelcome.setText("Welcome " + userName + " to the app");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = binding.recPopular;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2)); // 2 columns
        loadProductsFromFirestore();
    }

    private void loadProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");
        productsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        binding.progress.setVisibility(View.GONE);
                        binding.recPopular.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            List<ProductItem> itemList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductItem product = document.toObject(ProductItem.class);
                                product.setId(document.getId());
                                String itemId = document.getId();
                                String imagePath = "/img_product/" + itemId + ".jpg";
                                product.setLinkImg(imagePath);
                                itemList.add(product);

                                // Log thông tin
                                Log.d("ProductItem", "Brand: " + product.getBrand());
                                Log.d("ProductItem", "Description: " + product.getDescription());
                                Log.d("ProductItem", "Name: " + product.getName());
                                Log.d("ProductItem", "Price: " + product.getPrice());
                                Log.d("ProductItem", "Sizes: " + (product.getSize() != null ? product.getSize().toString() : "No sizes available"));
                                Log.d("ProductItem", "Image Link: " + product.getLinkImg());
                            }
                            if (itemList.isEmpty()) {
                                Log.d("ProductItem", "No products available.");
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
                        }
                    }
                });
    }

    public void getUserNameFromFirestore(FirestoreCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userName = document.getString("userName");

                                    if (userName != null && !userName.isEmpty()) {
                                        callback.onCallback(userName);
                                    } else {
                                        callback.onCallback("User");
                                    }
                                } else {
                                    callback.onCallback("User");
                                }
                            } else {
                                callback.onCallback("User");
                            }
                        }
                    });
        } else {
            callback.onCallback("No User Logged In");
        }
    }
    public interface FirestoreCallback {
        void onCallback(String userName);
    }
    @Override
    public void onClickViews() {
        super.onClickViews();
        binding.searchView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
        });
        setClickListener(binding.layoutAll, "CATE", "ALL");
        setClickListener(binding.layoutMen, "CATE", "MAN");
        setClickListener(binding.layoutWomen, "CATE", "WOMAN");
        setClickListener(binding.layoutKids, "CATE", "KIDS");

        setClickListener(binding.layoutStussy, "Brand", "Stussy");
        setClickListener(binding.layoutZara, "Brand", "Zara");
        setClickListener(binding.layoutGucci, "Brand", "Gucci");
        setClickListener(binding.layoutNike, "Brand", "Nike");
        setClickListener(binding.layoutChanel, "Brand", "Chanel");

        binding.imgFilter.setOnClickListener(v -> {
            BottomFilter bottomFilter = new BottomFilter(context);
            bottomFilter.show();
        });
    }
    private void setClickListener(View view, String key, String value) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            intent.putExtra(key, value);
            startActivity(intent);
        });
    }

}

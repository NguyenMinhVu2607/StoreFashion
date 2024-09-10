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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentHomeBinding; // Import the generated binding class
import com.actvn.at170557.storefashion.ui.main.home.adapter.PopularAdapter;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.actvn.at170557.storefashion.ui.main.listproducts.ListProductsActivity;
import com.actvn.at170557.storefashion.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding binding; // Use the generated binding class
    private Context context;

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

        // Initialize RecyclerView using ViewBinding
        RecyclerView recyclerView = binding.recPopular;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2)); // 2 columns

        // Prepare list items
        List<ProductItem> itemList = new ArrayList<>();
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));
        itemList.add(new ProductItem(R.drawable.sampleimag, "Stussy T-Shirt", "Subtitle 1", "$100"));

        // Set adapter
        PopularAdapter popularAdapter = new PopularAdapter(context, itemList);
        recyclerView.setAdapter(popularAdapter);
    }

    @Override
    public void onClickViews() {
        super.onClickViews();
        binding.searchView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
        });
        binding.layoutCate.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            startActivity(intent);
        });
        binding.imgFilter.setOnClickListener(v -> {
            BottomFilter bottomFilter = new BottomFilter(context);

            // Show the BottomFilter
            bottomFilter.show();
        });
    }
}

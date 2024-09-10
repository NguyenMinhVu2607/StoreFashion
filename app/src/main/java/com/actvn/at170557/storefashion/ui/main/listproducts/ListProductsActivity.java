package com.actvn.at170557.storefashion.ui.main.listproducts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityListProductsBinding;
import com.actvn.at170557.storefashion.ui.main.home.adapter.PopularAdapter;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;

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
        binding.recListpopular.setLayoutManager(new GridLayoutManager(context, 2));
        PopularAdapter popularAdapter = new PopularAdapter(context, itemList);
        binding.recListpopular.setAdapter(popularAdapter);

        binding.imgBack.setOnClickListener(v -> finish());
    }

    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
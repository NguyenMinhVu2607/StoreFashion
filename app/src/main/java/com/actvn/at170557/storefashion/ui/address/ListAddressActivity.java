package com.actvn.at170557.storefashion.ui.address;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityListAddressBinding;

import java.util.Arrays;
import java.util.List;

public class ListAddressActivity extends AppCompatActivity {

    private ActivityListAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();

        binding.imgBack.setOnClickListener(v -> finish());

        setupRecyclerView();
    }

    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recAddresss;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> addressList = Arrays.asList(
                "Home 1", "Home 2", "Home 3"
        );
        AddressAdapter adapter = new AddressAdapter(addressList);
        recyclerView.setAdapter(adapter);
    }
}

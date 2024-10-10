package com.actvn.at170557.storefashion.ui.pay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // Thêm import để sử dụng Log
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityCheckoutBinding;
import com.actvn.at170557.storefashion.ui.address.ListAddressActivity;
import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("SELECTED_ITEMS");

        if (selectedItems != null) {
            Log.d("CheckoutActivity", "Selected Items Count: " + selectedItems.size());
            for (CartItem item : selectedItems) {
                Log.d("CheckoutActivity", "Item: " + item.getName() + ", Price: " + item.getPrice());
            }
        }
    }


    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}

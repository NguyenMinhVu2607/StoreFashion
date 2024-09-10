package com.actvn.at170557.storefashion.ui.main.mycart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentHomeBinding;
import com.actvn.at170557.storefashion.databinding.FragmentMyCartBinding;
import com.actvn.at170557.storefashion.ui.pay.CheckoutActivity;

import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends BaseFragment {
    private FragmentMyCartBinding binding; // Use the generated binding class
    private Context context;

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_my_cart;
    }

    @Override
    protected ViewBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentMyCartBinding.inflate(inflater, container, false);
        return binding;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();


        RecyclerView recyclerView = binding.recMycart; // Make sure this ID matches your XML layout
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Sample data for cart items
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(R.drawable.sampleimag, "Product 1", "S","$20", "1"));
        cartItems.add(new CartItem(R.drawable.sampleimag, "Product 1", "S","$20", "1"));
        cartItems.add(new CartItem(R.drawable.sampleimag, "Product 1", "S","$20", "1"));
        cartItems.add(new CartItem(R.drawable.sampleimag, "Product 1", "S","$20", "1"));
        cartItems.add(new CartItem(R.drawable.sampleimag, "Product 1", "S","$20", "1"));
        cartItems.add(new CartItem(R.drawable.sampleimag, "Product 1", "S","$20", "1"));


        // Setup adapter
        CartAdapter cartAdapter = new CartAdapter(context, cartItems);
        recyclerView.setAdapter(cartAdapter);
    }

    @Override
    public void onClickViews() {
        super.onClickViews();
        binding.gotoCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(context, CheckoutActivity.class);
            startActivity(intent);

        });
    }
}

package com.actvn.at170557.storefashion.ui.main.mycart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentMyCartBinding;
import com.actvn.at170557.storefashion.ui.main.OnCartItemActionListener;
import com.actvn.at170557.storefashion.ui.main.OnCartItemDeleteListener;
import com.actvn.at170557.storefashion.ui.pay.CheckoutActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCartFragment extends BaseFragment implements OnCartItemDeleteListener, OnCartItemActionListener {
    private FragmentMyCartBinding binding;
    private FirebaseFirestore firestore;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private String userId;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("USER_ID");
        }
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = binding.recMycart;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), cartItems, this, this);
        recyclerView.setAdapter(cartAdapter);
        loadCartItems();

        binding.gotoCheckout.setOnClickListener(v -> {
            List<CartItem> selectedItems = getSelectedItems();
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            intent.putParcelableArrayListExtra("SELECTED_ITEMS", new ArrayList<>(selectedItems));
            startActivity(intent);
        });
    }

    private List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isChecked()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    private void loadCartItems() {
        firestore.collection("Cart").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                            if (items != null) {
                                cartItems.clear();

                                for (Map<String, Object> itemMap : items) {
                                    CartItem cartItem = new CartItem(
                                            (String) itemMap.get("imageUrl"),
                                            (String) itemMap.get("name"),
                                            (String) itemMap.get("size"),
                                            String.valueOf(itemMap.get("price")),
                                            String.valueOf(itemMap.get("quantity"))
                                    );
                                    cartItems.add(cartItem);
                                }
                                cartAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("MyCartFragment", "Document does not exist");
                        }
                    } else {
                        Log.d("MyCartFragment", "Failed to fetch document: ", task.getException());
                    }
                });
    }

    private void updateTotalAmount(List<CartItem> items) {
        double totalAmount = calculateTotalAmount(getSelectedItems());
        TextView tvTotalAmount = binding.tvTotalAmount;
        TextView tvGrandTotal = binding.tvGrandTotal;
        TextView shippingFee = binding.shippingFee;

        String shippingCost = "Free";
        double grandTotal = totalAmount;

        tvTotalAmount.setText("$" + totalAmount);
        tvGrandTotal.setText("$" + grandTotal);
        shippingFee.setText(shippingCost);
    }

    private double calculateTotalAmount(List<CartItem> selectedItems) {
        double total = 0;
        for (CartItem item : selectedItems) {
            if (item.isChecked()) {
                double price = Double.parseDouble(item.getPrice());
                int quantity = Integer.parseInt(item.getQuantity());
                if (quantity > 0) {
                    total += price * quantity;
                }
            }
        }
        return total;
    }

    @Override
    public void onCartItemDelete(int position, CartItem item) {
        removeItemFromFirestore(item, position);
    }

    private void removeItemFromFirestore(CartItem item, int position) {
        firestore.collection("Cart").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                            if (items != null) {
                                items.remove(position);
                                firestore.collection("Cart").document(userId)
                                        .update("items", items)
                                        .addOnSuccessListener(aVoid -> {
                                            cartItems.remove(position);
                                            cartAdapter.notifyItemRemoved(position);
                                            updateTotalAmount(cartItems); // Cập nhật tổng số tiền
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("MyCartFragment", "Error removing item: ", e);
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void onAddQuantity(CartItem cartItem, int position) {
        int newQuantity = Integer.parseInt(cartItem.getQuantity()) + 1;
        cartItem.setQuantity(String.valueOf(newQuantity));
        updateCartInFirebase();
        cartAdapter.notifyItemChanged(position);
    }

    @Override
    public void onRemoveQuantity(CartItem cartItem, int position) {
        int currentQuantity = Integer.parseInt(cartItem.getQuantity());
        if (currentQuantity > 1) {
            cartItem.setQuantity(String.valueOf(currentQuantity - 1));
            updateCartInFirebase();
            cartAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onItemSelected(List<CartItem> selectedItems) {

    }

    @Override
    public void onTotalAmountUpdated() {
        updateTotalAmount(cartItems);
    }

    private void updateCartInFirebase() {
        List<Map<String, Object>> itemsToUpdate = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("imageUrl", cartItem.getImageUrl());
            itemMap.put("name", cartItem.getName());
            itemMap.put("size", cartItem.getSize());
            itemMap.put("price", cartItem.getPrice());
            itemMap.put("quantity", cartItem.getQuantity());
            itemsToUpdate.add(itemMap);
        }
        firestore.collection("Cart").document(userId)
                .update("items", itemsToUpdate)
                .addOnSuccessListener(aVoid -> {
                    Log.d("MyCartFragment", "Giỏ hàng đã được cập nhật thành công");
                    updateTotalAmount(cartItems);
                })
                .addOnFailureListener(e -> {
                    Log.e("MyCartFragment", "Lỗi khi cập nhật giỏ hàng: ", e);
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartItems();
    }
}

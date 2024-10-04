package com.actvn.at170557.storefashion.ui.main.mycart;

import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCartFragment extends BaseFragment {
    private FragmentMyCartBinding binding;
    private FirebaseFirestore firestore;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private String userId; // Biến lưu userId

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
        firestore = FirebaseFirestore.getInstance(); // Khởi tạo Firestore
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cài đặt RecyclerView
        RecyclerView recyclerView = binding.recMycart;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), cartItems);
        recyclerView.setAdapter(cartAdapter);

        // Gọi phương thức để tải dữ liệu từ Firestore
        loadCartItems();
    }
    private void loadCartItems() {
        firestore.collection("Cart").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                            if (items != null) {
                                cartItems.clear(); // Xóa các mục cũ
                                double totalAmount = 0; // Khởi tạo biến tổng

                                for (Map<String, Object> itemMap : items) {
                                    // Chuyển đổi Map thành CartItem
                                    CartItem cartItem = new CartItem(
                                            (String) itemMap.get("imageUrl"),
                                            (String) itemMap.get("name"),
                                            (String) itemMap.get("size"),
                                            String.valueOf(itemMap.get("price")),
                                            String.valueOf(itemMap.get("quantity"))
                                    );
                                    cartItems.add(cartItem); // Thêm vào danh sách

                                    // Cộng dồn số tiền cho từng sản phẩm
                                    double price = Double.parseDouble(String.valueOf(itemMap.get("price"))); // Lấy giá sản phẩm
                                    int quantity = Integer.parseInt(String.valueOf(itemMap.get("quantity"))); // Lấy số lượng
                                    totalAmount += price * quantity; // Cộng dồn vào tổng
                                }

                                // Cập nhật giao diện
                                updateTotalAmount(totalAmount);
                                cartAdapter.notifyDataSetChanged(); // Cập nhật Adapter
                            }
                        } else {
                            Log.d("MyCartFragment", "Document does not exist");
                        }
                    } else {
                        Log.d("MyCartFragment", "Failed to fetch document: ", task.getException());
                    }
                });
    }

    private void updateTotalAmount(double totalAmount) {
        // Giả sử bạn đã có biến cho các TextView của tổng số tiền
        TextView tvTotalAmount = getView().findViewById(R.id.tv_total_amount);
        TextView tvGrandTotal = getView().findViewById(R.id.tv_grand_total);
        TextView shippingFee = getView().findViewById(R.id.shipping_fee);

        // Giả sử phí vận chuyển là miễn phí
        String shippingCost = "Free";
        double grandTotal = totalAmount; // Cập nhật grandTotal

        // Cập nhật giá trị vào TextView
        tvTotalAmount.setText("$" + totalAmount); // Cập nhật tổng tiền
        tvGrandTotal.setText("$" + grandTotal); // Cập nhật tổng tiền
        shippingFee.setText(shippingCost); // Cập nhật phí vận chuyển
    }


    @Override
    public void onClickViews() {
        super.onClickViews();
        binding.gotoCheckout.setOnClickListener(v -> {
            // Xử lý checkout
        });
    }
}

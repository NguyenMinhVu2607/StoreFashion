package com.actvn.at170557.storefashion.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        db.collection("Orders").document(userId).collection("UserOrders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Order order = snapshot.toObject(Order.class);
                            order.setId(snapshot.getId());
                            orderList.add(order);
                        }
                        orderAdapter = new OrderAdapter(orderList, this::cancelOrder);
                        recyclerView.setAdapter(orderAdapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderHistoryActivity.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
                });
    }

    // Hủy đơn hàng và cập nhật Firestore
    private void cancelOrder(Order order) {
        // Kiểm tra xem đơn hàng đã bị hủy chưa
        if (order.getStatus().equals("Canceled")) {
            Toast.makeText(OrderHistoryActivity.this, "Order already canceled", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("UserOrders")
                .document(order.getId())
                .update("status", "Canceled")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OrderHistoryActivity.this, "Order canceled", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại danh sách sau khi hủy
                    order.setStatus("Canceled");
                    // Xóa đơn hàng khỏi danh sách và thông báo cập nhật
                    orderList.remove(order);
                    orderAdapter.notifyItemRemoved(orderList.indexOf(order));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderHistoryActivity.this, "Error canceling order", Toast.LENGTH_SHORT).show();
                });
    }
}

package com.actvn.at170557.storefashion.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void cancelOrder(Order order) {
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
                    order.setStatus("Canceled");
                    int position = orderList.indexOf(order);
                    if (position != -1) {
                        orderAdapter.notifyItemChanged(position);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderHistoryActivity.this, "Error canceling order", Toast.LENGTH_SHORT).show();
                });
    }
}

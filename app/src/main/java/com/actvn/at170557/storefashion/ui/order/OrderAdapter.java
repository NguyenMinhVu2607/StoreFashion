package com.actvn.at170557.storefashion.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderCancelListener cancelListener;

    public interface OnOrderCancelListener {
        void onCancelOrder(Order order);
    }

    public OrderAdapter(List<Order> orders, OnOrderCancelListener cancelListener) {
        this.orderList = orders;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderDetails.setText("Address: " + order.getFullAddress() + "\nPhone: " + order.getPhoneNumber());
        holder.orderTotal.setText("Total: $" + order.getTotalAmount());
        holder.orderStatus.setText("Status: " + order.getStatus());

        holder.cancelOrderButton.setOnClickListener(v -> {
            cancelListener.onCancelOrder(order);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderStatus, orderDetails, orderTotal;
        Button cancelOrderButton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            cancelOrderButton = itemView.findViewById(R.id.cancelOrderButton);
        }
    }
}

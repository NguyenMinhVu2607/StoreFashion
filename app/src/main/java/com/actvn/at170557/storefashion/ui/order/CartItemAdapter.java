package com.actvn.at170557.storefashion.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItemList;

    public CartItemAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pre, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvItemPrice,quantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.cart_item_title);
            quantity = itemView.findViewById(R.id.quantity);
            tvItemPrice = itemView.findViewById(R.id.cart_item_price);
        }

        public void bind(CartItem cartItem) {
            tvItemName.setText(cartItem.getName());
            tvItemPrice.setText("Unit price :" + String.valueOf(cartItem.getPrice()));
            quantity.setText("Quantity: "+cartItem.getQuantity()) ;
        }
    }
}

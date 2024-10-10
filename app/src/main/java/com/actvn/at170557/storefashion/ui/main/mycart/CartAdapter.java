package com.actvn.at170557.storefashion.ui.main.mycart;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.OnCartItemActionListener;
import com.actvn.at170557.storefashion.ui.main.OnCartItemDeleteListener;
import com.actvn.at170557.storefashion.utils.FirebaseStorageHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

// CartAdapter class for handling RecyclerView in MyCartsActivity
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private OnCartItemDeleteListener deleteListener;
    private OnCartItemActionListener actionListener;
    private List<CartItem> selectedItems = new ArrayList<>(); // List to store selected items

    public CartAdapter(Context context, List<CartItem> cartItemList, OnCartItemDeleteListener deleteListener, OnCartItemActionListener actionListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.deleteListener = deleteListener;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        // Load image using FirebaseStorageHelper and Glide
        FirebaseStorageHelper.getImageUri(item.getImageUrl(), uri -> {
            if (uri != null) {
                Glide.with(context)
                        .load(uri.toString())
                        .placeholder(R.drawable.bg_load)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imageView);
            }
        });

        // Set other item details
        holder.titleTextView.setText(item.getName());
        holder.priceTextView.setText("$" + item.getPrice());
        holder.quantityTextView.setText(item.getQuantity());
        holder.cart_item_size.setText("Size: " + item.getSize());

        // Set checkbox state based on selectedItems list
        holder.checkBox.setChecked(selectedItems.contains(item));

        // Handle checkbox selection/deselection
        // Thiết lập checkbox
        holder.checkBox.setChecked(item.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked); // Cập nhật trạng thái isChecked
            actionListener.onItemSelected(selectedItems); // Cập nhật listener với các item được chọn
            actionListener.onTotalAmountUpdated(); // Gọi phương thức để tính toán lại tổng số tiền
        });



        // Handle other buttons for quantity increment/decrement
        holder.add_a_product.setOnClickListener(v -> actionListener.onAddQuantity(item, position));
        holder.minus_a_product.setOnClickListener(v -> actionListener.onRemoveQuantity(item, position));
        holder.removeCityButton.setOnClickListener(v -> deleteListener.onCartItemDelete(position, item));

        holder.removeCityButton.setOnClickListener(v -> {
            deleteListener.onCartItemDelete(position, item);
        });

        holder.add_a_product.setOnClickListener(v -> {
            actionListener.onAddQuantity(item, position);
        });

        holder.minus_a_product.setOnClickListener(v -> {
            actionListener.onRemoveQuantity(item, position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public List<CartItem> getSelectedItems() {
        return selectedItems; // Method to get selected items
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, add_a_product, minus_a_product;
        ImageView removeCityButton;
        TextView titleTextView, priceTextView, quantityTextView, cart_item_size;
        CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cart_item_image);
            add_a_product = itemView.findViewById(R.id.add_a_product);
            minus_a_product = itemView.findViewById(R.id.minus_a_product);
            removeCityButton = itemView.findViewById(R.id.removeCityButton);
            cart_item_size = itemView.findViewById(R.id.cart_item_size);
            titleTextView = itemView.findViewById(R.id.cart_item_title);
            priceTextView = itemView.findViewById(R.id.cart_item_price);
            quantityTextView = itemView.findViewById(R.id.cart_item_quantity);
            checkBox = itemView.findViewById(R.id.checkbox_select_item); // Initialize CheckBox
        }
    }
}

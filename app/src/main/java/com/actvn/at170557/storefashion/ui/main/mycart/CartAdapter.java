package com.actvn.at170557.storefashion.ui.main.mycart;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.utils.FirebaseStorageHelper;
import com.bumptech.glide.Glide;

import java.util.List;

// CartAdapter class for handling RecyclerView in MyCartsActivity
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private OnCartItemDeleteListener deleteListener;
    private OnCartItemActionListener actionlistener;

    // Interface to communicate actions to the Fragment
    public interface OnCartItemActionListener {
        void onAddQuantity(CartItem cartItem, int position);
        void onRemoveQuantity(CartItem cartItem, int position);
    }
    public interface OnCartItemDeleteListener {
        void onCartItemDelete(int position, CartItem item);
    }
    public CartAdapter(Context context, List<CartItem> cartItemList, OnCartItemDeleteListener deleteListener ,OnCartItemActionListener actionlistener ) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.deleteListener = deleteListener;
        this.actionlistener = actionlistener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        // Tải ảnh từ URL
        FirebaseStorageHelper.getImageUri(item.getImageUrl(), new FirebaseStorageHelper.OnImageUriCallback() {
            @Override
            public void onImageUriReceived(Uri uri) {
                if (uri != null) {
                    String imageUrl = uri.toString();
                    Log.d("imageUrl", "imageUrl:: " + imageUrl);
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.bg_load)
                            .error(R.mipmap.ic_launcher)
                            .into(holder.imageView);
                }
            }
        });
        holder.cart_item_size.setText("Size: "+item.getSize());
        holder.titleTextView.setText(item.getName());
        holder.priceTextView.setText(item.getPrice());
        holder.quantityTextView.setText(item.getQuantity());
        holder.removeCityButton.setOnClickListener(v -> {
            // Notify fragment about the delete action
            deleteListener.onCartItemDelete(position, item);
        });
        holder.add_a_product.setOnClickListener(v -> {
            actionlistener.onAddQuantity(item, position); // Notify Fragment when clicked
        });

        // Handle Minus button
        holder.minus_a_product.setOnClickListener(v -> {
            actionlistener.onRemoveQuantity(item, position); // Notify Fragment when clicked
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,add_a_product,minus_a_product;
        ImageView removeCityButton;
        TextView titleTextView, priceTextView, quantityTextView,cart_item_size;

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
        }
    }
}

package com.actvn.at170557.storefashion.ui.order;

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
import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;
import com.actvn.at170557.storefashion.utils.FirebaseStorageHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItemList;
    Context context;

    public CartItemAdapter(List<CartItem> cartItemList,Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
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
        holder.bind(cartItem,context);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvItemPrice,quantity;
        ImageView cart_item_image;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.cart_item_title);
            quantity = itemView.findViewById(R.id.quantity);
            tvItemPrice = itemView.findViewById(R.id.cart_item_price);
            cart_item_image = itemView.findViewById(R.id.cart_item_image);
        }

        public void bind(CartItem cartItem,Context context) {
            tvItemName.setText(cartItem.getName());
            tvItemPrice.setText("Unit price :" + String.valueOf(cartItem.getPrice()));
            quantity.setText("Quantity: "+cartItem.getQuantity()) ;

//            String pathIMG = "/img_product/" + cartItem.getImageUrl() + ".jpg";
//            Log.d("imageUrl", "pathIMG  : " + pathIMG);

            FirebaseStorageHelper.getImageUri(cartItem.getImageUrl(), new FirebaseStorageHelper.OnImageUriCallback() {
                @Override
                public void onImageUriReceived(Uri uri) {
                    if (uri != null) {
                        // URI của ảnh đã được nhận
                        String imageUrl = uri.toString();
                        Log.d("imageUrl", "imageUrl:: " + imageUrl);
                        Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.bg_load)
                                .error(R.mipmap.ic_launcher)
                                .into(cart_item_image);
                    } else {
                        Log.e("imageUrl", "imageUrl:: " );
                    }
                }
            });

        }
    }
}

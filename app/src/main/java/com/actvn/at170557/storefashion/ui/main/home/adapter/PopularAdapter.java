package com.actvn.at170557.storefashion.ui.main.home.adapter;

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
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.actvn.at170557.storefashion.utils.FirebaseStorageHelper;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private final List<ProductItem> itemList;
    private final LayoutInflater inflater;
    private final OnItemClickListener onItemClickListener;
    Context context;

    public PopularAdapter(Context context, List<ProductItem> itemList, OnItemClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem item = itemList.get(position);
        Log.d("PopularAdapter", "item : " + item.getId());
        Log.d("PopularAdapter", "item : " + item.getName());
        holder.textViewName.setText(item.getName());
        holder.textViewBrand.setText(item.getBrand());
        holder.textViewPrice.setText("$" + item.getPrice());
        String pathIMG = "/img_product/" + item.getId() + ".jpg";
        Log.d("imageUrl", "pathIMG  : " + pathIMG);

        FirebaseStorageHelper.getImageUri(pathIMG, new FirebaseStorageHelper.OnImageUriCallback() {
            @Override
            public void onImageUriReceived(Uri uri) {
                if (uri != null) {
                    // URI của ảnh đã được nhận
                    String imageUrl = uri.toString();
                    Log.d("imageUrl", "imageUrl:: " + imageUrl);
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.bg_load) // Placeholder image while loading
                            .error(R.mipmap.ic_launcher) // Error image if loading fails
                            .into(holder.imageViewPro);
                } else {
                    Log.e("imageUrl", "imageUrl:: " );
                }
            }
        });


        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ProductItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewBrand, textViewPrice;
        ImageView imageViewPro;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewBrand = itemView.findViewById(R.id.textViewBrand);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewPro = itemView.findViewById(R.id.imageViewPro);
        }
    }
}

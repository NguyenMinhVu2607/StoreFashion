package com.actvn.at170557.storefashion.ui.main.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private final List<ProductItem> itemList;
    private final LayoutInflater inflater;

    public PopularAdapter(Context context, List<ProductItem> itemList) {
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
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
        holder.imageView.setImageResource(item.getImageResourceId());
        holder.textView1.setText(item.getTitle());
        holder.textView2.setText(item.getSubtitle());
        holder.textView3.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1, textView2, textView3;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }
}
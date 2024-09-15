package com.actvn.at170557.storefashion.ui.main.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private final List<ProductItem> itemList;
    private final LayoutInflater inflater;
    private final OnItemClickListener onItemClickListener;

    public PopularAdapter(Context context, List<ProductItem> itemList, OnItemClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
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
        holder.textViewName.setText(item.getName());
        holder.textViewBrand.setText(item.getBrand());
        holder.textViewPrice.setText("$" + item.getPrice());

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

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewBrand = itemView.findViewById(R.id.textViewBrand);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}

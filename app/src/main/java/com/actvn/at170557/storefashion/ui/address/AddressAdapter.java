package com.actvn.at170557.storefashion.ui.address;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<String> addressList;
    private int selectedPosition = -1;

    public AddressAdapter(List<String> addressList) {
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        String address = addressList.get(position);
        holder.tvTitleAddress.setText(address);

        // Set background drawable resource based on selection
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.button_background_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.button_background);
        }

        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleAddress;
        ConstraintLayout itemLayout;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleAddress = itemView.findViewById(R.id.tv_title_address);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
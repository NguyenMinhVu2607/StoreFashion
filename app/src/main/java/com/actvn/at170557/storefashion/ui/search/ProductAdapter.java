package com.actvn.at170557.storefashion.ui.search;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.home.adapter.PopularAdapter;
import com.actvn.at170557.storefashion.ui.main.home.model.ProductItem;
import com.actvn.at170557.storefashion.utils.FirebaseStorageHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    Context context;
    ProductAdapter.OnItemClickListener onItemClickListener;
    public ProductAdapter(Context context, List<Product> productList, ProductAdapter.OnItemClickListener onItemClickListener) {
        this.productList = productList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productBrand.setText(product.getBrand());
        holder.textViewPrice.setText("$" + product.getPrice());
        String pathIMG = "/img_product/" + product.getId() + ".jpg";
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
                    Log.e("imageUrl", "imageUrl:: ");
                }
            }
        });
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(product));

    }

    public interface OnItemClickListener {
        void onItemClick(Product item);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productBrand, textViewPrice;
        ImageView imageViewPro;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageViewPro = itemView.findViewById(R.id.imageViewPro);
            productName = itemView.findViewById(R.id.textViewName);
            productBrand = itemView.findViewById(R.id.textViewBrand);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}

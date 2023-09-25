package com.example.exercise_03.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.exercise_03.IItemClickListener;
import com.example.exercise_03.Product;
import com.example.exercise_03.R;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {
    private Context context;
    private List<Product> productList;

    private IItemClickListener iItemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public AdapterProduct(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public AdapterProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(context).inflate(R.layout.layout_products, parent, false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProduct.ViewHolder holder, int position) {
        Product product = productList.get(position);

        Glide.with(context)
                .load(product.getThumbnail())
                .into(holder.imgProduct);

//        Log.d("TAG", "onBindViewHolder: "+product.getThumbnail());

        holder.tvTitleProduct.setText(product.getTitle());
        holder.tvPriceProduct.setText("$" + product.getPrice());
        holder.tvDescriptionProduct.setText(product.getDescription());
        double rattingProduct = product.getRating();
        holder.tvRatingProduct.setText(String.valueOf(rattingProduct));
        double discountPercentage = product.getDiscountPercentage();
        holder.tvDiscount.setText("-" + discountPercentage);

//        holder.imgDelete.setOnClickListener(v -> {
//            Toast.makeText(context, product.getId() + "", Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgProduct;
        private TextView tvTitleProduct;

        private TextView tvPriceProduct;

        private TextView tvDescriptionProduct;

        private TextView tvRatingProduct;

        private TextView tvDiscount;

        private ImageButton imgHeart;

        private ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTitleProduct = itemView.findViewById(R.id.tvTitleProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvDescriptionProduct = itemView.findViewById(R.id.tvDescriptionProduct);
            tvRatingProduct = itemView.findViewById(R.id.tvRating);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            imgHeart = itemView.findViewById(R.id.imgHeart);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Log.d("TAG", "onClick: " + pos);

            if (v.getId() == R.id.imgDelete) {
                if (iItemClickListener != null) {
                    iItemClickListener.deleteProduct(pos);
//                    Log.d("TAG", "onClick: " + pos);
                }
            }
        }
    }

    public void updateProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
}

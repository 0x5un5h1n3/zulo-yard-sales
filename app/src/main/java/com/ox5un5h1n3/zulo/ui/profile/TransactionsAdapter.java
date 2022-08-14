package com.ox5un5h1n3.zulo.ui.profile;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.MyAllTransactionViewHolder> {

    private final List<Product> mTransactionList;

    // getting list from the constructor
    public TransactionsAdapter(List<Product> TransactionList) {
        mTransactionList = TransactionList;
    }

    @NonNull
    @NotNull
    @Override
    public MyAllTransactionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new MyAllTransactionViewHolder(mView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAllTransactionViewHolder holder, int position) {
        Product product = mTransactionList.get(position);
        holder.mTvProductName.setText(product.getProductName());
        holder.mTvProductPrice.setText("$ " + product.getProductPrice());
        holder.mTvSoldByName.setText("Sold by : " + product.getOwnerName());
        holder.mTvReservedByName.setText("Reserved by : " + product.getCustomerName());
        Glide.with(holder.itemView).load(product.getProductImage()).into(holder.mImageView);

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_three));
    }

    @Override
    public int getItemCount() {
        return mTransactionList.size();
    }

    public static class MyAllTransactionViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTvProductName;
        private final TextView mTvProductPrice;
        private final TextView mTvSoldByName;
        private final TextView mTvReservedByName;
        private final ImageView mImageView;

        public MyAllTransactionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTvProductName = itemView.findViewById(R.id.tv_product_name);
            mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
            mTvSoldByName = itemView.findViewById(R.id.tv_sold_by_name);
            mTvReservedByName = itemView.findViewById(R.id.tv_reserved_by_name);
            mImageView = itemView.findViewById(R.id.iv_product);
        }
    }
}


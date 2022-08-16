package com.ox5un5h1n3.zulo.ui.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.MyAllProductViewHolder> {

    public static List<Product> mProductList;
    public static int getPosition;
    private Activity mActivity;

    private MaterialTextView mProductName;
    private MaterialTextView mProductPrice;
    private MaterialTextView mProductDescription;
    private ImageView mProductImage;
    MaterialAlertDialogBuilder dialog;

    private MaterialTextView mSellerName;
    private MaterialTextView mSellerAddress;
    private MaterialTextView mSellerMo;

//    Product product;

    public void setFilteredSearchList(List<Product> filteredList){
        this.mProductList = filteredList;
        notifyDataSetChanged();
    }


    // getting list from the constructor
    public AllProductAdapter(List<Product> productList, Activity activity) {
        mProductList = productList;
        mActivity = activity;
    }

    public AllProductAdapter() {

    }

    @NonNull
    @NotNull
    @Override
    public MyAllProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new MyAllProductViewHolder(mView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAllProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = mProductList.get(position);
        holder.mTvProductName.setText(product.getProductName());
        holder.mTvProductDesc.setText(product.getProductDescription());
        holder.mTvProductPrice.setText("$ " + product.getProductPrice());


        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_recycleview));

        Glide.with(holder.itemView).load(product.getProductImage()).into(holder.mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPosition = position;





                        Navigation.findNavController(view).navigate(R.id.action_navigation_search_main_to_search_item_details);



            }
        });
    }



    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class MyAllProductViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTvProductName;
        private final TextView mTvProductDesc;
        private final TextView mTvProductPrice;
        private final ImageView mImageView;
        private final MaterialCardView cvProduct;




        public MyAllProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTvProductName = itemView.findViewById(R.id.tv_product_name);
            mTvProductDesc = itemView.findViewById(R.id.tv_product_desc);
            mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
            mImageView = itemView.findViewById(R.id.iv_product);
            cvProduct = itemView.findViewById(R.id.cv_product);
        }
    }
    public void addNewList(List<Product> mNewProductList){
        mProductList = mNewProductList;
        notifyDataSetChanged();
    }
}
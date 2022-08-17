package com.ox5un5h1n3.zulo.ui.products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.ProductViewHolder> {

    public static List<Product> mManageProductList;
    public static int getManageProductPosition;
    private final Activity mActivity;
    Product product;
    MaterialAlertDialogBuilder dialog;
    private ProgressDialog mDialog;
    private String toastMsg = "";

    // data is passed into the constructor
    public ManageProductAdapter(List<Product> productList, Activity activity) {
        this.mManageProductList = productList;
        this.mActivity = activity;
    }

    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_products, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mDialog = new ProgressDialog(holder.itemView.getContext());
        mDialog.setMessage("Updating visibility of product");
        mDialog.setCancelable(false);

        Product product = mManageProductList.get(position);
        holder.mTvProductName.setText(product.getProductName());
        holder.mTvProductDescription.setText(product.getProductDescription());
        holder.mTvProductPrice.setText("$ " + product.getProductPrice());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_recycleview));

        Glide.with(holder.itemView).load(product.getProductImage()).into(holder.mImageView);
        holder.mSwitch.setChecked(product.getProductDisplay());
        holder.mBtnRejected.setVisibility(View.GONE);
        holder.mBtnApprove.setVisibility(View.GONE);

        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDialog.show();
                Map<String, Object> updateProductData = new HashMap<>();
                if (isChecked) {
                    updateProductData.put("productDisplay", true);
                    toastMsg = "Product visibility on for users";
                } else {
                    updateProductData.put("productDisplay", false);
                    toastMsg = "Product visibility off for users";
                }
                FirebaseFirestore.getInstance().collection("Products")
                        .document(product.getProductKey()).update(updateProductData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mDialog.cancel();
                                Toast.makeText(holder.itemView.getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        if (product.getProductReserve()) {
            holder.mBtnEditProduct.setVisibility(View.GONE);
            holder.mBtnRejected.setVisibility(View.VISIBLE);
            holder.mBtnApprove.setVisibility(View.VISIBLE);
        }
        if (product.getRequestApproved()) {
            holder.mBtnEditProduct.setVisibility(View.GONE);
            holder.mBtnRejected.setVisibility(View.GONE);
            holder.mBtnApprove.setVisibility(View.GONE);
            holder.mTvProductSoldMsg.setVisibility(View.VISIBLE);
        }
        holder.mBtnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductRequest(true, product);
                holder.mTvProductSoldMsg.setVisibility(View.VISIBLE);
                holder.mBtnEditProduct.setVisibility(View.GONE);
                holder.mBtnRejected.setVisibility(View.GONE);
                holder.mBtnApprove.setVisibility(View.GONE);
            }
        });
        holder.mBtnRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductRequest(false, product);
                holder.mBtnRejected.setVisibility(View.GONE);
                holder.mBtnApprove.setVisibility(View.GONE);
                holder.mBtnEditProduct.setVisibility(View.VISIBLE);
            }
        });
        holder.mBtnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getManageProductPosition = position;

                Navigation.findNavController(v).navigate(R.id.action_navigation_home_main_to_manage_products_to_edit_product);
            }
        });
    }

    private void updateProductRequest(boolean isApproved, Product product) {
        Map<String, Object> updateReserveProduct = new HashMap<>();
        updateReserveProduct.put("productReserve", isApproved);
        updateReserveProduct.put("requestApproved", isApproved);
        FirebaseFirestore.getInstance().collection("Products")
                .document(product.getProductKey()).update(updateReserveProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog = new MaterialAlertDialogBuilder(mActivity);
                        dialog.setTitle("Message");
                        dialog.setMessage("Proceed successfully");
                        dialog.setNegativeButton("OK", null);
                        dialog.show();

                    }
                });


    }

    @Override
    public int getItemCount() {
        return mManageProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvProductName;
        private final TextView mTvProductDescription;
        private final TextView mTvProductPrice;
        private final TextView mTvProductSoldMsg;
        private final ImageView mImageView;
        private final MaterialButton mBtnEditProduct;
        private final MaterialButton mBtnApprove;
        private final MaterialButton mBtnRejected;
        private final SwitchMaterial mSwitch;

        public ProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTvProductName = itemView.findViewById(R.id.tv_product_name);
            mTvProductDescription = itemView.findViewById(R.id.tv_product_desc);
            mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
            mTvProductSoldMsg = itemView.findViewById(R.id.tv_product_sold_msg);

            mImageView = itemView.findViewById(R.id.iv_product);
            mBtnEditProduct = itemView.findViewById(R.id.btn_edit_product);
            mBtnApprove = itemView.findViewById(R.id.btn_approve);
            mBtnRejected = itemView.findViewById(R.id.btn_reject);
            mSwitch = itemView.findViewById(R.id.switch_product);

        }
    }
}


package com.ox5un5h1n3.zulo.ui.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;
import com.ox5un5h1n3.zulo.data.model.UserDetail;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.MyAllProductViewHolder> {

    public static List<Product> mProductList;
    public static int getPosition;
    private Activity mActivity;

    private MaterialTextView mProductName;
    private MaterialTextView mProductPrice;
    private MaterialTextView mProductDescription;
    private ImageView mProductImage;

    private MaterialTextView mSellerName;
    private MaterialTextView mSellerAddress;
    private MaterialTextView mSellerMo;

    Product product;

    // 1st called
    // getting list from the constructor
    public AllProductAdapter(List<Product> productList, Activity activity) {
        mProductList = productList;
        mActivity = activity;
    }

    public AllProductAdapter() {

    }

    // 3rd called
    @NonNull
    @NotNull
    @Override
    public MyAllProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new MyAllProductViewHolder(mView);
    }

    //5th called
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAllProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = mProductList.get(position);
        holder.mTvProductName.setText(product.getProductName());
        holder.mTvProductDesc.setText(product.getProductDescription());
        holder.mTvProductPrice.setText("$ " + product.getProductPrice());





        //for image - using Glide
//        Glide.with(holder.itemView).load(product.getProductImage()).into(holder.mImageView);
//        holder.itemView.setOnClickListener(new View.OnClickListener(l) {
//            @Override
//            public void onClick(View view) {
//
////                Button viewAll = view.findViewById(R.id.btnAddYardSale);
////                viewAll.setOnClickListener(l ->
////                        Navigation.findNavController(l).navigate(R.id.action_navigation_home_main_to_subnav_sample)
////                );
//
//                holder.itemView.setOnClickListener(l ->
//                        Navigation.findNavController(l).navigate(R.id.action_navigation_home_main_to_subnav_sample)
//                );
//
//
////                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
////                intent.putExtra("Product", product);
////                mActivity.startActivity(intent);
//            }
//        });


        Glide.with(holder.itemView).load(product.getProductImage()).into(holder.mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPosition = position;


//                Button viewAll = view.findViewById(R.id.btnAddYardSale);
//                viewAll.setOnClickListener(l ->
//                        Navigation.findNavController(l).navigate(R.id.action_navigation_home_main_to_subnav_sample)
//                );
//                Intent intent = new Intent(mActivity, SearchProductDetail.class);
//                intent.putExtra("Product", product);
//                mActivity.startActivity(intent);

//                Intent intent = new Intent(mActivity, AllProductAdapter.this.getClass());
//                intent.putExtra("Product", product);
//                mActivity.startActivity(intent);

//                mProductName = view.findViewById(R.id.tv_product_name_det);
//                mProductPrice = view.findViewById(R.id.tv_product_price);
//                mProductDescription = view.findViewById(R.id.tv_product_desc);
//                mProductImage = view.findViewById(R.id.iv_product);


//                mProductName = view.findViewById(R.id.tv_product_name_det);

//                mProductName.setText("hello");
//                SearchProductDetail.class.mProductName





//                Toast.makeText(view.getContext(), product.getProductKey(), Toast.LENGTH_SHORT).show();


//                        ViewMaterialDialog viewDialog = new ViewMaterialDialog();;
//                viewDialog.showDialog(mActivity);



//                SearchProductDetail fragment = new SearchProductDetail();
//
//
//
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("Product", product);
//                fragment.setArguments(bundle);





                        Navigation.findNavController(view).navigate(R.id.action_navigation_search_main_to_search_item_details);



            }
        });
    }


    private class ViewMaterialDialog {

        EditText userName, userPhoneNo;
        MaterialTextView sellerName, SellerPhoneNo, SellerLocation;
        private ProgressDialog mDialog;

        public void showDialog(Activity activity){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_user_info, null);
            builder.setView(dialogView);

            mDialog = new ProgressDialog(activity);
            mDialog.setMessage("Reserving product");
            mDialog.setCancelable(false);

//            userName = dialogView.findViewById(R.id.et_name);
//            userPhoneNo = dialogView.findViewById(R.id.et_phone_no);


            mSellerName = dialogView.findViewById(R.id.tv_seller_name);
            mSellerAddress = dialogView.findViewById(R.id.tv_seller_address);
            mSellerMo = dialogView.findViewById(R.id.tv_seller_mobile);

            MaterialButton dialogBtn_cancel = dialogView.findViewById(R.id.btn_request_reserve);
            dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.show();

//                    String name = userName.getText().toString().trim();
//                    String phone = userPhoneNo.getText().toString().trim();

//                    if (name.isEmpty()){
//                        Toast.makeText(activity.getApplicationContext(), "Name is empty", Toast.LENGTH_SHORT).show();
//                        mDialog.cancel();
//                        return;
//                    }
//                    if (phone.isEmpty()){
//                        Toast.makeText(activity.getApplicationContext(), "Phone number is empty", Toast.LENGTH_SHORT).show();
//                        mDialog.cancel();
//                        return;
//                    }
//                    if (phone.length() != 10){
//                        Toast.makeText(activity.getApplicationContext(), "Invalid. Please enter a 10 digit number.", Toast.LENGTH_SHORT).show();
//                        mDialog.cancel();
//                        return;
//                    }

                    checkIsProductReservedAndUpdate();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
//            getSellerInfo();
        }



//        private void checkIsProductReservedAndUpdate1(Activity activity, String userName, String userPhoneNo) {
//            FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    Toast.makeText(mActivity, "Test", Toast.LENGTH_SHORT).show();
////
//                }
////                @Override
////                public void onSuccess(DocumentSnapshot documentSnapshot) {
////                    Product product = documentSnapshot.toObject(Product.class);
////                    if (product != null){
////                        if (!product.getProductReserve()){
////                            Map<String, Object> updateReserveProduct = new HashMap<>();
////                            updateReserveProduct.put("productReserve", true);
////                            updateReserveProduct.put("customerName", userName);
////                            updateReserveProduct.put("customerPhoneNo", userPhoneNo);
////
////                            FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).update(updateReserveProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
////                                @Override
////                                public void onSuccess(Void unused) {
////                                    mDialog.cancel();
////                                    activity.finish();
////                                    Toast.makeText(mActivity, "Reserve request sent successfully", Toast.LENGTH_SHORT).show();
////                                }
////                            });
////                        }
////                        else {
////                            Toast.makeText(mActivity, "Someone already requested for this product", Toast.LENGTH_LONG).show();
////                            activity.finish();
////                        }
////                    } else {
////                        Toast.makeText(mActivity, "This product might be removed", Toast.LENGTH_LONG).show();
////                        activity.finish();
////                    }
////                    mDialog.dismiss();
////                }
//            });
//        }

        private void getSellerInfo(){
        FirebaseFirestore.getInstance().collection("Users").document(product.getProductOwnerUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
                if (userDetail != null){
                    mSellerName.setText("Seller Name : " + userDetail.getUsername());
                    mSellerAddress.setText("Seller address : " + userDetail.getAddress());
                    mSellerMo.setText("Seller phone no. : " + userDetail.getPhoneNumber());
                } else {
                    Toast.makeText(mActivity.getApplicationContext(), "Seller not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        }

        private void checkIsProductReservedAndUpdate() {
            FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Product product = documentSnapshot.toObject(Product.class);
                    UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
                    if (product != null){
                        if (!product.getProductReserve()){
                            Map<String, Object> updateReserveProduct = new HashMap<>();
                            updateReserveProduct.put("productReserve", true);
                            updateReserveProduct.put("customerName", userDetail.getUsername());
                            updateReserveProduct.put("customerPhoneNo", userDetail.getPhoneNumber());

                            FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).update(updateReserveProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mDialog.cancel();
//                                    activity.finish();
                                    Toast.makeText(mActivity.getApplicationContext(), "Reserve request sent successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(mActivity.getApplicationContext(), "Someone already requested for this product", Toast.LENGTH_LONG).show();
//                            activity.finish();
                        }
                    } else {
                        Toast.makeText(mActivity.getApplicationContext(), "This product might be removed", Toast.LENGTH_LONG).show();
//                        activity.finish();
                    }
                    mDialog.dismiss();
                }
            });
        }


    }

    // 2nd called
    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    // 4th called
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
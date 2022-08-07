package com.ox5un5h1n3.zulo.ui.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import java.util.HashMap;
import java.util.Map;


public class SearchProductDetail extends Fragment {

    public MaterialTextView mProductName;
    private MaterialTextView mProductPrice;
    private MaterialTextView mProductDescription;
    private ImageView mProductImage;
    private MaterialButton mReserve;
    private MaterialTextView mSellerName;
    private MaterialTextView mSellerAddress;
    private MaterialTextView mSellerMo;
    private Product product;



    public SearchProductDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mProductName = view.findViewById(R.id.tv_product_name_det);
        mProductPrice = view.findViewById(R.id.tv_product_price);
        mProductDescription = view.findViewById(R.id.tv_product_desc);
        mProductImage = view.findViewById(R.id.iv_product);
        mReserve = view.findViewById(R.id.btn_reserve);
        mSellerName = view.findViewById(R.id.tv_seller_name);
        mSellerAddress = view.findViewById(R.id.tv_seller_address);
        mSellerMo = view.findViewById(R.id.tv_seller_mobile);

        getPreviousScreenProductData();
        reserveButton();

    }

    public void reserveButton() {
        mReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog viewDialog = new ViewDialog();
                viewDialog.showDialog(getActivity());
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void getPreviousScreenProductData() {
//        product = (Product) getIntent().getSerializableExtra("Product");
//        product = (Product) getActivity().getIntent().getSerializableExtra("Product");

//        product = (Product) getActivity().getIntent().getSerializableExtra("Product");
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            bundle.putSerializable("Product", product);
//        }

//        product = (Product) getActivity().getIntent().getSerializableExtra("Product");
//        product = (Product) getActivity().getIntent().getExtras().getSerializable("Product");

//        mProductName.setText("Hello");
//        mProductName.setText(product.getProductName());
//        mProductPrice.setText("Product Price : " + product.getProductPrice());
//        mProductDescription.setText("Product description : " + product.getProductDescription());
//        Glide.with(this).load(product.getProductImage()).into(mProductImage);

//        FirebaseFirestore.getInstance().collection("Users").document(product.getProductOwnerUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
//                if (userDetail != null){
//                    mSellerName.setText("Seller Name : " + userDetail.getUsername());
//                    mSellerAddress.setText("Seller address : " + userDetail.getAddress());
//                    mSellerMo.setText("Seller phone no. : " + userDetail.getPhoneNumber());
//                } else {
//                    Toast.makeText(getActivity(), "Seller not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private class ViewDialog {

        EditText userName;
        EditText userPhoneNo;
        private ProgressDialog mDialog;

        public void showDialog(Activity activity){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_user_info, null);
            builder.setView(dialogView);

            mDialog = new ProgressDialog(activity);
            mDialog.setMessage("Reserving product");
            mDialog.setCancelable(false);

            userName = dialogView.findViewById(R.id.et_name);
            userPhoneNo = dialogView.findViewById(R.id.et_phone_no);

            MaterialButton dialogBtn_cancel = dialogView.findViewById(R.id.btn_request_reserve);
            dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.show();

                    String name = userName.getText().toString().trim();
                    String phone = userPhoneNo.getText().toString().trim();

                    if (name.isEmpty()){
                        Toast.makeText(getActivity(), "Name is empty", Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                        return;
                    }
                    if (phone.isEmpty()){
                        Toast.makeText(getActivity(), "Phone number is empty", Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                        return;
                    }
                    if (phone.length() != 10){
                        Toast.makeText(getActivity(), "Invalid. Please enter a 10 digit number.", Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                        return;
                    }

                    checkIsProductReservedAndUpdate(activity, name, phone);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void checkIsProductReservedAndUpdate(Activity activity, String userName, String userPhoneNo) {
            FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Product product = documentSnapshot.toObject(Product.class);
                    if (product != null){
                        if (!product.getProductReserve()){
                            Map<String, Object> updateReserveProduct = new HashMap<>();
                            updateReserveProduct.put("productReserve", true);
                            updateReserveProduct.put("customerName", userName);
                            updateReserveProduct.put("customerPhoneNo", userPhoneNo);

                            FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).update(updateReserveProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mDialog.cancel();
                                    activity.finish();
                                    Toast.makeText(getActivity(), "Reserve request sent successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getActivity(), "Someone already requested for this product", Toast.LENGTH_LONG).show();
                            activity.finish();
                        }
                    } else {
                        Toast.makeText(getActivity(), "This product might be removed", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                    mDialog.dismiss();
                }
            });
        }
    }
}
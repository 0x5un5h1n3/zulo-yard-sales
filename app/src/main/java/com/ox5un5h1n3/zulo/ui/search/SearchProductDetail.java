package com.ox5un5h1n3.zulo.ui.search;

import static com.ox5un5h1n3.zulo.ui.search.AllProductAdapter.getPosition;
import static com.ox5un5h1n3.zulo.ui.search.AllProductAdapter.mProductList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;
import com.ox5un5h1n3.zulo.data.model.UserDetail;

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
    private ProgressDialog mDialog;

    private MaterialButton btnContactSeller;


    private String sellerContactNo;



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

        btnContactSeller = view.findViewById(R.id.btn_contact_seller);

        getProductData();
        reserveProduct();
        dialTheSeller();

    }

    private void dialTheSeller() {
        btnContactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+sellerContactNo));
                startActivity(intent);
            }
        });
    }

    public void reserveProduct() {
        mReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewDialog viewDialog = new ViewDialog();
//                viewDialog.showDialog(getActivity());

                checkIsProductReservedAndUpdate();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void getProductData() {
//        product = (Product) getIntent().getSerializableExtra("Product");
//        product = (Product) getActivity().getIntent().getSerializableExtra("Product");
//
//        product = (Product) getActivity().getIntent().getSerializableExtra("Product");
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            bundle.putSerializable("Product", product);
//        }

//        product = (Product) getActivity().getIntent().getSerializableExtra("Product");
//        product = (Product) getActivity().getIntent().getExtras().getSerializable("Product");

//        int position = 0;
        Product product = mProductList.get(getPosition);

//        mProductName.setText("Hello");
//        Toast.makeText(getActivity(), product.getProductName(), Toast.LENGTH_SHORT).show();
        mProductName.setText(product.getProductName());
        mProductPrice.setText("Product Price : " + product.getProductPrice());
        mProductDescription.setText("Product description : " + product.getProductDescription());
        Glide.with(this).load(product.getProductImage()).into(mProductImage);

        FirebaseFirestore.getInstance().collection("Users").document(product.getProductOwnerUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
                if (userDetail != null){
                    mSellerName.setText("Seller Name : " + userDetail.getUsername());
                    mSellerAddress.setText("Seller address : " + userDetail.getAddress());
                    mSellerMo.setText("Seller phone no. : " + userDetail.getPhoneNumber());
                    sellerContactNo = userDetail.getPhoneNumber();
                } else {
                    Toast.makeText(getActivity(), "Seller not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void checkIsProductReservedAndUpdate() {

        Product product = mProductList.get(getPosition);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Reserving product");
        mDialog.setCancelable(false);

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
                                Toast.makeText(getActivity(), "Reserve request sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Someone already requested for this product", Toast.LENGTH_LONG).show();
//                            activity.finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "This product might be removed", Toast.LENGTH_LONG).show();
//                        activity.finish();
                }
                mDialog.dismiss();
            }
        });
    }

    private class ViewDialog {

        EditText userName;
        EditText userPhoneNo;
        private ProgressDialog mDialog;

    }
}
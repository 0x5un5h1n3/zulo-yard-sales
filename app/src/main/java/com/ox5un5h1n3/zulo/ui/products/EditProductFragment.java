package com.ox5un5h1n3.zulo.ui.products;

import static com.ox5un5h1n3.zulo.ui.products.ManageProductAdapter.getManageProductPosition;
import static com.ox5un5h1n3.zulo.ui.products.ManageProductAdapter.mManageProductList;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProductFragment extends Fragment {

    MaterialAlertDialogBuilder dialog;
    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductDescription;
    private MaterialButton mUpdate;
    private MaterialTextView mTvChooseImage;
    private ImageView mIvProduct;
    private ProgressDialog mDialog;
    private Uri uri;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), imageUri -> {
                if (imageUri != null) {
                    uri = imageUri;
                    mIvProduct.setImageURI(imageUri);
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProductName = view.findViewById(R.id.et_p_name);
        mProductPrice = view.findViewById(R.id.et_p_price);
        mProductDescription = view.findViewById(R.id.et_p_description);
        mUpdate = view.findViewById(R.id.btn_update);
        mTvChooseImage = view.findViewById(R.id.tv_choose_image);
        mIvProduct = view.findViewById(R.id.iv_product);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Updating Product");
        mDialog.setCancelable(false);

        getSelectedProductDataFromFirebase();
        submitProductData();
        pickImage();

    }


    private void getSelectedProductDataFromFirebase() {
        Product mProduct = mManageProductList.get(getManageProductPosition);

        mProductName.setText(mProduct.getProductName());
        mProductPrice.setText(String.valueOf(mProduct.getProductPrice()));
        mProductDescription.setText(mProduct.getProductDescription());
        Glide.with(EditProductFragment.this).load(mProduct.getProductImage()).into(mIvProduct);
    }

    private void pickImage() {
        mTvChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionLauncher.launch("image/*");
            }
        });
        mIvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionLauncher.launch("image/*");
            }
        });
    }

    private void submitProductData() {
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.show();
                Product mProduct = mManageProductList.get(getManageProductPosition);

                String pName = mProductName.getText().toString().trim();
                String pPrice = mProductPrice.getText().toString().trim();
                String pDescription = mProductDescription.getText().toString().trim();

                if (pName.isEmpty()) {
                    Toast.makeText(getActivity(), "Product name is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                if (pPrice.isEmpty()) {
                    Toast.makeText(getActivity(), "Invalid price", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                if (Double.parseDouble(pPrice) == 0.0) {
                    Toast.makeText(getActivity(), "Invalid price", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                if (pDescription.isEmpty()) {
                    Toast.makeText(getActivity(), "Product description is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }

                Map<String, Object> updateProductData = new HashMap<>();
                updateProductData.put("productName", pName);
                updateProductData.put("productPrice", Double.parseDouble(pPrice));
                updateProductData.put("productDescription", pDescription);

                if (uri == null) {

                    uploadUserData(mProduct.getProductKey(), updateProductData);

                } else {

                    mDialog.setMessage("Updating product image");
                    final StorageReference ref = FirebaseStorage.getInstance().getReference()
                            .child("UserProfile/" + System.currentTimeMillis());
                    UploadTask uploadTask = ref.putFile(uri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            mDialog.setMessage("Adding product data");

                            if (task.isSuccessful()) {
                                return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        updateProductData.put("productImage", uri.toString());
                                        uploadUserData(mProduct.getProductKey(), updateProductData);
                                    }
                                });
                            } else {
                                uploadUserData(mProduct.getProductKey(), updateProductData);
                                throw Objects.requireNonNull(task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    private void uploadUserData(String productKey, Map<String, Object> updateProductData) {
        FirebaseFirestore.getInstance().collection("Products").document(productKey)
                .update(updateProductData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mDialog.cancel();
                        dialog = new MaterialAlertDialogBuilder(getActivity());
                        dialog.setTitle("Message");
                        dialog.setMessage("Product Updated Successfully");
                        dialog.setNegativeButton("OK", null);
                        dialog.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        mDialog.cancel();
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
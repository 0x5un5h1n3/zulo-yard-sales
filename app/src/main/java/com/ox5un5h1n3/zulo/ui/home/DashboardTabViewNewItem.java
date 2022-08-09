package com.ox5un5h1n3.zulo.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;
import com.ox5un5h1n3.zulo.data.model.UserDetail;

import java.util.Objects;


public class DashboardTabViewNewItem extends Fragment {

    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;

    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductDescription;
    private MaterialButton mSubmit;
    private MaterialButton mTakePicture;
    private MaterialTextView mTvChooseImage;
    private ImageView mIvProduct;
    private double lat = 0.0;
    private double lng = 0.0;

    private ProgressDialog mDialog;
    private Uri uri;
    private String ownerName;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), imageUri -> {
                if (imageUri != null) {
                    uri = imageUri;
                    mIvProduct.setImageURI(imageUri);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_new_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference reference = firebaseDatabase.getReference();


        mProductName = view.findViewById(R.id.et_username);
        mProductPrice = view.findViewById(R.id.productPrice);
        mProductDescription = view.findViewById(R.id.et_address);
        mSubmit = view.findViewById(R.id.btnSubmit);
        mTakePicture = view.findViewById(R.id.btnEditProfile);

        mTvChooseImage = view.findViewById(R.id.tv_choose_image);
        mIvProduct = view.findViewById(R.id.imageiewProduct);


        mDialog = new ProgressDialog(getContext());//Recheck
        mDialog.setMessage("Adding Product");
        mDialog.setCancelable(false);

        getUserData();
//        getPreviousScreenCurrentLocation();
        submitProductData();
        pickImage();

    }


    private void getUserData() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
                if (userDetail != null) {
                    ownerName = userDetail.getUsername();
                } else {
                    ownerName = "No owner name";
                }
            }
        });
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

    private void getPreviousScreenCurrentLocation(){
        lat = 0;
        lng = 0;
//        lat = getIntent().getExtras().getDouble("lat");
//        lng = getIntent().getExtras().getDouble("lng");
    }

    private void submitProductData() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.show();

                String pName = mProductName.getText().toString().trim();
                Double pPrice = Double.parseDouble(mProductPrice.getText().toString().trim());
                String pDescription = mProductDescription.getText().toString().trim();
                String pKey = FirebaseFirestore.getInstance().collection("Products").document().getId();
                String pUid = FirebaseAuth.getInstance().getUid();



                if (pName.isEmpty()) {
                    Toast.makeText(getActivity(), "Product name is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                if (pPrice <= 0.0) {
                    Toast.makeText(getActivity(), "Invalid price", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                if (pDescription.isEmpty()) {
                    Toast.makeText(getActivity(), "Product description is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }

                if (uri == null){
                    Toast.makeText(getActivity(), "Product image is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }

                mDialog.setMessage("Uploading product image");
                final StorageReference ref = FirebaseStorage.getInstance().getReference().child("UserProfile/" + System.currentTimeMillis());
                UploadTask uploadTask = ref.putFile(uri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        mDialog.setMessage("Adding product data");
                        if (task.isSuccessful()) {
                            return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Product product = new Product(pKey, pUid, pName, pPrice, pDescription, lat, lng, false, false, uri.toString(),"","", ownerName, true);
                                    uploadUserData(product);
                                }
                            });
                        } else {
                            mDialog.setMessage(task.getException().getMessage());
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                            throw Objects.requireNonNull(task.getException());
                        }
                    }
                });
            }
        });
    }

    private void uploadUserData(Product product) {
        FirebaseFirestore.getInstance().collection("Products").document(product.getProductKey()).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDialog.cancel();
                startActivity(new Intent(getActivity(), MainActivity.class));
//                finish();
                Toast.makeText(getActivity(), "Product Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                mDialog.cancel();
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


//        Button viewAll = view.findViewById(R.id.button);
//        viewAll.setOnClickListener(l ->
//                Navigation.findNavController(l).navigate(R.id.action_navigation_home_main_to_subnav_sample)
//        );
//    }


}
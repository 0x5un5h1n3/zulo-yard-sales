package com.ox5un5h1n3.zulo.ui.home;

import static com.ox5un5h1n3.zulo.ui.home.MapActivity.currentLat;
import static com.ox5un5h1n3.zulo.ui.home.MapActivity.currentLng;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;
import com.ox5un5h1n3.zulo.data.model.UserDetail;

import java.util.Objects;


public class AddNewSalePost extends Fragment {

    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    MaterialAlertDialogBuilder dialog;
    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductDescription;
    private MaterialButton mSubmit;
    private MaterialButton mOpenCam;
    private MaterialButton mTakePicture;
    private MaterialTextView mTvChooseImage;
    private ImageView mIvProduct;
    private double lat = 0.0;
    private double lng = 0.0;
    private ProgressDialog mDialog;
    private Uri uri;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), imageUri -> {
                if (imageUri != null) {
                    uri = imageUri;
                    mIvProduct.setImageURI(imageUri);
                }
            });
    private String ownerName;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_new_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProductName = view.findViewById(R.id.et_product_name);
        mProductPrice = view.findViewById(R.id.et_product_price);
        mProductDescription = view.findViewById(R.id.et_product_desc);
        mSubmit = view.findViewById(R.id.btnSubmitPost);
        mOpenCam = view.findViewById(R.id.btnOpenCamera);
        mTvChooseImage = view.findViewById(R.id.tv_choose_image);
        mIvProduct = view.findViewById(R.id.imageiewProduct);

        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Adding Product");
        mDialog.setCancelable(false);

        getUserData();
        getPreviousScreenCurrentLocation();
        submitProductData();
        pickImage();

        mOpenCam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 5);
                } else {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent);
                }
            }

        });

        //initialize AdMob Interstitial ad
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

//        InterstitialAd.load(getActivity(),"ca-app-pub-8130491741259680/1133559948", adRequest, //original ad
        InterstitialAd.load(getActivity(), "ca-app-pub-3940256099942544/1033173712", adRequest, //sample ad
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getActivity());
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        }, 1500);


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

    private void getPreviousScreenCurrentLocation() {
        lat = currentLat;
        lng = currentLng;
    }

    private void submitProductData() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.show();

                String pName = mProductName.getText().toString().trim();
                double pPrice = Double.parseDouble(mProductPrice.getText().toString().trim());
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

                if (uri == null) {
                    Toast.makeText(getActivity(), "Product image is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                } else {

                }
                mDialog.setMessage("Uploading product image");
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
                                    Product product = new Product(pKey, pUid, pName, pPrice, pDescription, lat, lng, false, false, uri.toString(), "", "", "", ownerName, true);
                                    uploadUserData(product);
                                }
                            });
                        } else {
                            throw Objects.requireNonNull(task.getException());
                        }
                    }
                });
            }
        });
    }

    private void uploadUserData(Product product) {
        FirebaseFirestore.getInstance().collection("Products")
                .document(product.getProductKey()).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mDialog.cancel();

                        dialog = new MaterialAlertDialogBuilder(getActivity());
                        dialog.setTitle("Message");
                        dialog.setMessage("Product Added Successfully");
                        dialog.setNegativeButton("OK", null);
                        dialog.show();

                        assert getParentFragment() != null;

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
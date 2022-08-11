package com.ox5un5h1n3.zulo.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.UserDetail;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditFragment extends Fragment {

    EditText mUsername, mPhoneNo, mAddress;
    MaterialButton mSubmit;
    MaterialTextView mDisplayName, mProfileIcon;
    MaterialAlertDialogBuilder dialog;
    ProgressDialog mDialog;


    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        mSubmit = view.findViewById(R.id.btnUpdateProfile);
        mUsername = view.findViewById(R.id.et_display_name);
        mPhoneNo = view.findViewById(R.id.et_phone);
        mAddress = view.findViewById(R.id.et_address);
        mProfileIcon = view.findViewById(R.id.profileIcon);
        mDisplayName = view.findViewById(R.id.displayName);


        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Updating User");
        mDialog.setCancelable(false);

        getCurrentUserDataFromFirebase();
        submitUserDataToFirebase();

    }

    private void submitUserDataToFirebase() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.show();

                String userName = mUsername.getText().toString().trim();
                String phoneNo = mPhoneNo.getText().toString().trim();
                String address = mAddress.getText().toString().trim();


                if (userName.isEmpty()){
                    Toast.makeText(getActivity(), "Display name is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }

                if (phoneNo.isEmpty()){
                    Toast.makeText(getActivity(), "Phone number is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                if (address.isEmpty()){
                    Toast.makeText(getActivity(), "address is empty", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }

                if (10 > phoneNo.length()){
                    Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    return;
                }
                uploadUserData(userName, phoneNo, address);
                getCurrentUserDataFromFirebase();
            }
        });
    }

    private void uploadUserData(String userName, String phoneNo, String address) {
        Map<String, Object> updatedProfileData = new HashMap<>();
        updatedProfileData.put("username", userName);
        updatedProfileData.put("phoneNumber", phoneNo);
        updatedProfileData.put("address", address);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(updatedProfileData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDialog.cancel();
//                finish();
//                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                dialog = new MaterialAlertDialogBuilder(getActivity());
                dialog.setTitle("Message");
                dialog.setMessage("Profile Updated Successfully");
                dialog.setNegativeButton("OK", null);
                dialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                mDialog.cancel();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCurrentUserDataFromFirebase() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);
                if (userDetail != null){

                    mProfileIcon.setText(String.valueOf(documentSnapshot.getString("username").charAt(0)));
//                profileIcon.setText(String.valueOf(username.getText().charAt(0)));
                    mDisplayName.setText(documentSnapshot.getString("username"));


                    mUsername.setText(userDetail.getUsername());
                    mPhoneNo.setText(userDetail.getPhoneNumber());
                    mAddress.setText(userDetail.getAddress());

                } else {
                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
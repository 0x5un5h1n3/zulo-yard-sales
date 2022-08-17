package com.ox5un5h1n3.zulo.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.UserDetail;

public class ProfileFragment extends Fragment {

    TextView username, email, phone, address, profileIcon, mRegisteredUser;
    Button mBtnEditProfile, mBtnTransactionHistory, mBtnReservationHistory;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;
    private LottieAnimationView lottieAnimationView;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button editProfile = view.findViewById(R.id.btnProfileEditProfile);
        editProfile.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_profile_main_to_profile_edit)
        );

        Button viewTransactionHistory = view.findViewById(R.id.btnTransactionHistory);
        viewTransactionHistory.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_profile_main_to_profile_transaction_history)
        );

        Button viewReservationHistory = view.findViewById(R.id.btnReservationtionHistory);
        viewReservationHistory.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_profile_main_to_profile_reservation_history)
        );


        username = view.findViewById(R.id.profileUsername);
        email = view.findViewById(R.id.profileEmail);
        phone = view.findViewById(R.id.profilePhone);
        address = view.findViewById(R.id.profileAddress);
        profileIcon = view.findViewById(R.id.profileIcon);
        mRegisteredUser = view.findViewById(R.id.textViewRegisteredUser);
        mBtnEditProfile = view.findViewById(R.id.btnProfileEditProfile);
        mBtnTransactionHistory = view.findViewById(R.id.btnTransactionHistory);
        mBtnReservationHistory = view.findViewById(R.id.btnReservationtionHistory);

        lottieAnimationView = view.findViewById(R.id.lottie_loading);
        lottieAnimationView.setAnimation(R.raw.profile_loading);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference docRef = firestore.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserDetail userDetail = documentSnapshot.toObject(UserDetail.class);


                assert userDetail != null;
                lottieAnimationView.setVisibility(View.GONE);

                mRegisteredUser.setVisibility(View.VISIBLE);
                mBtnEditProfile.setVisibility(View.VISIBLE);
                mBtnTransactionHistory.setVisibility(View.VISIBLE);
                mBtnReservationHistory.setVisibility(View.VISIBLE);

                profileIcon.setText(String.valueOf(documentSnapshot.getString("username").charAt(0)));
                profileIcon.setVisibility(View.VISIBLE);
//                profileIcon.setText(String.valueOf(username.getText().charAt(0)));
                username.setText(documentSnapshot.getString("username"));
                username.setVisibility(View.VISIBLE);
                email.setText(documentSnapshot.getString("email"));
                email.setVisibility(View.VISIBLE);

                if (documentSnapshot.getString("phoneNumber") == null) {
                    phone.setText("Phone");
                } else {
                    phone.setText(documentSnapshot.getString("phoneNumber"));
                    phone.setVisibility(View.VISIBLE);
                }

                if (documentSnapshot.getString("address") == null) {
                    address.setText("Address");
                } else {
                    address.setText(documentSnapshot.getString("address"));
                    address.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
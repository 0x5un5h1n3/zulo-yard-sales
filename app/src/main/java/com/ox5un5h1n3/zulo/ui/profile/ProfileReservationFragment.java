package com.ox5un5h1n3.zulo.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProfileReservationFragment extends Fragment {

    private final List<Product> mListOfTransactions = new ArrayList<>();
    private RecyclerView mTransactionsRecycler;
    private ProfileTransactionsAdapter mReservationAdapter;
    private LottieAnimationView lottieAnimationView;
    private TextView mReservationsCount;

    private SearchView mSvProfileReservationSearch;

    public ProfileReservationFragment() {
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
        return inflater.inflate(R.layout.fragment_profile_reservations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTransactionsRecycler = view.findViewById(R.id.rcv_transactions);

        mSvProfileReservationSearch = view.findViewById(R.id.sv_profile_reservations_search);
        mSvProfileReservationSearch.clearFocus();

        mSvProfileReservationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        lottieAnimationView = view.findViewById(R.id.lottie_loading);
        lottieAnimationView.setAnimation(R.raw.loading);
        mReservationsCount = view.findViewById(R.id.tv_reservation_count);

        getTransactions();
    }

    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : mListOfTransactions) {
            if (product.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No such item found", Toast.LENGTH_SHORT).show();
        } else {
            mReservationAdapter.setFilteredProfileReservationList(filteredList);
        }
    }

    private void getTransactions() {
        FirebaseFirestore.getInstance().collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            lottieAnimationView.setVisibility(View.GONE);
                            mTransactionsRecycler.setVisibility(View.VISIBLE);
                            mReservationsCount.setVisibility(View.VISIBLE);
                            mSvProfileReservationSearch.setVisibility(View.VISIBLE);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product products = document.toObject(Product.class);

                                if (products.getProductReserve()) {
                                    if (products.getCustomerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        if (!products.getRequestApproved()) {
                                            mListOfTransactions.add(products);
                                            int count = mListOfTransactions.size();
                                            mReservationsCount.setText("Total Reservation Count: " + count);

                                        }
                                    }

                                }
                            }
                            mReservationAdapter = new ProfileTransactionsAdapter(mListOfTransactions);
                            mTransactionsRecycler.setAdapter(mReservationAdapter);


                        } else {
                            Toast.makeText(getActivity(), "Error getting products", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
package com.ox5un5h1n3.zulo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class AllTransactionsFragment extends Fragment {

    private RecyclerView mTransactionsRecycler;
    private final List<Product> mListOfTransactions = new ArrayList<>();
    private AllTransactionAdapter mTransactionAdapter;
    private LottieAnimationView lottieAnimationView;
    private TextView mTransactionsCount;

    public AllTransactionsFragment() {
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
        return inflater.inflate(R.layout.fragment_all_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTransactionsRecycler = view.findViewById(R.id.rcv_transactions);
        lottieAnimationView = view.findViewById(R.id.lottie_loading);
        lottieAnimationView.setAnimation(R.raw.loading);
        mTransactionsCount = view.findViewById(R.id.tv_transaction_count);

        getAllTransactions();
    }

    private void getAllTransactions() {
        FirebaseFirestore.getInstance().collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            lottieAnimationView.setVisibility(View.GONE);
                            mTransactionsRecycler.setVisibility(View.VISIBLE);
                            mTransactionsCount.setVisibility(View.VISIBLE);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product products = document.toObject(Product.class);



                                if (products.getRequestApproved()){
                                        mListOfTransactions.add(products);
                                        int count = mListOfTransactions.size();
                                        mTransactionsCount.setText("Transaction Count: "+ count);

                                }
                            }
                            mTransactionAdapter = new AllTransactionAdapter(mListOfTransactions);
                            mTransactionsRecycler.setAdapter(mTransactionAdapter);


                        } else {
                            Toast.makeText(getActivity(), "Error getting products", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
private void getTransactions() {
    FirebaseFirestore.getInstance().collection("Products").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        lottieAnimationView.setVisibility(View.GONE);
                        mTransactionsRecycler.setVisibility(View.VISIBLE);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product products = document.toObject(Product.class);



                            if (products.getRequestApproved()){
                                if (products.getProductOwnerUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    mListOfTransactions.add(products);


                                    int count = mListOfTransactions.size();

                                    Toast.makeText(getActivity(), "Item Count " + count, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                        mTransactionAdapter = new AllTransactionAdapter(mListOfTransactions);
                        mTransactionsRecycler.setAdapter(mTransactionAdapter);


                    } else {
                        Toast.makeText(getActivity(), "Error getting products", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
}
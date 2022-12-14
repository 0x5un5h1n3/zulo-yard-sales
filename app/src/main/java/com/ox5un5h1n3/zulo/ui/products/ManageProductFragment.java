package com.ox5un5h1n3.zulo.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ManageProductFragment extends Fragment {

    public static List<Product> mProductList = new ArrayList<>();
    private RecyclerView mProductRecycler;
    private ManageProductAdapter mManageProductAdapter;
    private LottieAnimationView lottieAnimationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_manage_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mProductRecycler = view.findViewById(R.id.rcv_product);

        lottieAnimationView = view.findViewById(R.id.lottie_loading);
        lottieAnimationView.setAnimation(R.raw.loading);

        FirebaseFirestore.getInstance().collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            lottieAnimationView.setVisibility(View.GONE);
                            mProductRecycler.setVisibility(View.VISIBLE);
                            mProductList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Product product = document.toObject(Product.class);

                                if (product.getProductOwnerUid().equals(FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid())) {
                                    mProductList.add(product);
                                }

                            }
                            mManageProductAdapter = new ManageProductAdapter(mProductList, getActivity());
                            mProductRecycler.setAdapter(mManageProductAdapter);

                        } else {
                            Toast.makeText(getActivity(), "Error getting products", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
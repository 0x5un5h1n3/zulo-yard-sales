package com.ox5un5h1n3.zulo.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView mProductRecycler;
    private SearchView mSvSearch;
    private MaterialButton mBtnSearch;
    private MaterialButton mBtnReset;
    private LottieAnimationView lottieAnimationView;
    private AllProductAdapter mAllProductAdapter;
    private  List<Product> mProductList = new ArrayList<>();
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    
    private List<Product> productList;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProductRecycler = view.findViewById(R.id.rcv_product);

        mSvSearch = view.findViewById(R.id.sv_search);
        mSvSearch.clearFocus();



        mSvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
//        mBtnSearch = view.findViewById(R.id.btn_search);
//        mBtnReset = view.findViewById(R.id.btn_reset);

        lottieAnimationView = view.findViewById(R.id.lottie_loading);
        lottieAnimationView.setAnimation(R.raw.loading);

        getAllProducts();
//        searchButton();
//        resetButton();
    }

    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        for(Product product : mProductList){
            if(product.getProductName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(product);
            }
        }

        if(filteredList.isEmpty()){
//            Toast.makeText(getActivity(), "No such item found", Toast.LENGTH_SHORT).show();
        }else{
            mAllProductAdapter.setFilteredSearchList(filteredList);
        }
    }

    private void searchButton() {
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedText = mSvSearch.getSuggestionsAdapter().toString().trim();
                if (searchedText.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter search text", Toast.LENGTH_SHORT).show();
                    return;
                }
                final List<Product> mSearchedProductList = new ArrayList<>();
                for (int i = 0; i < mProductList.size(); i++){
                    if (searchedText.equals(mProductList.get(i).getProductName())){
                        mSearchedProductList.add(mProductList.get(i));
                    }
                }
                mAllProductAdapter.addNewList(mSearchedProductList);
            }
        });
    }
    private void resetButton() {
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEtSearch.setText("");
                getAllProducts();
            }
        });
    }

    private void getAllProducts() {


        FirebaseFirestore.getInstance().collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            mProductList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product products = document.toObject(Product.class);
                                if (currentUser == null) {
                                    makeList(products);
                                } else if (!currentUser.getUid().equals(products.getProductOwnerUid())){
                                    makeList(products);
                                }
//                                makeList(products);

                            }
                            mAllProductAdapter = new AllProductAdapter(mProductList, getActivity());
                            mProductRecycler.setAdapter(mAllProductAdapter);

                            lottieAnimationView.setVisibility(View.GONE);
                            mProductRecycler.setVisibility(View.VISIBLE);
                            mSvSearch.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(getActivity(), "Error getting products", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void makeList(Product product){
        if (product.getProductDisplay()){
            mProductList.add(product);
        }
    }
}
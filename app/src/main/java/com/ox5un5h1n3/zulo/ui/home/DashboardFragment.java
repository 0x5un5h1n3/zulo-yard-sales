package com.ox5un5h1n3.zulo.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ox5un5h1n3.zulo.R;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        Button addYardSaleItem = view.findViewById(R.id.btnAddYardSale);
        addYardSaleItem.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_home_to_dashboard_tab_view_new_item)
        );

        Button manageYardSaleProducts = view.findViewById(R.id.btnManageYardSaleProducts);
        manageYardSaleProducts.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_home_to_manage_products)
        );

        Button viewTransactions = view.findViewById(R.id.btnViewTransactions);
        viewTransactions.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_home_to_view_transactions)
        );

    }
}
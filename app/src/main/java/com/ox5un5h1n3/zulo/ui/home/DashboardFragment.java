package com.ox5un5h1n3.zulo.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.ox5un5h1n3.zulo.MapActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.ui.signin.SignInActivity;
import com.ox5un5h1n3.zulo.ui.signup.SignUpActivity;

import java.util.Map;

public class DashboardFragment extends Fragment {

    MaterialAlertDialogBuilder dialog;

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

        Button findYardSalesOnMap = view.findViewById(R.id.btnFindYardSalesOnMap);
        findYardSalesOnMap.setOnClickListener(l ->
//                displayFindSaleDialog()

                startActivity(new Intent(getActivity(), MapActivity.class))
        );

        Button searchYardSaleItems = view.findViewById(R.id.btnSearchYardSaleItems);
        searchYardSaleItems.setOnClickListener(l ->
                displaySearchDialog()
        );

    }

    private void displaySearchDialog() {
        dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle("Tip");
        dialog.setMessage("Use the 'Search' button on the bottom navigation");
        dialog.setNegativeButton("OK", null);
        dialog.show();
    }

    private void displayFindSaleDialog() {
        dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle("Tip");
        dialog.setMessage("Use the 'Find Sales' button on the bottom navigation");
        dialog.setNegativeButton("OK", null);
        dialog.show();
    }
}
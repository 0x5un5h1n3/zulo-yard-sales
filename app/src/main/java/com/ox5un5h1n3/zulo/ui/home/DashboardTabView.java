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

import com.ox5un5h1n3.zulo.R;
public class DashboardTabView extends Fragment {

    public DashboardTabView() {
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


        Button findYardSale = view.findViewById(R.id.btnFindYardSale);


        findYardSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), DashboardTabViewNewItem.class);
                startActivity(intent);
//                SearchFragment fragment = new SearchFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.Container, fragment);
//                transaction.commit();
            }
        });



        Button viewAll = view.findViewById(R.id.btnAddYardSale);
        viewAll.setOnClickListener(l ->
                Navigation.findNavController(l).navigate(R.id.action_navigation_home_main_to_subnav_sample)
        );

    }
}
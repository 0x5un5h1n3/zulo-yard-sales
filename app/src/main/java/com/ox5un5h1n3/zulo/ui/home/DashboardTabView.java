package com.ox5un5h1n3.zulo.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.ui.search.SearchFragment;
//import com.hci3.aris.data.EnrollmentDataSource;

public class DashboardTabView extends Fragment {

    public DashboardTabView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboad, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button findYardSale = view.findViewById(R.id.btnFindYardSale);


        findYardSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SearchFragment fragment = new SearchFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.Container, fragment);
//                transaction.commit();
            }
        });


//        EnrollmentDataSource sourceData = new EnrollmentDataSource();
//        EnrollmentRecyclerAdapter enrollmentAdapter = new EnrollmentRecyclerAdapter(getContext(), sourceData.getEnrollment());

//        RecyclerView linearRecyclerView = view.findViewById(R.id.enrollment_list);
//        RecyclerView gridRecyclerView = view.findViewById(R.id.enrollment_grid);
//
//        if (gridRecyclerView != null) {
//            GridLayouEnrollmentRecyclerAdaptertManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
//            gridRecyclerView.setLayoutManager(gridLayoutManager);
//            gridRecyclerView.setAdapter(enrollmentAdapter);
//        } else {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//            linearRecyclerView.setLayoutManager(linearLayoutManager);
//            linearRecyclerView.setAdapter(enrollmentAdapter);
//        }

    }
}
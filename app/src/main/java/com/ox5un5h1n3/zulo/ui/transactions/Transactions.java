package com.ox5un5h1n3.zulo.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ox5un5h1n3.zulo.R;

import java.util.ArrayList;
import java.util.List;

public class Transactions extends Fragment {

    public Transactions() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> titles = new ArrayList<>();
        titles.add("Sales");
        titles.add("Purchases");


        TabLayout tabLayout = view.findViewById(R.id.tabBar);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        TransactionsStateAdapter adapter = new TransactionsStateAdapter(getContext(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (TabLayout.Tab tab, int position) ->
                tab.setText(titles.get(position))).attach();

    }
}
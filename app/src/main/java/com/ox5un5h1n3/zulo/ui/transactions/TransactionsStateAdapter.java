package com.ox5un5h1n3.zulo.ui.transactions;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TransactionsStateAdapter extends FragmentStateAdapter {

    final Context ctx;
    final int totalTabs;

    public TransactionsStateAdapter(Context context, Transactions fragment, int totalTabs) {
        super(fragment);
        this.ctx = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SalesFragment();
            default:
                return new PurchasesFragment();
        }
    }

    public int getItemCount() {
        return totalTabs;
    }
}

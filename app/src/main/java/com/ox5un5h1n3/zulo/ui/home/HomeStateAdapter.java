package com.ox5un5h1n3.zulo.ui.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeStateAdapter extends FragmentStateAdapter {

    final Context ctx;
    final int totalTabs;

    public HomeStateAdapter(Context context, HomeFragment fragment, int totalTabs) {
        super(fragment);
        this.ctx = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DashboardTabView();
            case 1:
                return new ForYouTabView();
            default:
                return new CategoriesTabView();
        }
    }

    public int getItemCount() {
        return totalTabs;
    }
}

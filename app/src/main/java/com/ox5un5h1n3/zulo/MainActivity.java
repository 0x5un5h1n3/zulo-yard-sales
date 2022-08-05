package com.ox5un5h1n3.zulo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ox5un5h1n3.zulo.databinding.ActivityMainBinding;
import com.ox5un5h1n3.zulo.ui.signin.SignInActivity;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        // Use binding as an alternative for getting widgets
        // in activity_main instead of findViewById
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(binding.toolbar);

        // Prevent back button on bottom navigation fragments.
        // these are considered as top-level navigation thus shouldn't have one
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_favorites, R.id.navigation_profile)
                .build();

        // Responsible for the navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if (binding.navView != null) {
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            assert binding.navView != null;
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user == null){
//            startActivity(new Intent(MainActivity.this, SignInActivity.class));
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;

        NavController navController = navHostFragment.getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    /* Add toolbar menu options */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Toolbar onClick actions */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.logout) {
//            finish();
//            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout) {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to sign out?");
            builder.setCancelable(true);

            builder.setNegativeButton("Sign out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Signed out!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();

                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            });
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }
}
package com.ox5un5h1n3.zulo.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;


public class ForYouTabView extends Fragment {


    private FloatingActionButton mFabLocation;

    private GoogleMap mGoogleMap;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final List<Product> mProductList = new ArrayList<>();

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                if (entry.getKey().equals(permissionsForLocation[0])) {
                    if (!entry.getValue()) {
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    } else {
                        checkGps();
                    }
                } else if (entry.getKey().equals(permissionsForLocation[1])) {
                    if (!entry.getValue()) {
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    } else {
                        checkGps();
                    }
                }
            }
        }
    });

   private double currentLat = 0.0;
    private double currentLng = 0.0;

    private final String[] permissionsForLocation = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull @Nonnull LocationResult locationResult) {
            currentLng = locationResult.getLastLocation().getLongitude();
            currentLat = locationResult.getLastLocation().getLatitude();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLng), 11.0f));
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_for_you, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        createLocationRequest();
        
        requestPermissionLauncher.launch(permissionsForLocation);

        mFabLocation = view.findViewById(R.id.fab_location);
        BottomNavigationView navView = view.findViewById(R.id.nav_view);

        checkLocation();

    }

    private void addListAndDrawMaker(Product product){

        //getProductDisplay()  is the Flag used in Product POJO class & in DB. If the value is TRUE it means
        // that we will make it visible;  If the value is FALSE then we will not show it.
        if (product.getProductDisplay()){
            mProductList.add(product);
            Objects.requireNonNull(mGoogleMap.addMarker(
                            new MarkerOptions().position(
                                    new LatLng(
                                            product.getProductLat(),
                                            product.getProductLng()))))
                    .setTag(product.getProductKey());
        }
    }

    protected void createLocationRequest() {
        if (locationRequest == null) {
            locationRequest = LocationRequest.create();
        }
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkLocation() {
        mFabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionLauncher.launch(permissionsForLocation);
            }
        });
    }

    //Get all gps related links
    private void checkGps() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(requireActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (locationSettingsResponse.getLocationSettingsStates().isGpsPresent()) {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(), 111);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e("locationSettingsExp: ", sendEx.getMessage());
                    }
                }
            }
        });
    }

    private boolean checkPermission(String permissionName) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.checkSelfPermission(getActivity(), permissionName) == PackageManager.PERMISSION_GRANTED;
        } else {
            return PermissionChecker.checkSelfPermission(getActivity(), permissionName) == PermissionChecker.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Update current location if available
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
//    @Override
//    public void onCameraMoveStarted(int i) {
//        if (i == REASON_GESTURE){
//            fusedLocationClient.removeLocationUpdates(locationCallback);
//        }
//    }
}
package com.ox5un5h1n3.zulo.ui.home;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveStartedListener {

    private FloatingActionButton mFabLocation;

    private GoogleMap mGoogleMap;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final List<Product> mProductList = new ArrayList<>();

    private Marker marker_me;


    // permission checking when user request. Step 1 & 2
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                if (entry.getKey().equals(permissionsForLocation[0])) {
                    if (!entry.getValue()) {
                        Toast.makeText(MapActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    } else {
                        checkGps();
                    }
                } else if (entry.getKey().equals(permissionsForLocation[1])) {
                    if (!entry.getValue()) {
                        Toast.makeText(MapActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    } else {
                        checkGps();
                    }
                }
            }
        }
    });

    // default lat lng
//    private double currentLat = 0.0;
//    private double currentLng = 0.0;
    public static double currentLat = 0.0;
    public static double currentLng = 0.0;

    // require permission for getting location
    private final String[] permissionsForLocation = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    // To receive updated location if user request by location request
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull @Nonnull LocationResult locationResult) {
            currentLng = locationResult.getLastLocation().getLongitude();
            currentLat = locationResult.getLastLocation().getLatitude();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLng), 11.0f));
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setupMap();

        /fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequest();
        requestPermissionLauncher.launch(permissionsForLocation);

        mFabLocation = findViewById(R.id.fab_location);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        checkLocation();

    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style);
        mGoogleMap.setMapStyle(styleOptions);


        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        getAllProduct();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        for (int i = 0; i <= mProductList.size(); i++) {
            if (mProductList.get(i).getProductKey().equals(marker.getTag())) {
                String newLine = System.getProperty("line.separator");

                MaterialAlertDialogBuilder dialog;
                dialog = new MaterialAlertDialogBuilder(MapActivity.this);
                dialog.setTitle(mProductList.get(i).getProductName());
                dialog.setMessage("Description: "+ mProductList.get(i).getProductDescription()
                        + newLine + "Price: "+ mProductList.get(i).getProductPrice());
                dialog.setNegativeButton("Cancel", null);
                dialog.show();

//                Toast.makeText(MapActivity.this, mProductList.get(i).getProductName(), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(this, ProductDetailActivity.class);
//                intent.putExtra("Product", mProductList.get(i));
//                startActivity(intent);
                break;
            }
        }
//        Toast.makeText(MapActivity.this, "onMarkerClick", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void getAllProduct() {
        mProductList.clear();
        mGoogleMap.clear();
        FirebaseFirestore.getInstance().collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Product products = document.toObject(Product.class);

//                                if (currentUser == null) {
//                                    addListAndDrawMaker(products);
//                                } else if (!currentUser.getUid().equals(products.getProductOwnerUid())){
//                                    addListAndDrawMaker(products);
//                                }

                                addListAndDrawMaker(products);
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Getting error while fetching product", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

//        if (product.getProductDisplay()){
//
//            mProductList.add(product);
//            mGoogleMap.addMarker(
//                            new MarkerOptions().position(
//                                    new LatLng(
//                                            product.getProductLat(),
//                                            product.getProductLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.sale)))
//                    .setTag(product.getProductKey());
//        }
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
        SettingsClient client = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (locationSettingsResponse.getLocationSettingsStates().isGpsPresent()) {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        });

        task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapActivity.this, 111);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e("locationSettingsExp: ", sendEx.getMessage());
                    }
                }
            }
        });
    }


    private boolean checkPermission(String permissionName) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED;
        } else {
            return PermissionChecker.checkSelfPermission(this, permissionName) == PermissionChecker.PERMISSION_GRANTED;
        }
    }

    @SuppressLint("MissingPermission")
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
    @Override
    public void onCameraMoveStarted(int i) {
        if (i == REASON_GESTURE){
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
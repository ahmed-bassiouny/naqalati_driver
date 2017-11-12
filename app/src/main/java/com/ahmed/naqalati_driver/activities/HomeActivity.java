package com.ahmed.naqalati_driver.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.ahmed.naqalati_driver.R;
import com.ahmed.naqalati_driver.model.FirebaseRoot;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class HomeActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    SupportMapFragment mapFragment;
    LocationManager locationManager;
    ImageView signout;
    TextView tvRequestCount;
    // local variable
    private final int requestLocationPermission = 123;
    private double currentLat = 0.0;
    private double currentLng = 0.0;
    GoogleMap googleMap;
    Marker userMarker;
    ValueEventListener requestListener;
    private String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById();
        initObjects();
        onClick();
    }

    private void onClick() {
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AwesomeInfoDialog(HomeActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.you_want_signout)
                        .setColoredCircle(R.color.dialogInfoBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true)
                        .setPositiveButtonText(getString(R.string.yes))
                        .setPositiveButtonbackgroundColor(R.color.red_logo)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonText(getString(R.string.no))
                        .setNegativeButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(HomeActivity.this, SigninActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButtonClick(new Closure() {
                            @Override
                            public void exec() {
                            }
                        })
                        .show();
            }
        });
        tvRequestCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ShowRequestsActivity.class));
            }
        });
    }

    private void initLocationListener() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationPermission);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initLocationListener();
    }

    private void initObjects() {
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void findViewById() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        signout = findViewById(R.id.signout);
        tvRequestCount = findViewById(R.id.tv_request_count);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestLocationPermission && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationPermission);
        }
    }

    public void showSettingsAlert() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.open_gps));
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return;
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(user.getUid())
                .child(FirebaseRoot.DB_LAT)
                .setValue(location.getLatitude());

        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(user.getUid())
                .child(FirebaseRoot.DB_LNG)
                .setValue(location.getLongitude());
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        setLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showSettingsAlert();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void setLocation() {
        if (googleMap == null)
            return;
        if (userMarker != null)
            userMarker.remove();
        LatLng person = new LatLng(currentLat, currentLng);
        MarkerOptions markerOptions = new MarkerOptions().position(person).title("Person Name");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
        userMarker = googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(person));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 15), 1000, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRequestListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
        removeRequestListener();
    }

    private ValueEventListener getRequestListener() {
        requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    int requestSize = (int) dataSnapshot.getChildrenCount();
                    tvRequestCount.setText(String.valueOf(requestSize));
                } else {
                    tvRequestCount.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        return requestListener;
    }

    private void initRequestListener() {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).child(FirebaseRoot.DB_PENDING_REQUEST).addValueEventListener(getRequestListener());
    }

    private void removeRequestListener() {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).child(FirebaseRoot.DB_PENDING_REQUEST).removeEventListener(requestListener);
    }
}

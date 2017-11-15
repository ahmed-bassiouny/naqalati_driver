package com.ahmed.naqalati_driver.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ahmed.naqalati_driver.helper.LocationManager;
import com.ahmed.naqalati_driver.helper.Utils;
import com.ahmed.naqalati_driver.model.Driver;
import com.ahmed.naqalati_driver.model.RequestInfo;
import com.ahmed.naqalati_driver.model.RequestStatus;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.ahmed.naqalati_driver.R;
import com.ahmed.naqalati_driver.model.FirebaseRoot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements
        LocationListener, OnMapReadyCallback {

    SupportMapFragment mapFragment;
    LocationManager locationManager;
    ImageView signout;
    TextView tvRequestCount;
    // local variable
    private final int requestLocationPermission = 123;
    private double currentLat = 0.0;
    private double currentLng = 0.0;
    private boolean zoomOnMap = true; // to make zoom first time on map
    GoogleMap googleMap;
    Marker userMarker;
    ValueEventListener requestListener;
    ValueEventListener currentRequestListener;
    private String driverId;
    private String currentRequest = "";

    private RelativeLayout container;
    private CircleImageView profileImage;
    private TextView tvUserName;
    private TextView tvUserPhone;
    private TextView tvTime;
    private Button btnArrived, btnCancel;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById();
        initObjects();
        onClick();
    }

    private void getInfoFromDB() {
        if (driverId.isEmpty())
            return;
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER).child(driverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Driver driver = dataSnapshot.getValue(Driver.class);
                        if (driver == null)
                            return;
                        if (!driver.getCurrentRequest().isEmpty()) {
                            progress.setVisibility(View.VISIBLE);
                            Toast.makeText(HomeActivity.this, R.string.waiting, Toast.LENGTH_SHORT).show();
                            currentRequest = driver.getCurrentRequest();
                            addListenerOnCurrentRequest();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void onClick() {
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,EditProfileActivity.class));
                finish();
            }
        });
        tvRequestCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ShowRequestsActivity.class));
            }
        });
        btnArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                        .child(currentRequest).child(FirebaseRoot.DB_REQUEST_STATUS_IN_REQUESTS)
                        .setValue(RequestStatus.DRIVER_GO_TO_END_POINT);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                        .child(currentRequest).child(FirebaseRoot.DB_REQUEST_STATUS_IN_REQUESTS)
                        .setValue(RequestStatus.CANCEL_FROM_DRIVER);
            }
        });
    }


    private void initObjects() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = new LocationManager(this, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationPermission);
        }
        mapFragment.getMapAsync(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void findViewById() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        signout = findViewById(R.id.signout);
        tvRequestCount = findViewById(R.id.tv_request_count);
        container = findViewById(R.id.container);
        profileImage = findViewById(R.id.profile_image);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserPhone = findViewById(R.id.tv_user_phone);
        tvTime = findViewById(R.id.tv_time);
        btnArrived = findViewById(R.id.btn_arrived);
        btnCancel = findViewById(R.id.btn_cancel);
        progress = findViewById(R.id.progress);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestLocationPermission && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationPermission);
        }else if (requestCode == requestLocationPermission && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            locationManager = new LocationManager(this, this);
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void setLocation() {
        if (googleMap == null)
            return;
        if (userMarker != null)
            userMarker.remove();
        if (currentLat == 0 || currentLng == 0)
            return;
        LatLng person = new LatLng(currentLat, currentLng);
        MarkerOptions markerOptions = new MarkerOptions().position(person);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
        userMarker = googleMap.addMarker(markerOptions);
        if (zoomOnMap) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(person));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 15), 1000, null);
            zoomOnMap = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRequestListener();
        getInfoFromDB();
        if (!Utils.isGpsEnable(this)) {
            showSettingsAlert();
        } else {
            if(locationManager!=null)
            locationManager.addListener();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeRequestListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeListener(this);
        removeListenerOnCurrentRequest();
    }

    private ValueEventListener getRequestListener() {
        requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    int requestSize = (int) dataSnapshot.getChildrenCount();
                    if (requestSize > 0)
                        Utils.showNotificationAboutNewRequest(HomeActivity.this);
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
        if (requestListener == null)
            return;
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).child(FirebaseRoot.DB_PENDING_REQUEST).removeEventListener(requestListener);
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

    private ValueEventListener getListenerOnCurrentRequest() {
        currentRequestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RequestInfo requestInfo = dataSnapshot.getValue(RequestInfo.class);
                if (requestInfo.getRequestStatus() == RequestStatus.DRIVER_GO_TO_START_POINT) {
                    container.setVisibility(View.VISIBLE);
                    btnArrived.setVisibility(View.VISIBLE);
                    setUserInfo(requestInfo);
                    googleMap.clear();
                    setLocation();
                    final LatLng driverLocation = new LatLng(requestInfo.getDriverLat(), requestInfo.getDriverLng());
                    final LatLng startPointLocation = new LatLng(requestInfo.getStartPoint().getLat(), requestInfo.getStartPoint().getLng());
                    Routing routing = new Routing.Builder()
                            .travelMode(Routing.TravelMode.DRIVING)
                            .waypoints(driverLocation, startPointLocation)
                            .withListener(new RoutingListener() {
                                @Override
                                public void onRoutingFailure(RouteException e) {

                                }

                                @Override
                                public void onRoutingStart() {

                                }

                                @Override
                                public void onRoutingSuccess(ArrayList<Route> arrayList, int shortestRouteIndex) {
                                    ArrayList polylines = new ArrayList<>();
                                    String totalTime = "";
                                    //add route(s) to the map.
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        totalTime = arrayList.get(i).getDurationText();
                                        PolylineOptions polyOptions = new PolylineOptions();
                                        polyOptions.color(Color.BLUE);
                                        polyOptions.width(10 + i * 3);
                                        polyOptions.addAll(arrayList.get(i).getPoints());
                                        Polyline polyline = googleMap.addPolyline(polyOptions);
                                        polylines.add(polyline);
                                    }

                                    // Start marker
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(driverLocation);
                                    googleMap.addMarker(options);

                                    // End marker
                                    options = new MarkerOptions();
                                    options.position(startPointLocation);
                                    googleMap.addMarker(options);
                                    tvTime.setText(convertTimeToArabic(totalTime));
                                    progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onRoutingCancelled() {

                                }
                            })
                            .build();
                    routing.execute();
                } else if (requestInfo.getRequestStatus() == RequestStatus.DRIVER_GO_TO_END_POINT) {
                    setUserInfo(requestInfo);
                    container.setVisibility(View.VISIBLE);
                    btnArrived.setVisibility(View.INVISIBLE);
                    googleMap.clear();
                    setLocation();
                    final LatLng startPointLocation = new LatLng(requestInfo.getStartPoint().getLat(), requestInfo.getStartPoint().getLng());
                    final LatLng endPointLocation = new LatLng(requestInfo.getEndPoint().getLat(), requestInfo.getEndPoint().getLng());
                    Routing routing = new Routing.Builder()
                            .travelMode(Routing.TravelMode.DRIVING)
                            .waypoints(startPointLocation, endPointLocation)
                            .withListener(new RoutingListener() {
                                @Override
                                public void onRoutingFailure(RouteException e) {

                                }

                                @Override
                                public void onRoutingStart() {
                                }

                                @Override
                                public void onRoutingSuccess(ArrayList<Route> arrayList, int shortestRouteIndex) {
                                    ArrayList polylines = new ArrayList<>();
                                    String totalTime = "";
                                    //add route(s) to the map.
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        totalTime = arrayList.get(i).getDurationText();
                                        PolylineOptions polyOptions = new PolylineOptions();
                                        polyOptions.color(Color.BLUE);
                                        polyOptions.width(10 + i * 3);
                                        polyOptions.addAll(arrayList.get(i).getPoints());
                                        Polyline polyline = googleMap.addPolyline(polyOptions);
                                        polylines.add(polyline);
                                    }

                                    // Start marker
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(startPointLocation);
                                    googleMap.addMarker(options);

                                    // End marker
                                    options = new MarkerOptions();
                                    options.position(endPointLocation);
                                    googleMap.addMarker(options);
                                    tvTime.setText(convertTimeToArabic(totalTime));
                                    progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onRoutingCancelled() {

                                }
                            })
                            .build();
                    routing.execute();
                } else if(requestInfo.getRequestStatus() == RequestStatus.CANCEL_FROM_DRIVER){
                    Toast.makeText(HomeActivity.this, R.string.driver_cancel, Toast.LENGTH_SHORT).show();
                    googleMap.clear();
                    container.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    removeListenerOnCurrentRequest();
                    removeCurrentRequest();
                } else if(requestInfo.getRequestStatus() == RequestStatus.CANCEL_FROM_USER){
                    Toast.makeText(HomeActivity.this, R.string.user_cancel, Toast.LENGTH_SHORT).show();
                    googleMap.clear();
                    container.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    removeListenerOnCurrentRequest();
                    removeCurrentRequest();
                } else if(requestInfo.getRequestStatus() == RequestStatus.COMPLETE){
                    Toast.makeText(HomeActivity.this, R.string.complete, Toast.LENGTH_SHORT).show();
                    googleMap.clear();
                    container.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    removeListenerOnCurrentRequest();
                    removeCurrentRequest();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress.setVisibility(View.GONE);
            }
        };
        return currentRequestListener;
    }

    private void addListenerOnCurrentRequest() {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                .child(currentRequest).addValueEventListener(getListenerOnCurrentRequest());
    }

    private void removeListenerOnCurrentRequest() {
        if (currentRequestListener != null)
            FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_REQUESTS)
                    .child(currentRequest).removeEventListener(currentRequestListener);
    }

    public void removeCurrentRequest() {
        FirebaseDatabase.getInstance().getReference(FirebaseRoot.DB_DRIVER)
                .child(driverId).child(FirebaseRoot.DB_CURRENT_REQUEST).setValue("");
        currentRequest = "";
        setLocation();
    }

    private void setUserInfo(RequestInfo userInfo) {
        tvUserName.setText(userInfo.getUserName());
        tvUserPhone.setText(userInfo.getUserPhone());
        if (!userInfo.getUserImage().isEmpty())
            Utils.showImage(this, userInfo.getUserImage(), profileImage);
    }

    private String convertTimeToArabic(String time) {
        return time.replace("hours", "ساعة").replace("mins", "دقيقة");
    }
}

package com.kissanfactory.groceryuserapplication.Activities;

import com.kissanfactory.groceryuserapplication.Helpers.PermissionUtils;
import com.kissanfactory.groceryuserapplication.Models.AgentLocationResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackOrderActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    Timer timer;
    private GoogleMap map;
    static Marker carMarker;
    Bitmap BitMapMarker;
    Location myLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getAgentLocation();
            }
        }, 1000, 5000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                LatLng punjab = new LatLng(31.1471, 75.3412);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(punjab, 7.2f));
                map.setMyLocationEnabled(true);
                Polygon polygon1 = map.addPolygon(new PolygonOptions()
                        .add(new LatLng(30.384985, 73.895382),
                                new LatLng(31.130811, 74.675411),
                                new LatLng(31.196904, 74.513127),
                                new LatLng(31.839772, 74.576720),
                                new LatLng(32.499907, 75.836602),
                                new LatLng(32.416520, 75.900133),
                                new LatLng(32.100141, 75.582479),
                                new LatLng(30.586986, 76.902506),
                                new LatLng(29.597583, 75.278943),
                                new LatLng(30.360393, 73.895382))
                        .strokeColor(Color.RED)
                        .strokeWidth(5));
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    // get the delivery agent location
    private void getAgentLocation(){
        String orderId = getIntent().getStringExtra("orderId");
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<AgentLocationResponse> call = new UserApiToJsonHandler().agentLocation(token, orderId);
        call.enqueue(new Callback<AgentLocationResponse>() {
            @Override
            public void onResponse(Call<AgentLocationResponse> call, Response<AgentLocationResponse> response) {
                if (response.isSuccessful()){
                    LatLng latLng = new LatLng(
                            Double.parseDouble(response.body().getData().get(0).getCurrentLocation().getLatitude()),
                            Double.parseDouble(response.body().getData().get(0).getCurrentLocation().getLongitude())
                    );
                    myLocation = new Location("");
                    myLocation.setLatitude(latLng.latitude);
                    myLocation.setLongitude(latLng.longitude);

                    if (carMarker==null){
                        carMarker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(BitMapMarker))
                                .flat(true));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                latLng, 17f);
                        map.animateCamera(cameraUpdate);
                    }else{
                        carMarker.setPosition(latLng);
                    }
                    Toast.makeText(TrackOrderActivity.this, response.body().getData().get(0).getCurrentLocation().getLatitude(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TrackOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgentLocationResponse> call, Throwable t) {
                Toast.makeText(TrackOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getAgentLocation();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}
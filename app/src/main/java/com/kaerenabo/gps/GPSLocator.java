package com.kaerenabo.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class GPSLocator implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private Activity activity;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final int REQUEST_CHECK_SETTINGS = 1001;
    public static final int REQUEST_LOCATION_PERMISSION = 151;
    private LocationCallback locationCallback;
    private float distance = 0;
    private long interval = 0;

    public GPSLocator(){

    }

    public GPSLocator(Activity activity, LocationCallback locationCallback){
        this.activity = activity;
        this.locationCallback = locationCallback;
    }

    /**
     *
     * @param activity
     * @param locationCallback
     * @param interval setting time interval (10000 for 10 sec) of location callback
     * @param distance setting distance () where location callback comes.
     */
    public GPSLocator(Activity activity, LocationCallback locationCallback, long interval, float distance){
        this.activity = activity;
        this.locationCallback = locationCallback;
        this.interval = interval;
        this.distance = distance;
    }

    public void init(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);

        }
        else {
            mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(activity)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }
        }
    }

    private void startLocationUpdates() {
        if (  Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
    }

    private Location getLastLocation(){

        if (  Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }else{

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
        return mLastLocation;
    }

    public void stopLocationUpdates() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {

            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

            mGoogleApiClient.disconnect();
        }
    }

    private LocationRequest createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setSmallestDisplacement(distance);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        return mLocationRequest;
    }

    public void getLocation(){

        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            mLastLocation = getLastLocation();
            sendLocation(mLastLocation);
            startLocationUpdates();

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            final PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                            builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult locationSettingsResult) {
                    Status status = locationSettingsResult.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location requests here.
                            startLocationUpdates();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                status.startResolutionForResult(
                                        activity,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    private void sendLocation(Location location){
        if(location!=null && locationCallback!=null) {
            locationCallback.onReceiveLocation(location.getLatitude(), location.getLongitude());
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
        locationCallback.onGoogleClientConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        sendLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

package com.kaerenabo.gps;

public interface LocationCallback {

    public void onReceiveLocation(double lat, double lng);

    public void onGoogleClientConnected();
}

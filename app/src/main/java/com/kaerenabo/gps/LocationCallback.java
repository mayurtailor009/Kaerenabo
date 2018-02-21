package com.kaerenabo.gps;

/**
 * Created by mayur.tailor on 16-03-2016.
 */
public interface LocationCallback {

    public void onReceiveLocation(double lat, double lng);

    public void onGoogleClientConnected();
}

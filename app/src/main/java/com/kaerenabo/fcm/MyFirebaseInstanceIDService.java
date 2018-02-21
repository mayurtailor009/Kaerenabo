package com.kaerenabo.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
 
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
 
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
 
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
    }
 
    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }
 
    private void storeRegIdInPref(String token) {
        Utils.putStringIntoPref(this, Constant.PREF_DEVICE_TOKEN, token);
    }
}
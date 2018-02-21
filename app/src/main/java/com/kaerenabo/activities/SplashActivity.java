package com.kaerenabo.activities;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.kaerenabo.R;
import com.kaerenabo.models.LoginEnum;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private long splashDelay = 3000;
    Timer timer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LoginEnum loginEnum = Utils.getLoginStateEnum(SplashActivity.this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = null;

                LoginEnum loginEnum = Utils.getLoginStateEnum(SplashActivity.this);
                if(loginEnum == LoginEnum.NUMBER_VERIFY){
                    signInPhone();
                }
                else{
                    if(loginEnum == LoginEnum.FB){
                        i = new Intent(SplashActivity.this, FacebookLoginActivity.class);
                    }

                    else if(loginEnum == LoginEnum.LOCATION_VERIFY){
                        i = new Intent(SplashActivity.this, LocationVerifiyActivity.class);
                    }
                    else{
                        i = new Intent(SplashActivity.this, HomeActivity.class);
                    }
                    finish();
                    startActivity(i);
                }
                //startActivity(new Intent(SplashActivity.this, HomeActivity.class));

            }
        };

        timer = new Timer();
        timer.schedule(task, splashDelay);
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }

    public void signInPhone() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                        .build(),
                777);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == ResultCodes.OK) {
            Utils.putStringIntoPref(this, Constant.PREF_MOBILE_NO, response.getPhoneNumber());
            Utils.setLoginStateEnum(SplashActivity.this, LoginEnum.LOCATION_VERIFY);
            gotoLocationVerifyScreen();
        } else {
            if (response == null) {

                return;
            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                return;
            }
            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                return;
            }
        }
    }


    private void gotoLocationVerifyScreen() {
        Intent intent = new Intent(this, LocationVerifiyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


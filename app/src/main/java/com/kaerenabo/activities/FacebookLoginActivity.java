package com.kaerenabo.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaerenabo.R;
import com.kaerenabo.models.LoginEnum;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        printHash();
        db = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();
        ImageView btnLogin = findViewById(R.id.iv_fb_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this,
                        Arrays.asList("email","user_likes"));
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            handleSignInResponse(resultCode, data);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        checkUserExistOnFirebase(json_object);
                    }

                });
        Bundle param = new Bundle();
        param.putString("fields", "id,name,email,likes,first_name,last_name");
        request.setParameters(param);
        request.executeAsync();

    }

    public void checkUserExistOnFirebase(final JSONObject jsonObject){
        Utils.putStringIntoPref(FacebookLoginActivity.this, Constant.USER_INFO,
                jsonObject.toString());

        JsonObject fbJson = new Gson().fromJson(Utils.getStringFromPref(this, Constant.USER_INFO), JsonObject.class);
        String userId = fbJson.get("id").getAsString();

        db.collection("users").document(userId).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.setLoginStateEnum(FacebookLoginActivity.this, LoginEnum.NUMBER_VERIFY);
                signInPhone();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Utils.setLoginStateEnum(FacebookLoginActivity.this, LoginEnum.HOME);
                    gotoHome();
                }else{
                    Utils.setLoginStateEnum(FacebookLoginActivity.this, LoginEnum.NUMBER_VERIFY);
                    signInPhone();
                }
            }
        });
    }

    private void printHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.kaerenabo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
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

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (resultCode == ResultCodes.OK) {
            Utils.putStringIntoPref(this, Constant.PREF_MOBILE_NO, response.getPhoneNumber());
            Utils.setLoginStateEnum(FacebookLoginActivity.this, LoginEnum.LOCATION_VERIFY);
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
    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

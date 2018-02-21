package com.kaerenabo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kaerenabo.R;
import com.kaerenabo.activities.FacebookLoginActivity;
import com.kaerenabo.activities.HomeActivity;
import com.kaerenabo.activities.LocationVerifiyActivity;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);

        return view;
    }

    private void init(View view){
        ((HomeActivity)getActivity()).hideNavigationIcon();

        view.findViewById(R.id.view_address).setOnClickListener(this);
        view.findViewById(R.id.view_lang).setOnClickListener(this);
        view.findViewById(R.id.view_logout).setOnClickListener(this);

        JsonObject fbJson = new Gson().fromJson(Utils.getStringFromPref(getActivity(), Constant.USER_INFO), JsonObject.class);
        ((TextView)view.findViewById(R.id.tv_name)).setText(fbJson.get("name").getAsString());
        String fbImage = "https://graph.facebook.com/v2.11/"+fbJson.get("id").getAsString()+"/picture?type=normal";
        ImageView ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        Glide.with(getActivity()).load(fbImage).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);

        TextView tvLikes = view.findViewById(R.id.tv_likes);
        tvLikes.setText(android.text.TextUtils.join(",", getFbLikes(fbJson)));
    }

    private ArrayList<String> getFbLikes(JsonObject fbJson){
        ArrayList<String>likes = new ArrayList<>();
        JsonArray likesArr = fbJson.getAsJsonObject("likes").getAsJsonArray("data");
        for(JsonElement jobj : likesArr){
            likes.add(jobj.getAsJsonObject().get("name").getAsString());
        }
        return likes;
    }
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.menu_profile));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_address:
                startLocationVerifyActivity();
                break;
            case R.id.view_lang:
                showLangDialog();
                break;
            case R.id.view_logout:
                Utils.showDialog(getActivity(), getString(R.string.dialog_logout_title),
                        getString(R.string.dialog_logout_msg), getString(R.string.dialog_label_ok),
                        getString(R.string.dialog_label_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        });

                break;
        }
    }

    private void logout(){
        Intent i = new Intent(getActivity(), FacebookLoginActivity.class);
        LoginManager.getInstance().logOut();
        clearSharePref();
        startActivity(i);
        getActivity().finish();
    }
    private void startLocationVerifyActivity(){
        Intent i = new Intent(getActivity(), LocationVerifiyActivity.class);
        i.putExtra(Constant.ARG_IS_LOCATION_UPDATE, true);
        startActivity(i);
        //getActivity().finish();
    }
    private void clearSharePref(){
        SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    public void showLangDialog() {
        CharSequence[] items = {getString(R.string.label_english), getString(R.string.label_danish)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Language");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(item == 0){
                    Utils.putStringIntoPref(getActivity(), Constant.PREF_LOCALE, "en");
                    gotoHome();
                    //ShowLangChangeAlertDialog("en");
                }
                else if(item ==1){
                    Utils.putStringIntoPref(getActivity(), Constant.PREF_LOCALE, "da");
                    gotoHome();
                    //ShowLangChangeAlertDialog("fr");
                }
            }
        });
        builder.show();
    }
    private void ShowLangChangeAlertDialog(final String lang){
        Utils.showDialog(getActivity(), getString(R.string.dialog_title_alert),
                getString(R.string.dialog_change_lang_message), getString(R.string.label_app_restart),
                getString(R.string.label_close_now), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.putStringIntoPref(getActivity(), Constant.PREF_LOCALE, lang);
                        gotoHome();
                    }
                });
    }
    private void gotoHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

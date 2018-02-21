package com.kaerenabo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kaerenabo.AppController;
import com.kaerenabo.R;
import com.kaerenabo.adapters.LocationAdapter;
import com.kaerenabo.gps.GPSLocator;
import com.kaerenabo.gps.LocationCallback;
import com.kaerenabo.interfaces.OnItemClickListenerRecycler;
import com.kaerenabo.models.CoordinateDTO;
import com.kaerenabo.models.GroupDTO;
import com.kaerenabo.models.LocationDTO;
import com.kaerenabo.models.LoginEnum;
import com.kaerenabo.models.UserDTO;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationVerifiyActivity extends BaseActivity implements View.OnClickListener, OnItemClickListenerRecycler, LocationCallback {

    public static String TAG = "LocationVerifiyActivity";
    private EditText etLocation;
    RecyclerView recyclerView;
    LocationAdapter searchAdapter;
    private View parentView;
    private  ArrayList<LocationDTO> locationList;
    private boolean toShowLocationView = true;
    GPSLocator gpsLocator;
    private double lat, lng;
    FirebaseFirestore db;
    LocationDTO locationDTO;
    CoordinateDTO coordinateDTO;
    JsonObject fbJson;
    ArrayList<GroupDTO> groupList = new ArrayList<>();
    private String userId;
    private UserDTO meUser;
    private GroupDTO meGroup;
    private ProgressDialog pDialog;
    private Boolean isLocationUpdate = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_verifiy);

        init();

        getAllGroups();

        if(isLocationUpdate){
            getUserMe();
            getGroupMe();
        }
    }

    private void init(){
        etLocation = findViewById(R.id.et_location);
        if(getIntent()!=null){
            isLocationUpdate = getIntent().getBooleanExtra(Constant.ARG_IS_LOCATION_UPDATE, false);
        }
        if(isLocationUpdate){
            etLocation.setText(Utils.getStringFromPref(this, Constant.PREF_LOCATION));
            setupToolbar();
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.validating_address));
        db = FirebaseFirestore.getInstance();
        fbJson = new Gson().fromJson(Utils.getStringFromPref(this, Constant.USER_INFO), JsonObject.class);
        userId = fbJson.get("id").getAsString();
        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(fbJson.get("name").getAsString());
        setTouchNClick(R.id.btn_continue);
        parentView = findViewById(R.id.activity_location_verifiy);
        recyclerView = findViewById(R.id.rv_location);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!toShowLocationView){
                    toShowLocationView = true;
                    return;
                }
                if(charSequence.length()>0){
                        callLocationApi(charSequence.toString());
                    }else{
                        setAdapter(new ArrayList<LocationDTO>());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        gpsLocator = new GPSLocator(this, this, 10000, 0);
        gpsLocator.init();
    }
    public void setupToolbar(){
        findViewById(R.id.view_header).setVisibility(View.VISIBLE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        TextView tvTitle = (TextView) myToolbar.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.label_change_address));
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.back_icon);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    public void createUser(LocationDTO locationDTO){
        try{
        if(locationDTO == null)
            return;
        if(coordinateDTO == null)
            return;

        meUser = new UserDTO();
        meUser.setFCMtoken(Utils.getStringFromPref(this, Constant.PREF_DEVICE_TOKEN));
        meUser.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
        meUser.setAddress(locationDTO.getTekst());
        meUser.setAddressHref(locationDTO.getAdresse().getHref());
        meUser.setUserID(fbJson.get("id").getAsString());
        meUser.setName(fbJson.get("name").getAsString());
        meUser.setFirstName(fbJson.get("first_name").getAsString());
        meUser.setLastName(fbJson.get("last_name").getAsString());
        meUser.setEmail(fbJson.get("email").getAsString());
        meUser.setPhone(Utils.getStringFromPref(this, Constant.PREF_MOBILE_NO));
        meUser.setLatitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(1));
        meUser.setLongitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(0));
        meUser.setUserLatitude(lat);
        meUser.setUserLongitude(lng);
        meUser.setIsAdmin(true);
        meUser.setIsUserAccepted(false);
        meUser.setCurrentState("addressAuthenticated");
        meUser.setDeviceType("Android");
        meUser.setLikes(getFbLikes());
        db.collection("users").document(meUser.getUserID())
                .set(meUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        // gotoHome();
                        createGroup();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        }catch (Exception e){

        }
    }

    private GroupDTO createGroup(){
        try {
            meGroup = prepareGroup();
            db.collection("groups").document(meUser.getUserID())
                    .set(meGroup)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            createAndUpdateGroup(meGroup, meUser);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
            return meGroup;
        }catch (Exception e){

        }
        return null;
    }
    private ArrayList<String> getFbLikes(){
        ArrayList<String>likes = new ArrayList<>();
        JsonArray likesArr = fbJson.getAsJsonObject("likes").getAsJsonArray("data");
        for(JsonElement jobj : likesArr){
            likes.add(jobj.getAsJsonObject().get("name").getAsString());
        }
        return likes;
    }
    private GroupDTO prepareGroup(){
        meGroup = new GroupDTO();
        meGroup.setFCMtoken(Utils.getStringFromPref(this, Constant.PREF_DEVICE_TOKEN));
        meGroup.setAddress(locationDTO.getTekst());
        meGroup.setAddressLatitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(1));
        meGroup.setAddressLongitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(0));
        meGroup.setUserID(fbJson.get("id").getAsString());
        meGroup.setLikes(getFbLikes());
        meGroup.setBlockedUserIDs(new ArrayList<String>());
        meGroup.setHomeUserIDs(new ArrayList<String>());
        meGroup.setNearByUserIDs(new ArrayList<String>());
        return meGroup;
    }
    public ArrayList<GroupDTO> getAllGroups(){
        db.collection("groups").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("","");
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    // Convert the whole Query Snapshot to a list
                    // of objects directly! No need to fetch each
                    // document.
                    List<GroupDTO> types = documentSnapshots.toObjects(GroupDTO.class);
                    groupList.addAll(types);
                    Log.d(TAG, "onSuccess: " + types.size());
                }
            }
        });
        return groupList;
    }

    public void createAndUpdateGroup(GroupDTO myGroup, UserDTO myUser){
        try {

            boolean toUpdate = false;
            boolean hasAdmin = false;
            Location locationMy = Utils.getLocation(myGroup.getAddressLatitude(), myGroup.getAddressLongitude());
            ArrayList<String> homeUserid = new ArrayList<>();
            ArrayList<String> nearbyUserid = new ArrayList<>();
            ArrayList<String> allDevice = new ArrayList<>();
            myGroup.setHomeUserIDs(homeUserid);
            myGroup.setNearByUserIDs(nearbyUserid);

            for (GroupDTO groupDTO : groupList) {
                toUpdate = false;
                if (groupDTO.getHomeUserIDs() == null)
                    groupDTO.setHomeUserIDs(new ArrayList<String>());
                if (groupDTO.getNearByUserIDs() == null)
                    groupDTO.setNearByUserIDs(new ArrayList<String>());

                if (!groupDTO.getUserID().equals(myUser.getUserID())) {

                    Location locationDest = Utils.getLocation(groupDTO.getAddressLatitude(), groupDTO.getAddressLongitude());
                    if (Utils.getLocationDiff(locationMy, locationDest) <= Constant.NEIGHBOUR_DISTANCE) {
                        toUpdate = true;
                        myGroup.getNearByUserIDs().add(groupDTO.getUserID());
                        if (!groupDTO.getNearByUserIDs().contains(myGroup.getUserID())) {
                            groupDTO.getNearByUserIDs().add(myGroup.getUserID());
                            toUpdate = true;
                            if (checkLikeMatch(myGroup.getLikes(), groupDTO.getLikes())) {
                                allDevice.add(groupDTO.getFCMtoken());
                            }
                        }
                    } else if (groupDTO.getNearByUserIDs().contains(myGroup.getUserID())) {
                        groupDTO.getNearByUserIDs().remove(myGroup.getUserID());
                        toUpdate = true;
                    }
                    // Find Home Users
                    if (groupDTO.getAddress().equals(myGroup.getAddress())) {
                        if(!myGroup.getHomeUserIDs().contains(groupDTO.getUserID()))
                            myGroup.getHomeUserIDs().add(groupDTO.getUserID());
                        if(!groupDTO.getHomeUserIDs().contains(myGroup.getUserID()))
                            groupDTO.getHomeUserIDs().add(myGroup.getUserID());
                        toUpdate = true;
                        //myUser.setIsAdmin(true);
                        hasAdmin = true;
                    } else if (groupDTO.getHomeUserIDs().contains(myGroup.getUserID())) {
                        groupDTO.getHomeUserIDs().remove(myGroup.getUserID());
                        toUpdate = true;
                    }
                    if (toUpdate) {
                        updateGroup(groupDTO);
                    }
                }
            }
            updateGroup(myGroup);
            updateUser(myUser, myGroup, hasAdmin);
        }catch (Exception e){

        }
    }

    public void updateGroup(GroupDTO groupDTO){
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("homeUserIDs", groupDTO.getHomeUserIDs());
        updates.put("nearByUserIDs", groupDTO.getNearByUserIDs());
        updates.put("address", groupDTO.getAddress());
        updates.put("addressLatitude", groupDTO.getAddressLatitude());
        updates.put("addressLongitude", groupDTO.getAddressLongitude());
        db.collection("groups").document(groupDTO.getUserID()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void updateUser(UserDTO userDTO, GroupDTO groupDTO, boolean hasAdmin){
        boolean isAdmin = false, hasUserAccepted = false;
        HashMap<String, Object> updates = new HashMap<>();
        if(groupDTO.getHomeUserIDs().size() == 0){
            isAdmin = true;
            hasUserAccepted = true;
        } else if(hasAdmin){
            isAdmin = false;
            hasUserAccepted = false;
        }
        updates.put("isAdmin", isAdmin);
        updates.put("isUserAccepted", hasUserAccepted);
        updates.put("address", userDTO.getAddress());
        updates.put("latitude", userDTO.getLatitude());
        updates.put("longitude", userDTO.getLongitude());
        db.collection("users").document(userDTO.getUserID()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pDialog.dismiss();
                gotoHome();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public boolean checkLikeMatch(ArrayList<String> likeMe, ArrayList<String> likeYour){
        for(String like : likeMe){
            if(likeYour.contains(like)){
                return true;
            }
        }
        return false;
    }

    public void setAdapter(ArrayList<LocationDTO> list){
        locationList = list;
        searchAdapter = new LocationAdapter(this, list);
        searchAdapter.SetClickListner(this);
        recyclerView.setAdapter(searchAdapter);
        if(list!=null && list.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.btn_continue:
                if(etLocation.getText().toString().trim().equals("")){
                    Toast.makeText(this, getString(R.string.location_validation_message), Toast.LENGTH_LONG).show();
                    return;
                }
                pDialog.show();
                try{
                    Utils.putStringIntoPref(this, Constant.PREF_LOCATION, etLocation.getText().toString());
                    if(isLocationUpdate){
                        createAndUpdateGroup(meGroup, meUser);
                    }else{
                        createUser(locationDTO);
                    }
                }
                catch (Exception e){
                    pDialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void callLocationApi(String keyWord){
        String tag_json_obj = keyWord;

        String url = Constant.LOCATION_API+keyWord;

        /*final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();*/

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if(response.length()>0){
                            ArrayList<LocationDTO> list = new Gson().fromJson(response.toString(),
                                    new TypeToken<ArrayList<LocationDTO>>() {
                            }.getType());

                            setAdapter(list);
                        }
                        else{
                            setAdapter(new ArrayList<LocationDTO>());
                        }
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        });

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onItemClick(View view, int position) {
        Utils.hideKeyboard(this);
        recyclerView.setVisibility(View.GONE);
        toShowLocationView = false;
        etLocation.setText(locationList.get(position).getTekst());
        callLocationForLatLngApi(locationList.get(position));
        locationDTO = locationList.get(position);
    }

    public void callLocationForLatLngApi(final LocationDTO locationDTO){
        String tag_json_obj = locationDTO.getAdresse().getHref();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                locationDTO.getAdresse().getHref(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        coordinateDTO = new Gson().fromJson(response.toString(), CoordinateDTO.class);
                        pDialog.hide();

                        Location locationSource = new Location("");
                        locationSource.setLatitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(1));
                        locationSource.setLongitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(0));

                        Location locationDest = new Location("");
                        locationDest.setLatitude(lat);
                        locationDest.setLongitude(lng);

                        /*float distance = Utils.getLocationDiff(locationSource, locationDest);
                        Toast.makeText(LocationVerifiyActivity.this, "Distance:- " +
                                distance, Toast.LENGTH_LONG).show();
                        if(distance<=30){

                        }
                        else{
                            Utils.showDialog(LocationVerifiyActivity.this, getString(R.string.dialog_title_alert),
                                    getString((R.string.dialog_message_invalid_location)));
                        }*/

                        if(isLocationUpdate && meGroup!=null && meUser!=null){

                            meGroup.setAddress(locationDTO.getTekst());
                            meGroup.setAddressLatitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(1));
                            meGroup.setAddressLongitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(0));

                            meUser.setAddress(locationDTO.getTekst());
                            meUser.setAddressHref(locationDTO.getAdresse().getHref());
                            meUser.setLatitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(1));
                            meUser.setLongitude(coordinateDTO.getAdgangsadresse().getAdgangspunkt().getKoordinater().get(0));
                            meUser.setUserLatitude(lat);
                            meUser.setUserLongitude(lng);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gpsLocator!=null)
        gpsLocator.stopLocationUpdates();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == GPSLocator.REQUEST_CHECK_SETTINGS){
                gpsLocator.getLocation();
            }
        }
    }

    @Override
    public void onReceiveLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public void onGoogleClientConnected() {
        gpsLocator.getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPSLocator.REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(gpsLocator!=null)
                        gpsLocator.init();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void gotoHome() {
        Utils.setLoginStateEnum(LocationVerifiyActivity.this, LoginEnum.HOME);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void getGroupMe(){
        try {
            db.collection("groups").document(userId).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    meGroup = documentSnapshot.toObject(GroupDTO.class);
                }
            });
        }catch (Exception e){
        }
    }

    public void getUserMe(){
        try {
            db.collection("users").document(userId).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    meUser = documentSnapshot.toObject(UserDTO.class);
                }
            });
        }catch (Exception e){
        }
    }
}

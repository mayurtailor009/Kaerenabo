package com.kaerenabo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaerenabo.R;
import com.kaerenabo.activities.HomeActivity;
import com.kaerenabo.adapters.GlobalPostAdapter;
import com.kaerenabo.adapters.PostAdapter;
import com.kaerenabo.models.GloalPostDTO;
import com.kaerenabo.models.GroupDTO;
import com.kaerenabo.models.PostDTO;
import com.kaerenabo.models.UserDTO;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class NeighbourFragment extends Fragment implements View.OnClickListener{

    RecyclerView recyclerView;
    PostAdapter postAdapter;
    FirebaseFirestore db;
    private String userId;
    ArrayList<PostDTO> postList = new ArrayList<>();
    private String eventType;
    private ProgressDialog pDialog;
    private TextView tvGroupMember, tvGroupHeading;
    private GroupDTO groupMe;
    private UserDTO userMe;
    private ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            eventType = bundle.getString(Constant.ARG_EVENT_TYPE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(eventType.equals(Constant.HOME_GROUP)){
            tvGroupHeading.setText(getString(R.string.label_home_group));
        }
        else{
            tvGroupHeading.setText(getString(R.string.label_neighbour_group));
        }

        getUserMe();
        allGlobalPost();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_neighbour, container, false);
        init(view);

        return view;
    }

    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.app_name));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_event:
                Fragment fragment = new CalendarEventFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.ARG_EVENT_TYPE, eventType);
                fragment.setArguments(bundle);
                HomeActivity activity = (HomeActivity) getActivity();
                activity.addFragment(fragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(View view){
        viewPager = view.findViewById(R.id.pager);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        tvGroupMember = view.findViewById(R.id.tv_member);
        tvGroupHeading = view.findViewById(R.id.tv_group_heading);
        ((HomeActivity)getActivity()).showNavigationIcon();
        JsonObject fbJson = new Gson().fromJson(Utils.getStringFromPref(getActivity(), Constant.USER_INFO), JsonObject.class);
        userId = fbJson.get("id").getAsString();
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.rv_post);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        view.findViewById(R.id.view_neighbour_group).setOnClickListener(this);
    }

    public void setPostAdapter(ArrayList<PostDTO> list){
        postAdapter = new PostAdapter(getActivity(), list);
        recyclerView.setAdapter(postAdapter);
    }

    public void getAllPost(final GroupDTO groupDTO){
        try{
            Query query = FirebaseFirestore.getInstance()
                    .collection("posts")
                    .whereEqualTo("postType", Constant.IMAGE_POST)
                    .whereEqualTo("postedToGroup", eventType)
                    .orderBy("postDate", Query.Direction.DESCENDING);
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    pDialog.dismiss();
                    postList.clear();
                    List<String> ids = getPostUsersIds(groupDTO);
                    tvGroupMember.setText(""+ids.size());
                    if (e != null) {
                        return;
                    }
                    List<PostDTO> types = snapshot.toObjects(PostDTO.class);
                    for(PostDTO dto : types){
                        if(ids.contains(dto.getUserID())){
                            postList.add(dto);
                        }
                    }
                    setPostAdapter(postList);
                }
            });
/*            db.collection("posts")
                    .whereEqualTo("postType", Constant.IMAGE_POST)
                    .whereEqualTo("postedToGroup", eventType)
                    .orderBy("postDate")
                    .get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    postList.clear();
                    pDialog.dismiss();
                    List<String> ids = getPostUsersIds(groupDTO);
                    tvGroupMember.setText(""+ids.size());
                    if (documentSnapshots.isEmpty()) {
                       return;
                    } else {
                        List<PostDTO> types = documentSnapshots.toObjects(PostDTO.class);
                        for(PostDTO dto : types){
                            if(ids.contains(dto.getUserID())){
                                postList.add(dto);
                            }
                        }
                        setPostAdapter(postList);
                    }
                }
            });*/
        }catch (Exception e){
            pDialog.dismiss();
        }
    }

    public ArrayList<String> getPostUsersIds(GroupDTO groupDTO){
        ArrayList<String> ids = null;
        if(eventType.equals(Constant.HOME_GROUP)){
            ids = groupDTO.getHomeUserIDs();
        }
        else{
            ids = groupDTO.getNearByUserIDs();
        }
        ids.add(userId);
        if(groupDTO.getBlockedUserIDs()!=null && groupDTO.getBlockedUserIDs().size()>0){
            for (String blockUser : groupDTO.getBlockedUserIDs()){
                if(ids.contains(blockUser)){
                    ids.remove(blockUser);
                }
            }
        }
        return ids;
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
                    groupMe = documentSnapshot.toObject(GroupDTO.class);
                    if(eventType.equals(Constant.HOME_GROUP)){
                        tvGroupMember.setText(""+groupMe.getHomeUserIDs().size());
                    } else{
                        tvGroupMember.setText(""+groupMe.getNearByUserIDs().size());
                    }
                    getAllPost(groupMe);
                }
            });
        }catch (Exception e){
            pDialog.dismiss();
        }
    }
    public void getUserMe(){
        try {
            pDialog.show();
            db.collection("users").document(userId).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userMe = documentSnapshot.toObject(UserDTO.class);
                    if(eventType.equals(Constant.HOME_GROUP) && userMe!=null && !userMe.getIsUserAccepted()){
                        // show dialog.
                        Utils.showDialog(getActivity(), getString(R.string.dialog_label_ok), getString(R.string.approval_pending_msg));
                        pDialog.dismiss();
                    }else{
                        getGroupMe();
                    }
                }
            });
        }catch (Exception e){
            pDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_neighbour_group:
                Fragment fragment = new GroupMemberFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.ARG_GROUP, groupMe);
                bundle.putString(Constant.ARG_EVENT_TYPE, eventType);
                fragment.setArguments(bundle);
                HomeActivity activity = (HomeActivity) getActivity();
                activity.addFragment(fragment);
                break;
        }
    }

    public void setPager(ArrayList<String> list){
        GlobalPostAdapter mCustomPagerAdapter = new GlobalPostAdapter(getActivity(), list);
        viewPager.setAdapter(mCustomPagerAdapter);
    }

    public void allGlobalPost(){
        try{
            db.collection("globalMessage").document("message").get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        GloalPostDTO gloalPostDTO = documentSnapshot.toObject(GloalPostDTO.class);
                        setPager(gloalPostDTO.getMessages());
                    }
                }
            });
        }catch (Exception e){
        }
    }
}

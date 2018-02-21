package com.kaerenabo.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaerenabo.R;
import com.kaerenabo.activities.HomeActivity;
import com.kaerenabo.adapters.GroupMemberAdapter;
import com.kaerenabo.interfaces.OnItemClickListenerRecycler;
import com.kaerenabo.models.GroupDTO;
import com.kaerenabo.models.UserDTO;
import com.kaerenabo.utilities.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupMemberFragment extends Fragment implements OnItemClickListenerRecycler {
    RecyclerView recyclerView;
    GroupMemberAdapter userAdapter;
    private ArrayList<String> userIds;
    private ArrayList<UserDTO> userList;
    FirebaseFirestore db;
    private ProgressDialog pDialog;
    private GroupDTO groupMe;
    private String eventType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            groupMe = (GroupDTO) bundle.getSerializable(Constant.ARG_GROUP);
            eventType =  bundle.getString(Constant.ARG_EVENT_TYPE);
            if (eventType.equals(Constant.HOME_GROUP)) {
                userIds = groupMe.getHomeUserIDs();
            } else {
                userIds = groupMe.getNearByUserIDs();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_member, container, false);

        init(view);

        if (userIds != null && userIds.size() > 0) {
            getAllUsers();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void init(View view) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        db = FirebaseFirestore.getInstance();

        ((HomeActivity) getActivity()).setBackNavigationIcon();

        recyclerView = view.findViewById(R.id.rv_members);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setToolbarTitle(getString(R.string.title_group_member));
    }

    public void setAdapter(ArrayList<UserDTO> list) {
        userAdapter = new GroupMemberAdapter(getActivity(), list, eventType);
        userAdapter.SetClickListner(this);
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        showOprDialog(userList.get(position));
    }

    public void showOprDialog(final UserDTO userDTO) {

        final ArrayList<String> options = new ArrayList<>();
        if(groupMe.getBlockedUserIDs().contains(userDTO.getUserID())){
            options.add(getString(R.string.unblock));
        }else{
            options.add(getString(R.string.block));
        }
        if (eventType.equals(Constant.HOME_GROUP)) {
            if(!userDTO.getIsAdmin()){
                options.add(getString(R.string.label_change_to_admin));
            }
            if(!userDTO.getIsUserAccepted()){
                options.add(getString(R.string.label_add_in_group));
            }
        }
        CharSequence[] items = new CharSequence[options.size()];
        for(int i=0;i<options.size();i++){
            items[i] = options.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String action = options.get(item);
                if(action.equals(getString(R.string.block))){
                    groupMe.getBlockedUserIDs().add(userDTO.getUserID());
                    updateGroup(groupMe, true);
                } else if(action.equals(getString(R.string.unblock))){
                    groupMe.getBlockedUserIDs().remove(userDTO.getUserID());
                    updateGroup(groupMe, false);
                } else if(action.equals(getString(R.string.label_add_in_group))){
                    userDTO.setIsUserAccepted(true);
                    updateUserAsAccepted(userDTO);
                } else if(action.equals(getString(R.string.label_change_to_admin))){
                    userDTO.setIsAdmin(true);
                    updateUserAsAdmin(userDTO);
                }
            }
        });
        builder.show();
    }

    public void getAllUsers() {
        try {
            db.collection("users")
                    .get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    pDialog.dismiss();
                    if (documentSnapshots.isEmpty()) {
                        return;
                    } else {
                        List<UserDTO> types = documentSnapshots.toObjects(UserDTO.class);
                        UserDTO myUserDTO = null;
                        if (userList == null)
                            userList = new ArrayList<>();
                        for (String userId : userIds) {
                            for (UserDTO userDTO : types) {
                                if (userId.equals(userDTO.getUserID())) {
                                    myUserDTO = new UserDTO();
                                    myUserDTO.setName(userDTO.getName());
                                    myUserDTO.setUserID(userDTO.getUserID());
                                    myUserDTO.setIsAdmin(userDTO.getIsAdmin());
                                    myUserDTO.setIsUserAccepted(userDTO.getIsUserAccepted());
                                    userList.add(myUserDTO);
                                    break;
                                }
                            }
                        }
                        setAdapter(userList);
                    }
                }
            });
        } catch (Exception e) {
            pDialog.dismiss();
        }
    }
    public void updateGroup(GroupDTO groupDTO, final boolean isBlock){
        pDialog.show();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("blockedUserIDs", groupDTO.getBlockedUserIDs());
        db.collection("groups").document(groupDTO.getUserID()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pDialog.dismiss();
                if(isBlock){
                    Toast.makeText(getActivity(), getString(R.string.block_success_msg), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.unblock_success_msg), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
            }
        });
    }
    public void updateUserAsAdmin(UserDTO userDTO){
        pDialog.show();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("isAdmin", userDTO.getIsAdmin());
        db.collection("users").document(userDTO.getUserID()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), getString(R.string.user_as_admin_success), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
            }
        });
    }
    public void updateUserAsAccepted(UserDTO userDTO){
        pDialog.show();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("isUserAccepted", userDTO.getIsUserAccepted());
        db.collection("users").document(userDTO.getUserID()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), getString(R.string.user_accepted_success), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
            }
        });
    }
}

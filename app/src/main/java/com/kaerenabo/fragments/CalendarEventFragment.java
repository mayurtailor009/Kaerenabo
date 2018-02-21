package com.kaerenabo.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaerenabo.R;
import com.kaerenabo.activities.HomeActivity;
import com.kaerenabo.adapters.MontlyEventAdapter;
import com.kaerenabo.customviews.CustomCalendarView;
import com.kaerenabo.interfaces.OnCalendarDateSelect;
import com.kaerenabo.models.CalendarDTO;
import com.kaerenabo.models.GroupDTO;
import com.kaerenabo.models.PostDTO;
import com.kaerenabo.models.UserDTO;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarEventFragment extends Fragment implements View.OnClickListener, OnCalendarDateSelect {

    View view;
    private CustomCalendarView customCalendarView;
    private ArrayList<CalendarDTO> dateList;
    FirebaseFirestore db;
    private String userId;
    ArrayList<PostDTO> postList = new ArrayList<>();
    private String eventType;
    private ProgressDialog pDialog;
    HashMap<CalendarDTO, ArrayList<PostDTO>> eventMap = new HashMap();
    private ListView lvEvent;
    public CalendarEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        init();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            eventType = bundle.getString(Constant.ARG_EVENT_TYPE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getGroupMe();
    }

    private void init(){
        lvEvent = (ListView) view.findViewById(R.id.lv_events);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        ((HomeActivity)getActivity()).setBackNavigationIcon();
        JsonObject fbJson = new Gson().fromJson(Utils.getStringFromPref(getActivity(), Constant.USER_INFO), JsonObject.class);
        userId = fbJson.get("id").getAsString();
        db = FirebaseFirestore.getInstance();

        customCalendarView = (CustomCalendarView) view.findViewById(R.id.calendar_view);
        customCalendarView.setOnCalendarDateSelect(this);
    }

    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.title_calendar_event));
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void getSelectedDate(CalendarDTO calendarDTO) {
        setEventListAdapter(calendarDTO);
    }

    public void setEventListAdapter(CalendarDTO calendarDTO){
        MontlyEventAdapter montlyEventAdapter = new MontlyEventAdapter(getActivity(), eventMap.get(calendarDTO));
        lvEvent.setAdapter(montlyEventAdapter);
    }

    public void getAllPost(final GroupDTO groupDTO){
        try{
            db.collection("posts")
                    .whereEqualTo("postType", Constant.EVENT_POST)
                    .whereEqualTo("postedToGroup", eventType)
                    .get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    postList.clear();
                    if (documentSnapshots.isEmpty()) {
                        return;
                    } else {
                        List<String> ids = getPostUsersIds(groupDTO);
                        List<PostDTO> types = documentSnapshots.toObjects(PostDTO.class);
                        for(PostDTO dto : types){
                            if(ids.contains(dto.getUserID())){
                                postList.add(dto);
                            }
                        }
                        postList.addAll(getAllRemainingPost(postList));
                        getAllUsers();
                    }
                }
            });
        }catch (Exception e){
            pDialog.dismiss();
        }
    }
    public ArrayList<PostDTO> getAllRemainingPost(ArrayList<PostDTO> list){
        ArrayList<PostDTO> newList = new ArrayList<>();
        for(PostDTO postDTO : list){
            if(!isSameDay(postDTO.getEventStartDate(), postDTO.getEventEndDate())){
                newList.addAll(getDatePostList(postDTO));
            }
        }
        return newList;
    }
    public ArrayList<PostDTO> getDatePostList(PostDTO postDTO){
        ArrayList<PostDTO> list = new ArrayList<>();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(postDTO.getEventStartDate());
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(postDTO.getEventEndDate());
        PostDTO dto = null;
        while (calendarStart.before(calendarEnd)){
            dto = new PostDTO();
            dto.setUserName(postDTO.getUserName());
            dto.setUserID(postDTO.getUserID());
            dto.setPostedToGroup(postDTO.getPostedToGroup());
            dto.setPostType(postDTO.getPostType());
            dto.setPostDescription(postDTO.getPostDescription());
            dto.setPostDate(postDTO.getPostDate());
            dto.setDocumentID(postDTO.getDocumentID());
            dto.setName(postDTO.getName());
            dto.setPostImage(postDTO.getPostImage());
            dto.setPostImageRefrence(postDTO.getPostImageRefrence());

            calendarStart.add(Calendar.DAY_OF_MONTH, 1);
            dto.setEventStartDate(calendarStart.getTime());
            dto.setEventEndDate(postDTO.getEventEndDate());
            list.add(dto);
        }
        return list;
    }
    public ArrayList<String> getPostUsersIds(GroupDTO groupDTO){
        ArrayList<String> ids = null;
        if(eventType.equals(Constant.HOME_GROUP)){
            ids = new ArrayList<>(groupDTO.getHomeUserIDs());
        }
        else{
            ids = new ArrayList<>(groupDTO.getNearByUserIDs());
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
            pDialog.show();
            db.collection("groups").document(userId).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    GroupDTO groupDTO = documentSnapshot.toObject(GroupDTO.class);
                    getAllPost(groupDTO);
                }
            });
        }catch (Exception e){
            pDialog.dismiss();
        }
    }
    public void setCalendarList() {
        ArrayList<CalendarDTO> list = customCalendarView.getCalendarAdapter().getDateList();
        for (CalendarDTO dto : list){
            eventMap.put(dto, getDateWisePost(dto));
        }
        customCalendarView.getCalendarAdapter().setDateList(list);

    }
    public ArrayList<PostDTO> getDateWisePost(CalendarDTO calendarDTO){
        ArrayList<PostDTO> list = new ArrayList<>();
        for(PostDTO postDTO : postList){
            if(isSameDay(new Date(calendarDTO.getTime()), postDTO.getEventStartDate())){
                list.add(postDTO);
                calendarDTO.setHasEvent(true);
            }
        }
        return list;
    }
    public boolean isDateSelected(){
        return true;
    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    public void getAllUsers(){
        try{
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
                        for(PostDTO postDTO : postList){
                            for(UserDTO userDTO : types){
                                if(postDTO.getUserID().equals(userDTO.getUserID())){
                                    postDTO.setName(userDTO.getName());
                                    break;
                                }
                            }
                        }
                    }
                    setCalendarList();
                }
            });
        }catch (Exception e){
            pDialog.dismiss();
        }
    }
}

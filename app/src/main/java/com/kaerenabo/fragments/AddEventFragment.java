package com.kaerenabo.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaerenabo.R;
import com.kaerenabo.activities.HomeActivity;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment implements View.OnClickListener{

    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etTitle;
    private Calendar calendarStartTime;
    private Calendar calendarEndTime;
    private FirebaseFirestore db;
    private ProgressDialog pDialog;
    private String eventType;
    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_event, container, false);

        init(view);

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

    private void init(View view){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        db = FirebaseFirestore.getInstance();
        ((HomeActivity)getActivity()).setBackNavigationIcon();
        calendarStartTime = Calendar.getInstance();
        calendarEndTime = Calendar.getInstance();
        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);
        etTitle = view.findViewById(R.id.et_title);

        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.title_create_event));
    }

    private boolean validateForm(){
        if(etTitle.getText().toString().trim().equals("")){
            showErrorToast();
            return false;
        }
        else if(etStartDate.getText().toString().trim().equals("")){
            showErrorToast();
            return false;
        }
        else if(etEndDate.getText().toString().trim().equals("")){
            showErrorToast();
            return false;
        }
        return true;
    }

    private void showErrorToast(){
        Toast.makeText(getActivity(), "Please enter values in all fields",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_start_date:
                Utils.hideKeyboard(getActivity());
                showStartDatePicker();
                break;
            case R.id.et_end_date:
                Utils.hideKeyboard(getActivity());
                showEndDatePicker();
                break;
            case R.id.btn_submit:
                Utils.hideKeyboard(getActivity());
                if(validateForm()){
                    submitPost();
                }
                break;
        }
    }

    private void showStartDatePicker(){
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1)
                        + "/" + String.valueOf(year);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarStartTime.set(Calendar.MONTH, month);
                calendarStartTime.set(Calendar.YEAR, year);
                etStartDate.setText(date);
            }
       }, calendarStartTime.get(Calendar.YEAR), calendarStartTime.get(Calendar.MONTH), calendarStartTime.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void showEndDatePicker(){
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1)
                        + "/" + String.valueOf(year);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarEndTime.set(Calendar.MONTH, month);
                calendarEndTime.set(Calendar.YEAR, year);
                etEndDate.setText(date);
            }
        }, calendarEndTime.get(Calendar.YEAR), calendarEndTime.get(Calendar.MONTH), calendarEndTime.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    public void submitPost(){
        try {
            JsonObject fbJson = new Gson().fromJson(Utils.getStringFromPref(getActivity(), Constant.USER_INFO), JsonObject.class);

            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", fbJson.get("name").getAsString());
            map.put("userID", fbJson.get("id").getAsString());
            map.put("postType", Constant.EVENT_POST);
            map.put("postedToGroup", eventType);
            map.put("postDescription", etTitle.getText().toString().trim());
            map.put("postDate", Calendar.getInstance().getTimeInMillis());
            //map.put("postDate", FieldValue.serverTimestamp());
            map.put("eventStartDate", calendarStartTime.getTime());
            map.put("eventEndDate", calendarEndTime.getTime());

            pDialog.show();
            db.collection("posts")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(), getString(R.string.event_created_success), Toast.LENGTH_LONG).show();
                            getActivity().onBackPressed();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            pDialog.dismiss();
        }
    }
}

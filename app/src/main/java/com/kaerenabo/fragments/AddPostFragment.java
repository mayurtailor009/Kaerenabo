package com.kaerenabo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaerenabo.R;
import com.kaerenabo.activities.HomeActivity;
import com.kaerenabo.imagepicker.ImagePicker;
import com.kaerenabo.imagepicker.ImagePickerCallback;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

public class AddPostFragment extends Fragment implements View.OnClickListener, ImagePickerCallback {
    private EditText etPhoto;
    private EditText etDesc;
    private FirebaseFirestore db;
    private ProgressDialog pDialog;
    ImagePicker imagePicker;
    String folderName = "Kaerenabo";
    private FirebaseStorage storage;
    private StorageReference imagesRef;
    private StorageReference storageRef;
    private String imagePath;
    private String eventType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            eventType = bundle.getString(Constant.ARG_EVENT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        init(view);
        return view;
    }

    private void init(View view){
        ((HomeActivity)getActivity()).setBackNavigationIcon();
        storage = FirebaseStorage.getInstance(Constant.BUCKET_REF);
        storageRef = storage.getReference();
        imagesRef = storageRef.child(Constant.IMAGE_REF);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        db = FirebaseFirestore.getInstance();
        etPhoto = view.findViewById(R.id.et_photo);
        etDesc = view.findViewById(R.id.et_desc);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        etPhoto.setOnClickListener(this);
        imagePicker = new ImagePicker(getActivity(), this);
    }

    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.title_send_post));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_photo:
                Utils.hideKeyboard(getActivity());
                imagePicker.showImagePickerDialog(null, folderName, this, false);
                break;
            case R.id.btn_submit:
                Utils.hideKeyboard(getActivity());
                if(validateForm()){
                    if(imagePath !=null && !imagePath.equals(""))
                        uploadFile();
                    else
                        submitPost(null);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImagePicker.REQUEST_IMAGE_CAMERA || requestCode == ImagePicker.REQUEST_IMAGE_GALLERY){
            imagePicker.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onImagePickerResult(String filePath, int requestCode) {
        imagePath = filePath;
    }

    public String uploadFile(){
        try{
            pDialog.show();
            Uri file = Uri.fromFile(new File(imagePath));
            StorageReference riversRef = storageRef.child(Constant.IMAGE_REF+"/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    pDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    submitPost(downloadUrl.getLastPathSegment());
                }
            });

        }
        catch (Exception e){
            pDialog.dismiss();
        }
        return null;
    }

    public void submitPost(String url){
        try {
            if(!pDialog.isShowing())
                pDialog.show();
            JsonObject fbJson = new Gson().fromJson(Utils.getStringFromPref(getActivity(), Constant.USER_INFO), JsonObject.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", fbJson.get("name").getAsString());
            map.put("userID", fbJson.get("id").getAsString());
            map.put("postType", Constant.IMAGE_POST);
            map.put("postedToGroup", eventType);
            map.put("postDescription", etDesc.getText().toString().trim());
            map.put("postDate", Calendar.getInstance().getTimeInMillis());
            if(url!=null && !url.equals(""))
                map.put("postImageRefrence", url);
            db.collection("posts")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(), getString(R.string.post_created_success), Toast.LENGTH_LONG).show();
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

    private boolean validateForm(){
        if(etDesc.getText().toString().trim().equals("")){
            showErrorToast();
            return false;
        }
        return true;
    }
    private void showErrorToast(){
        Toast.makeText(getActivity(), "Please enter values in all fields",Toast.LENGTH_LONG).show();
    }
}

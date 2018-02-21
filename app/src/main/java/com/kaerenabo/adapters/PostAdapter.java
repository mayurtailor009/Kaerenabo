package com.kaerenabo.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaerenabo.R;
import com.kaerenabo.interfaces.OnItemClickListenerRecycler;
import com.kaerenabo.models.PostDTO;
import com.kaerenabo.utilities.Constant;
import com.kaerenabo.utilities.Utils;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PostDTO> list;
    StorageReference storageReference;
    FirebaseStorage storage;
    /**
     * The Item click listener.
     */
    OnItemClickListenerRecycler itemClickListener;
    public PostAdapter(Context context, ArrayList<PostDTO> list){
        this.context= context;
        this.list = list;
        storage = FirebaseStorage.getInstance(Constant.BUCKET_REF);
        storageReference= storage.getReference();
    }


    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvPostedBy;
        private TextView tvPostDate;
        private ImageView ivProfilePic;
        private ImageView ivPostPic;
        public PostViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tv_post_by);
            tvPostDate = (TextView) itemView.findViewById(R.id.tv_post_date);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.iv_profile_pic);
            ivPostPic = (ImageView) itemView.findViewById(R.id.iv_post);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void SetClickListner(OnItemClickListenerRecycler itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PostViewHolder postViewHolder = (PostViewHolder) holder;
        PostDTO postDTO = list.get(position);

        postViewHolder.tvTitle.setText(postDTO.getPostDescription());
        postViewHolder.tvPostedBy.setText(postDTO.getUserName());
        postViewHolder.tvPostDate.setText(Utils.convertTimestampToDate(postDTO.getPostDate()));
        if(postDTO.getPostImageRefrence()!=null && !postDTO.getPostImageRefrence().equals("")){
            postViewHolder.ivPostPic.setVisibility(View.VISIBLE);
            StorageReference imgRef = storageReference.child(postDTO.getPostImageRefrence());
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(postViewHolder.ivPostPic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }else{
            postViewHolder.ivPostPic.setVisibility(View.GONE);
        }
        String fbImage = "https://graph.facebook.com/v2.11/"+postDTO.getUserID()+"/picture?type=normal";
        Glide.with(context).load(fbImage).apply(RequestOptions.circleCropTransform()).into(postViewHolder.ivProfilePic);
    }

}
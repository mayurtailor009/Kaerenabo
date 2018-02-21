package com.kaerenabo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kaerenabo.R;
import com.kaerenabo.interfaces.OnItemClickListenerRecycler;
import com.kaerenabo.models.UserDTO;
import com.kaerenabo.utilities.Constant;

import java.util.ArrayList;

public class GroupMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<UserDTO> list;
    /**
     * The Item click listener.
     */
    OnItemClickListenerRecycler itemClickListener;
    private String eventType;
    public GroupMemberAdapter(Context context, ArrayList<UserDTO> list, String eventType){
        this.context= context;
        this.list = new ArrayList<UserDTO>(list);
        this.eventType = eventType;
    }


    class GroupMemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView tvName;
        private ImageView ivProfilePic;
        private TextView tvAdmin;
        public GroupMemberViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAdmin = (TextView) itemView.findViewById(R.id.tv_admin);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
            return false;
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
                .inflate(R.layout.item_group_member, parent, false);
        return new GroupMemberViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupMemberViewHolder groupMemberViewHolder = (GroupMemberViewHolder) holder;
        UserDTO dto = list.get(position);
        groupMemberViewHolder.tvName.setText(dto.getName());
        String fbImage = "https://graph.facebook.com/v2.11/"+dto.getUserID()+"/picture?type=normal";
        Glide.with(context).load(fbImage).apply(RequestOptions.circleCropTransform()).into(groupMemberViewHolder.ivProfilePic);

        if (eventType.equals(Constant.HOME_GROUP)) {
            if(dto.getIsAdmin()){
                groupMemberViewHolder.tvAdmin.setVisibility(View.VISIBLE);
            }else{
                groupMemberViewHolder.tvAdmin.setVisibility(View.INVISIBLE);
            }
        }else{
            groupMemberViewHolder.tvAdmin.setVisibility(View.GONE);
        }
    }

}
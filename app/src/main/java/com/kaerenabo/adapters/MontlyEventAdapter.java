package com.kaerenabo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kaerenabo.R;
import com.kaerenabo.models.PostDTO;

import java.util.ArrayList;


public class MontlyEventAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PostDTO> eventList;

    /**
     * Instantiates a new Montly event adapter.
     *
     * @param context   the context
     * @param eventList the event list
     */
    public MontlyEventAdapter(Context context, ArrayList<PostDTO> eventList) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.eventList = eventList;
        if (this.eventList == null)
            this.eventList = new ArrayList<PostDTO>();
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_montly_event, parent, false);
        }

        PostDTO postDTO = eventList.get(position);

        ((TextView) row.findViewById(R.id.tv_title)).setText(postDTO.getPostDescription());
        ((TextView) row.findViewById(R.id.tv_name)).setText(postDTO.getName());
        String fbImage = "https://graph.facebook.com/v2.11/"+postDTO.getUserID()+"/picture?type=normal";
        ImageView ivProfilePic = row.findViewById(R.id.iv_profile_pic);
        Glide.with(context).load(fbImage).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
        return row;
    }

    /**
     * Gets event list.
     *
     * @return the event list
     */
    public ArrayList<PostDTO> getEventList() {
        return eventList;
    }
}

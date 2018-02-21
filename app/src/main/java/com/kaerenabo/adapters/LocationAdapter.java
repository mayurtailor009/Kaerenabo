package com.kaerenabo.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaerenabo.R;
import com.kaerenabo.interfaces.OnItemClickListenerRecycler;
import com.kaerenabo.models.LocationDTO;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<LocationDTO> list;
    /**
     * The Item click listener.
     */
    OnItemClickListenerRecycler itemClickListener;
    public LocationAdapter(Context context, ArrayList<LocationDTO> list){
        this.context= context;
        this.list = new ArrayList<LocationDTO>(list);
    }


    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvAddress;
        public LocationViewHolder(View itemView) {
            super(itemView);

            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
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
                .inflate(R.layout.item_address, parent, false);
        return new LocationViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
        LocationDTO dto = list.get(position);
        locationViewHolder.tvAddress.setText(dto.getTekst());

    }

}
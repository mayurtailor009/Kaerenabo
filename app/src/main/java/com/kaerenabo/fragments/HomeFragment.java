package com.kaerenabo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kaerenabo.R;
import com.kaerenabo.activities.HomeActivity;

public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init();

        return view;
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
                if(item.getTitle() == getString( R.string.menu_event)){
                    item.setIcon(R.drawable.close_icon);
                    item.setTitle(R.string.menu_close);
                }
                else{
                    item.setIcon(R.drawable.calender_icon);
                    item.setTitle(R.string.menu_event);
                }
                ((HomeActivity)getActivity()).showHideEvent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        ((HomeActivity)getActivity()).showNavigationIcon();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.app_name));
    }
}

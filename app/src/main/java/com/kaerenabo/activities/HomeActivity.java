package com.kaerenabo.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kaerenabo.R;
import com.kaerenabo.fragments.AddEventFragment;
import com.kaerenabo.fragments.AddPostFragment;
import com.kaerenabo.fragments.NeighbourFragment;
import com.kaerenabo.fragments.ProfileFragment;
import com.kaerenabo.utilities.Constant;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    Toolbar myToolbar;
    private TextView tvTitle;
    private View viewEvent;
    private int selectedPosition;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        Bundle bundle = null;
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                selectedPosition = 0;
                                selectedFragment = new NeighbourFragment();
                                bundle = new Bundle();
                                bundle.putString(Constant.ARG_EVENT_TYPE, Constant.HOME_GROUP);
                                selectedFragment.setArguments(bundle);
                                break;
                            case R.id.action_neighbour:
                                selectedPosition = 1;
                                selectedFragment = new NeighbourFragment();
                                bundle = new Bundle();
                                bundle.putString(Constant.ARG_EVENT_TYPE, Constant.NEIGHBOUR_GROUP);
                                selectedFragment.setArguments(bundle);
                                break;
                            case R.id.action_profile:
                                selectedPosition = 2;
                                selectedFragment = new ProfileFragment();
                                break;
                        }
                        clearBackStack();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        selectedPosition = 0;
        NeighbourFragment selectedFragment = new NeighbourFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ARG_EVENT_TYPE, Constant.HOME_GROUP);
        selectedFragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout, selectedFragment);
        bottomNavigationView.getMenu().getItem(0).setEnabled(true);
        transaction.commit();
    }
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
    private void init(){

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        tvTitle = (TextView) myToolbar.findViewById(R.id.tv_title);
        setSupportActionBar(myToolbar);

        viewEvent = findViewById(R.id.rl_post);
        viewEvent.setOnClickListener(this);

        findViewById(R.id.btn_create_event).setOnClickListener(this);
        findViewById(R.id.btn_send_post).setOnClickListener(this);
    }

    public void showHideEvent(){

        if(viewEvent.getVisibility() == View.GONE){
            viewEvent.setVisibility(View.VISIBLE);
        }
        else{
            viewEvent.setVisibility(View.GONE);
        }
    }

    public void setToolbarTitle(String title){
        tvTitle.setText(title);
    }

    public void showNavigationIcon(){
        myToolbar.setNavigationIcon(R.drawable.plus_icon);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///
                showHideEvent();
            }
        });
    }

    public void hideNavigationIcon(){
        myToolbar.setNavigationIcon(null);
    }

    public void setBackNavigationIcon(){
        myToolbar.setNavigationIcon(R.drawable.back_icon);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Fragment fragment;
        Bundle bundle;
        switch (arg0.getId()){
            case R.id.btn_create_event:
                fragment = new AddEventFragment();
                bundle = new Bundle();
                if(selectedPosition == 0)
                    bundle.putString(Constant.ARG_EVENT_TYPE, Constant.HOME_GROUP);
                else
                    bundle.putString(Constant.ARG_EVENT_TYPE, Constant.NEIGHBOUR_GROUP);
                fragment.setArguments(bundle);
                addFragment(fragment);
                showHideEvent();
                break;
            case R.id.btn_send_post:
                fragment = new AddPostFragment();
                bundle = new Bundle();
                if(selectedPosition == 0)
                    bundle.putString(Constant.ARG_EVENT_TYPE, Constant.HOME_GROUP);
                else
                    bundle.putString(Constant.ARG_EVENT_TYPE, Constant.NEIGHBOUR_GROUP);
                fragment.setArguments(bundle);
                addFragment(fragment);
                showHideEvent();
                break;
            case R.id.rl_post:
                showHideEvent();
                break;
        }
    }

    public void addFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }
}

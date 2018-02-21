package com.kaerenabo.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mayur.tailor on 21-01-2018.
 */

public class GroupDTO implements Serializable{

    private String FCMtoken;
    private String address;
    private double addressLatitude;
    private double addressLongitude;
    private ArrayList<String> blockedUserIDs;
    private ArrayList<String> homeUserIDs;
    private ArrayList<String> nearByUserIDs;
    private ArrayList<String> likes;
    private String userID;

    public ArrayList<String> getNearByUserIDs() {
        return nearByUserIDs;
    }

    public void setNearByUserIDs(ArrayList<String> nearByUserIDs) {
        this.nearByUserIDs = nearByUserIDs;
    }

    public String getFCMtoken() {
        return FCMtoken;
    }

    public void setFCMtoken(String FCMtoken) {
        this.FCMtoken = FCMtoken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(double addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public double getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(double addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public ArrayList<String> getBlockedUserIDs() {
        return blockedUserIDs;
    }

    public void setBlockedUserIDs(ArrayList<String> blockedUserIDs) {
        this.blockedUserIDs = blockedUserIDs;
    }

    public ArrayList<String> getHomeUserIDs() {
        return homeUserIDs;
    }

    public void setHomeUserIDs(ArrayList<String> homeUserIDs) {
        this.homeUserIDs = homeUserIDs;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

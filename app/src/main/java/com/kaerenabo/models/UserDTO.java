package com.kaerenabo.models;

import java.util.ArrayList;



public class UserDTO {
    private String userID;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String addressHref;
    private String deviceType;
    private String accessToken;
    private String FCMtoken;
    private ArrayList<String> likes;
    private double latitude;
    private double longitude;
    private double userLatitude;
    private double userLongitude;
    private boolean isAdmin;
    private boolean isUserAccepted;
    private String currentState;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressHref() {
        return addressHref;
    }

    public void setAddressHref(String addressHref) {
        this.addressHref = addressHref;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFCMtoken() {
        return FCMtoken;
    }

    public void setFCMtoken(String FCMtoken) {
        this.FCMtoken = FCMtoken;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }
    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
    public boolean getIsUserAccepted(){
        return isUserAccepted;
    }
    public void setIsUserAccepted(boolean isUserAccepted){
        this.isUserAccepted = isUserAccepted;
    }
    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}

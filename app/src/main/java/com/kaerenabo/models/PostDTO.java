package com.kaerenabo.models;

import java.util.Date;


public class PostDTO {
    private String userID;
    private String documentID;
    private String userName;
    private String postType;
    private String postedToGroup;
    private String postDescription;
    private String postImageRefrence;
    private long postDate;
    private Date eventStartDate;
    private Date eventEndDate;
    private String postImage;
    private String name;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostedToGroup() {
        return postedToGroup;
    }

    public void setPostedToGroup(String postedToGroup) {
        this.postedToGroup = postedToGroup;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImageRefrence() {
        return postImageRefrence;
    }

    public void setPostImageRefrence(String postImageRefrence) {
        this.postImageRefrence = postImageRefrence;
    }

    public long getPostDate() {
        return postDate;
    }

    public void setPostDate(long postDate) {
        this.postDate = postDate;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

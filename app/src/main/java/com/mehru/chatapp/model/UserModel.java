package com.mehru.chatapp.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phoneNumber;
    private String userName ;
    private Timestamp createdStampTime ;
    private String userId;
    private String fcmToken;

    public UserModel() {
    }

    public UserModel(String phoneNumber, String userName, Timestamp createdStampTime,String userId) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.createdStampTime = createdStampTime;
        this.userId = userId ;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedStampTime() {
        return createdStampTime;
    }

    public void setCreatedStampTime(Timestamp createdStampTime) {
        this.createdStampTime = createdStampTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

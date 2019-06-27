package com.mytodolist.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String uid;
    private String firstName;
    private String LastName;
    private String email;
    private String password;
    private Timestamp createdDate;
    private String fcm_token;


    public UserModel() {
    }

    public UserModel(String uid, String firstName, String lastName, String email, String password, String push_token, Timestamp createdDate) {
        this.uid = uid;
        this.firstName = firstName;
        LastName = lastName;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.fcm_token = push_token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}

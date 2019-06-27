package com.mytodolist.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class CommentModel implements Serializable {

    private String comment_id;
    private String user_id;
    private String comment;
    private String firstName;
    private String LastName;
    private Timestamp created_time;

    public CommentModel() {
    }

    public CommentModel(String comment_id, String user_id, String comment, String firstName, String lastName, Timestamp created_time) {
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.comment = comment;
        this.firstName = firstName;
        LastName = lastName;
        this.created_time = created_time;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public Timestamp getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }
}

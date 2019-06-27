package com.mytodolist.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class ToDoModel implements Serializable {


    private String todo_id;
    private String uid;
    private String title;
    private String description;
    private Timestamp time;
    private Timestamp created_date;
    private boolean isCompleted;

    //private List<CommentModel> comments;

    public ToDoModel() {
    }

    public ToDoModel(String todo_id, String uid, String title, String description, Timestamp time, Timestamp created_date,boolean isCompleted) {
        this.todo_id = todo_id;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.time = time;
        this.created_date = created_date;
        this.isCompleted = isCompleted;
    }

    public String getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(String todo_id) {
        this.todo_id = todo_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

   /* public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }*/

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

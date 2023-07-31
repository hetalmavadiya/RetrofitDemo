package com.example.retrofit.Model;

public class TodoModel {
    private int userId;
    private int id;
    private String title;
    private boolean completed;
    private boolean isChecked;
    public TodoModel(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

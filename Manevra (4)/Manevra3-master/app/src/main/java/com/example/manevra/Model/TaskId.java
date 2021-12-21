package com.example.manevra.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class TaskId {
    @Exclude
    public String TaskId;
    public <T extends TaskId > T withId(@NonNull final String id){
        this.TaskId=id;
        return (T)this;
    }
}

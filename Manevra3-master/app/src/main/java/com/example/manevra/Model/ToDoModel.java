package com.example.manevra.Model;

public class ToDoModel extends  TaskId  {
    private String task,due;
    private int status;

    public String getTask() {
        return task;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}

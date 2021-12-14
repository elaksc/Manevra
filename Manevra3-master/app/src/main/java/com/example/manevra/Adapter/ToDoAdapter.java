package com.example.manevra.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manevra.Model.AddNewTask;
import com.example.manevra.Model.ToDoModel;
import com.example.manevra.R;
import com.example.manevra.ToDoListActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter <ToDoAdapter.MyViewHolder>{
    private List<ToDoModel> toDoList;
    private ToDoListActivity activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(ToDoListActivity toDoListActivity,List<ToDoModel>toDoList){
        this.toDoList=toDoList;
        activity=toDoListActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.each_task,parent,false);
        firestore =FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }


    public void deleteTask(int position){
        ToDoModel toDoModel=toDoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        toDoList.remove(position);
        notifyItemRemoved(position);
    }

  public Context getContext(){
      return activity;
  }
    public void editTask(int position){
        ToDoModel toDoModel =toDoList.get(position);

        Bundle bundle =new Bundle();
        bundle.putString("task",toDoModel.getTask());
        bundle.putString("due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask =new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(),addNewTask.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel =toDoList.get(position);
        holder.mCheckbox.setText(toDoModel.getTask());
        holder.mDueDateTv.setText(toDoModel.getDue());

        holder.mCheckbox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firestore.collection("Görev").document(toDoModel.TaskId).update("Durum",1);
                }else{
                    firestore.collection("Görev").document(toDoModel.TaskId).update("Durum",0);
                }
            }
        });
    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv=itemView.findViewById(R.id.due_date_tv);
            mCheckbox=itemView.findViewById(R.id.mccheckbox);
        }
    }
}

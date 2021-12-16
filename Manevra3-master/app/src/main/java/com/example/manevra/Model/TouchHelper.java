package com.example.manevra.Model;


import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manevra.Adapter.ToDoAdapter;
import com.example.manevra.R;
import com.example.manevra.ToDoListActivity;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TouchHelper  extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;
    public TouchHelper(ToDoAdapter adapter ) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position=viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getContext());
            builder
                    .setTitle("Silmek İstediğinize Emin Misiniz?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.deleteTask(position);
                        }
                    }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog alertDialog =builder.create();
            alertDialog.show();
        }else {
            adapter.editTask(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit_24)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext() , R.color.purple_700))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

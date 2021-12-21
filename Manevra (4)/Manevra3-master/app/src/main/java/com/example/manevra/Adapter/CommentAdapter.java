package com.example.manevra.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manevra.Model.CommentModel;
import com.example.manevra.Model.GonderilenKitap;
import com.example.manevra.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> liste;
    Context mContext;
    private CommentAdapter.OnItemClickListener mListener;

    public CommentAdapter(List<CommentModel> liste, Context mContext) {
        this.liste = liste;
        this.mContext = mContext;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new CommentAdapter.ViewHolder(itemView, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.message.setText(liste.get(position).getText());
        holder.date.setText(dateTime(liste.get(position).getDate()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView message, date;

        public ViewHolder(@NonNull View itemView, CommentAdapter.OnItemClickListener mListener) {
            super(itemView);

            message = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.message_time);

        }
    }
    public String dateTime(Long t) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String _date = formatter.format(calendar.getTime());
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(_date);
            time = prettyTime.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}

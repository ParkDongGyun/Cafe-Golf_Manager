package com.sbsj.cafegolf_master.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.cafegolf_master.OnItemClickListener;
import com.sbsj.cafegolf_master.R;
import com.sbsj.cafegolf_master.network.LessonInfoColumn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<LessonInfoColumn> datalist;

    private SimpleDateFormat format = new SimpleDateFormat("HH : mm");

    public LessonAdapter (Context context, ArrayList<LessonInfoColumn> datalist, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = onItemClickListener;
    }

    public void setDatalist(ArrayList<LessonInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<LessonInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LessonAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {
        LessonInfoColumn lessoninfo = datalist.get(position);

        holder.tv_lesson_id.setText(String.format(context.getString(R.string.idformat), position + 1));
        String hour_before = Integer.toString(lessoninfo.getHour_before());
        if(lessoninfo.getHour_before() < 10)
            hour_before = "0" + hour_before;
        String minute_before = Integer.toString(lessoninfo.getMinute_before());
        if(lessoninfo.getMinute_before() < 10)
            minute_before = "0" + minute_before;
        String time_before = String.format(context.getString(R.string.timeformat), hour_before, minute_before);
        holder.tv_lesson_before.setText(time_before);

        String hour_after = Integer.toString(lessoninfo.getHour_before()+1);
        if(lessoninfo.getHour_before() < 9)
            hour_after = "0" + hour_after;
        String minute_after = Integer.toString(lessoninfo.getMinute_before());
        if(lessoninfo.getMinute_before() < 10)
            minute_after = "0" + minute_after;
        String time_after = String.format(context.getString(R.string.timeformat), hour_after, minute_after);
        holder.tv_lesson_after.setText(time_after);

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public LessonInfoColumn getItem(int position) {
        return datalist.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_lesson_id;
        RelativeLayout rl_member;
        TextView tv_lesson_before;
        TextView tv_lesson_after;
        Button btn_lesson_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_lesson_id = itemView.findViewById(R.id.tv_lesson_id);
            rl_member = itemView.findViewById(R.id.rl_lesson);
            tv_lesson_before = itemView.findViewById(R.id.tv_lesson_before);
            tv_lesson_after = itemView.findViewById(R.id.tv_lesson_after);
            btn_lesson_edit = itemView.findViewById(R.id.btn_lesson_edit);

            rl_member.setOnLongClickListener(this);
            btn_lesson_edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, getAdapterPosition());

            return true;
        }
    }
}

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
import com.sbsj.cafegolf_master.network.MemberInfoColumn;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<MemberInfoColumn> datalist;

    public MemberAdapter (Context context, ArrayList<MemberInfoColumn> datalist, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = onItemClickListener;
    }

    public void setDatalist(ArrayList<MemberInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<MemberInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder holder, int position) {
        MemberInfoColumn memberinfo = datalist.get(position);
        holder.tv_member_id.setText(String.format(context.getString(R.string.idformat), position + 1));
        if(!memberinfo.getName().isEmpty()) {
            holder.tv_member_name.setText(memberinfo.getName());
        }
        holder.btn_member_lesson.setText(String.format(context.getString(R.string.lessoncountformat),memberinfo.getLesson_count()));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public MemberInfoColumn getItem(int position) {
        return datalist.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        RelativeLayout rl_member;
        TextView tv_member_id;
        TextView tv_member_name;
        Button btn_member_lesson;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_member = itemView.findViewById(R.id.rl_member);
            tv_member_id = itemView.findViewById(R.id.tv_member_id);
            tv_member_name = itemView.findViewById(R.id.tv_member_name);
            btn_member_lesson = itemView.findViewById(R.id.btn_lessoncount);

            rl_member.setOnLongClickListener(this);
            btn_member_lesson.setOnClickListener(this);
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

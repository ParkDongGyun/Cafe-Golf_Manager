package com.sbsj.cafegolf_master.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.sbsj.cafegolf_master.network.BookingInfoColumn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<BookingInfoColumn> datalist;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat stringFormat = new SimpleDateFormat("yy/MM/dd hh:mm");

    public BookingAdapter (Context context, ArrayList<BookingInfoColumn> datalist, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = onItemClickListener;
    }

    public void setDatalist(ArrayList<BookingInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<BookingInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        holder.tv_booking_id.setText(String.format(context.getString(R.string.idformat), position + 1));
        try {
            Date date = dateFormat.parse(datalist.get(position).getBook_date());
            String date_string = stringFormat.format(date);
            holder.tv_booking.setText(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("BookingAdapter", "parse error : " + e.getMessage());
            holder.tv_booking.setText("Error Date");
        }
        holder.tv_booking_mem.setText(datalist.get(position).getMember_name());
        if(datalist.get(position).getIsapproved() > 0) {
            holder.btn_booking.setText("승인 완료");
            holder.btn_booking.setBackgroundColor(context.getColor(R.color.gray));
        } else {
            holder.btn_booking.setText("승인 대기");
            holder.btn_booking.setBackgroundColor(context.getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public BookingInfoColumn getItem(int position) {
        return datalist.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_booking_id;
        RelativeLayout rl_booking;
        TextView tv_booking;
        TextView tv_booking_mem;
        Button btn_booking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_booking_id = itemView.findViewById(R.id.tv_booking_id);
            rl_booking = itemView.findViewById(R.id.rl_booking);
            tv_booking = itemView.findViewById(R.id.tv_booking);
            tv_booking_mem = itemView.findViewById(R.id.tv_booking_mem);
            btn_booking = itemView.findViewById(R.id.btn_booking);

            rl_booking.setOnLongClickListener(this);
            btn_booking.setOnClickListener(this);
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

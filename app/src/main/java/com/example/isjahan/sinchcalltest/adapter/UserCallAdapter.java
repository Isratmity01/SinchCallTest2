package com.example.isjahan.sinchcalltest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isjahan.sinchcalltest.R;
import com.example.isjahan.sinchcalltest.model.CallDetails;

import java.util.ArrayList;
import java.util.List;



public class UserCallAdapter extends RecyclerView.Adapter<UserCallAdapter.MyViewHolder> {

    private List<CallDetails> callDetailses;
    Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.person_name);
            genre = (TextView) view.findViewById(R.id.calltype);
           // year = (TextView) view.findViewById(R.id.year);
        }
    }


    public UserCallAdapter(Context context, ArrayList<CallDetails>callDetailses2)
    {
        this.mContext=context;
        this.callDetailses=callDetailses2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caller_list_item, parent, false);

        return new MyViewHolder(itemView);
    }


    public void updateList(ArrayList<CallDetails>data) {
        callDetailses = data;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CallDetails callDetails = callDetailses.get(position);
        holder.title.setText(callDetails.getCallingTo());
        holder.genre.setText(callDetails.getCallType());
        //holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return callDetailses.size();
    }
}

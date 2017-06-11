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

import de.greenrobot.event.EventBus;


public class UserCallAdapter extends RecyclerView.Adapter<UserCallAdapter.MyViewHolder> {

    private ArrayList<CallDetails> callDetailses;
    Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView title, genre;
        ImageView callicon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.person_name);
            genre = (TextView) view.findViewById(R.id.calltype);
            callicon=(ImageView)view.findViewById(R.id.person_call);
            view.setTag(view);
           // view.setOnClickListener((View.OnClickListener) this);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CallDetails callDetails = callDetailses.get(position);
        holder.title.setText(callDetails.getCallingTo());
        holder.genre.setText(callDetails.getCallType());
        holder.callicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CallDetails(callDetailses.get(position).getCallingTo(),
                        callDetailses.get(position).getCallInTime(),callDetailses.get(position).getCallType()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return callDetailses.size();
    }
}

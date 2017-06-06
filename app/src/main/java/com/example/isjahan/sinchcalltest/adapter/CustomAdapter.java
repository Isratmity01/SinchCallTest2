package com.example.isjahan.sinchcalltest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isjahan.sinchcalltest.R;
import com.example.isjahan.sinchcalltest.model.UserCalls;

import java.util.ArrayList;

/**
 * Created by HP on 6/7/2017.
 */

public class CustomAdapter extends BaseAdapter implements View.OnClickListener{

        ArrayList<String> result;
        Context context;
        int [] imageId;
        private static LayoutInflater inflater=null;
        public CustomAdapter(Context context2, ArrayList<String> prgmNameList) {
            // TODO Auto-generated constructor stub
            result=prgmNameList;
            context=context2;

            inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tv;
            ImageView img;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.caller_list_item, null);
            holder.tv=(TextView) rowView.findViewById(R.id.textView2);
            holder.img=(ImageView) rowView.findViewById(R.id.imageclick);
            holder.tv.setText(result.get(position));

          rowView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // Do something
        }
    });
            return rowView;
        }
    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        UserCalls dataModel=(UserCalls)object;

        switch (v.getId())
        {
            case R.id.textView2:
                Toast.makeText(context, "Release date " +dataModel.getUserName(), Toast.LENGTH_LONG).show();
                break;
        }
    }

}

package com.example.isjahan.sinchcalltest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.isjahan.sinchcalltest.LogActivity;
import com.example.isjahan.sinchcalltest.R;
import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.model.CallDetails;

import java.util.ArrayList;


public class Fragment_RecentCalls extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    LinearLayout linearLayout;
    private ArrayList<CallDetails> userCallsArrayList=new ArrayList<>();
    private ArrayList<String >names=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    public Fragment_RecentCalls() {
        // Required empty public constructor
    }
    public static Fragment_RecentCalls newInstance() {
        Fragment_RecentCalls f = new Fragment_RecentCalls();
        return f;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_recentcalls, container, false);

    bindViews(fragmentView);
        return fragmentView;
}

    private void bindViews(View view) {

        linearLayout=(LinearLayout)view.findViewById(R.id.listholder);
        listView=(ListView)view.findViewById(R.id.simpleListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                // ListView Clicked item value
               String remoteuserName = (String) listView.getItemAtPosition(position);

                ((LogActivity)getActivity()).callButtonClicked(remoteuserName);
                // Show Alert

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }
    public void init()
    {
        populatelistview();
    }
    public void populatelistview()
    {

        final DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        userCallsArrayList=dbHelper.getAllUserLog();
        for(int i=0;i<userCallsArrayList.size();i++)
        {
            names.add(userCallsArrayList.get(i).getCallingTo() + userCallsArrayList.get(i).getCallType());
        }
        if(names.size()>0)linearLayout.setVisibility(View.VISIBLE);
        arrayAdapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                names );

        listView.setAdapter(arrayAdapter);

    }

    public void refreshlistview()
    {

        final DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        names.clear();
        userCallsArrayList=dbHelper.getAllUserLog();
        for(int i=0;i<userCallsArrayList.size();i++)
        {
            names.add(userCallsArrayList.get(i).getCallingTo() + userCallsArrayList.get(i).getCallType());
        }
        if(names.size()>0)linearLayout.setVisibility(View.VISIBLE);
        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        listView.clearFocus();

        arrayAdapter.notifyDataSetChanged();


    }
}
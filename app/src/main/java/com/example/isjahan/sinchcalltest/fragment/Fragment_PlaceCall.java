package com.example.isjahan.sinchcalltest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.isjahan.sinchcalltest.LogActivity;
import com.example.isjahan.sinchcalltest.R;

public class Fragment_PlaceCall extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mCallButton;
    private EditText mCallName;

    public Fragment_PlaceCall() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_blank2, container, false);

        bindViews(fragmentView);
        return fragmentView;
    }
    private void bindViews(View view) {

        mCallName = (EditText) view.findViewById(R.id.callName);
        mCallButton = (Button) view.findViewById(R.id.callButton);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((LogActivity)getActivity()).callButtonClicked(mCallName.getText().toString().trim());
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

    }

}

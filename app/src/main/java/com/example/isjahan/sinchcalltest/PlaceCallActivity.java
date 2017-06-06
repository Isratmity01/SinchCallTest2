package com.example.isjahan.sinchcalltest;

import com.example.isjahan.sinchcalltest.controllers.RealmController;
import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.model.UserCalls;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;

public class PlaceCallActivity extends BaseActivity {

    private Button mCallButton,mOtherCallButton;
    private Realm realm;
    private EditText mCallName;
    private ListView listView;
    TextView userName;
    LinearLayout linearLayout;
    private ArrayList<UserCalls>userCallsArrayList=new ArrayList<>();
    private ArrayList<String >names=new ArrayList<>();
    String myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent=getIntent();
        myself= intent.getStringExtra("Name");
        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);
        mOtherCallButton = (Button) findViewById(R.id.callButton2);
        mOtherCallButton.setEnabled(true);
        mOtherCallButton.setOnClickListener(buttonClickListener);
        listView=(ListView)findViewById(R.id.simpleListView);

        linearLayout=(LinearLayout)findViewById(R.id.numberInputLayout);
        populatelistview();
    }

    @Override
    protected void onServiceConnected() {
         userName = (TextView) findViewById(R.id.loggedInName);
        String get=getSinchServiceInterface().getUserName();
        if(TextUtils.isEmpty(get))
        {   getSinchServiceInterface().startClient(myself);
            userName.setText(myself);

        }

       else userName.setText(get);
        mCallButton.setEnabled(true);
    }
public void populatelistview()
{
    final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
    userCallsArrayList=dbHelper.getAllUser();
    for(int i=0;i<userCallsArrayList.size();i++)
    {
        names.add(userCallsArrayList.get(i).getUserName());
    }
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            names );

    listView.setAdapter(arrayAdapter);

}
    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {


        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Call call = getSinchServiceInterface().callUser(userName);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallScreenActivity.class);

            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked();
                    break;
                case R.id.callButton2:
                    linearLayout.setVisibility(View.VISIBLE);
                    break;


            }
        }
    };

    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        populatelistview();

    }
}

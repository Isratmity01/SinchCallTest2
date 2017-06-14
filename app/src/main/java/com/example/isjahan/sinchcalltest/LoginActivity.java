package com.example.isjahan.sinchcalltest;

import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.model.UserCalls;

import com.sinch.android.rtc.SinchError;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button mLoginButton;
    private EditText mLoginName;
    private ProgressDialog mSpinner;
    private String userName , uname;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 175;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.login);

            mLoginName = (EditText) findViewById(R.id.loginName);

            mLoginButton = (Button) findViewById(R.id.loginButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermissions();
            //user is using app for the first time

        }
            mLoginButton.setEnabled(false);
            mLoginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginClicked();
                }
            });
        }


    @Override
    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "mars permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_CONTACTS);
            message += "\n to get contacts from phone.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
            message += "\nfor calling funcion.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
            message += "\nfor calling funcion.";
            //requestReadPhoneStatePermission();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            message += "\nfor calling funcion.";
            //requestReadPhoneStatePermission();
        }

        if (!permissions.isEmpty()) {
            // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        } // else: We already have permissions, so handle as normal
    }
    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked() {
         userName = mLoginName.getText().toString();


        if( isValidNumber(userName)) {
            final UserCalls user = new UserCalls(userName, 1);
            final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.addUserCall(user);

            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(userName);
                showSpinner();
            } else {

                openPlaceCallActivity();
            }
        }
        else Toast.makeText(getApplicationContext(),"Enter Number Properly",Toast.LENGTH_SHORT).show();
    }

    private void openPlaceCallActivity() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LoginName", userName);
        editor.apply();
        Intent mainActivity = new Intent(this, LogActivity.class);
        this.finish();
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
    private boolean isValidNumber(String number) {
        String phone_PATTERN = "^(?:\\+?88)?01[15-9]\\d{8}$";

        Pattern pattern = Pattern.compile(phone_PATTERN);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.MODIFY_AUDIO_SETTINGS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean recordaudio = perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
                Boolean modaudio = perms.get(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED;
                Boolean phonestate = perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
                Boolean contactstate = perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
                if (recordaudio && modaudio&& phonestate &&contactstate) {
                    // All Permissions Granted
                   return;
                    //Toast.makeText(PhoneRegActivity.this, "Thanks for permission", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}

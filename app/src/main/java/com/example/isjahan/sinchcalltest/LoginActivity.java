package com.example.isjahan.sinchcalltest;

import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.model.UserCalls;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sinch.android.rtc.SinchError;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button mLoginButton;
    private EditText mLoginName;
    private ProgressDialog mSpinner;
    private String userName , uname;
    Phonenumber.PhoneNumber bdNumberProto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.login);

            mLoginName = (EditText) findViewById(R.id.loginName);

            mLoginButton = (Button) findViewById(R.id.loginButton);
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

            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
                return;
            }

            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(userName);
                showSpinner();
            } else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LoginName", userName);
                editor.apply();
                openPlaceCallActivity();
            }
        }
        else Toast.makeText(getApplicationContext(),"Enter Number Properly",Toast.LENGTH_SHORT).show();
    }

    private void openPlaceCallActivity() {

        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
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
}

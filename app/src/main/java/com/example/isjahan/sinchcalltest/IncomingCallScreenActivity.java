package com.example.isjahan.sinchcalltest;

import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.model.CallDetails;
import com.example.isjahan.sinchcalltest.model.CallEnded;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.greenrobot.event.EventBus;

public class IncomingCallScreenActivity extends BaseActivity {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private AudioPlayer mAudioPlayer;
    String calltype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_incoming);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Intent intent=getIntent();
         calltype=intent.getStringExtra("IsIncoming");
        Button answer = (Button) findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = (Button) findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);

        mAudioPlayer = new AudioPlayer(this);
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
       switch (audioManager.getRingerMode())
        {
                case AudioManager.RINGER_MODE_NORMAL:
                    mAudioPlayer.playRingtone();
                    break;
            case AudioManager.RINGER_MODE_VIBRATE:
                mAudioPlayer.makevibrate();
                break;
        }

        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            remoteUser.setText(call.getRemoteUserId());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            try {
                call.answer();
                Intent intent = new Intent(this, CallScreenActivity.class);
                intent.putExtra(SinchService.CALL_ID, mCallId);
                this.finish();
                startActivity(intent);
            } catch (MissingPermissionException e) {
                ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
            }
        } else {
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now answer the call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            if(calltype.equals("yes"))
            {
                if(cause.toString().equals("DENIED")||cause.toString().equals("CANCELED"))
                {
                    final CallDetails user = new CallDetails(call.getRemoteUserId(),System.currentTimeMillis(),"Missed");
                    final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                    dbHelper.addUserLog(user);
                }
                else {

                    final CallDetails user = new CallDetails(call.getRemoteUserId(),System.currentTimeMillis(),"Incoming");
                    final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                    dbHelper.addUserLog(user);
                }
            }
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            EventBus.getDefault().post(new CallEnded("yes"));
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };
}

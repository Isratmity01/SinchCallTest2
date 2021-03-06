package com.example.isjahan.sinchcalltest;

import com.example.isjahan.sinchcalltest.model.CallEnded;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;


public class CallScreenActivity extends BaseActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();
    private int flag = 0;
    private int flagmic = 0;
    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;
private Button speakerButton,micButton;
    private String mCallId;
    AudioManager audioManager ;
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    ImageView imageView;
    Call call;
    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_callscreen);
        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        imageView=(CircleImageView)findViewById(R.id.profile_image) ;
        imageView.setImageResource(R.drawable.callicon);
        Button endCallButton = (Button) findViewById(R.id.hangupButton);
        speakerButton=(Button)findViewById(R.id.speakerButton);
        micButton=(Button)findViewById(R.id.muteButton);
        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(false);

        speakerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    flag = 1; // 1 => Button ON
                    speakerButton.setBackgroundResource(R.drawable.ic_speakeron);
                    Toast.makeText(CallScreenActivity.this,"start",Toast.LENGTH_SHORT).show();
                    audioManager.setSpeakerphoneOn(true);
                } else {
                    flag = 0; // 0 => Button OFF
                    speakerButton.setBackgroundResource(R.drawable.ic_speakeroff);
               //     Toast.makeText(CallScreenActivity.this,"off",Toast.LENGTH_SHORT).show();
                    audioManager.setSpeakerphoneOn(false);
                }
            }
        });
        micButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagmic == 0) {
                    flagmic = 1; // 1 => Button ON
                    micButton.setBackgroundResource(R.drawable.ic_muteon);
                  //  Toast.makeText(CallScreenActivity.this,"start",Toast.LENGTH_SHORT).show();
                    audioManager.setMicrophoneMute(true);
                } else {
                    flagmic = 0; // 0 => Button OFF
                    micButton.setBackgroundResource(R.drawable.ic_mute);
                    Toast.makeText(CallScreenActivity.this,"off",Toast.LENGTH_SHORT).show();
                    audioManager.setMicrophoneMute(false);
                }
            }
        });
    }

    @Override
    public void onServiceConnected() {
         call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            mCallerName.setText(call.getRemoteUserId());
            if(call.getState().toString().equals("INITIATING"))mCallState.setText("Calling");
            else mCallState.setText(call.getState().toString());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }
    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();//
        //CallDetails callDetails=realm.createObject(CallDetails.class);
        Call call = getSinchServiceInterface().getCall(mCallId);
        String time=TimeActivity.getCurrentTime(getApplicationContext());
        Log.d("Check", call.getCallId() + call.getRemoteUserId() + mDurationTask.toString()+ time +"Outgoing");
        /*callDetails=new CallDetails(call.getCallId(),call.getRemoteUserId(),mDurationTask.toString(),time,"Outgoing");
        realm.beginTransaction();
        realm.copyToRealm(callDetails);
        realm.commitTransaction();
        realm.close();*/
        if (call != null) {
            call.hangup();
        }
        finish();
    }
    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }
    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (call.getDetails().getDuration() == 0) {
                mCallState.setVisibility(View.VISIBLE);
                mCallDuration.setVisibility(View.GONE);
            } else {
                mCallState.setVisibility(View.GONE);
                mCallDuration.setVisibility(View.VISIBLE);
                mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
            }
        }
    }
    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            call.getDetails().getDuration();
            EventBus.getDefault().post(new CallEnded("yes"));
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            Toast.makeText(CallScreenActivity.this, endMsg, Toast.LENGTH_LONG).show();
            endCall();
        }
        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }
        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }
}

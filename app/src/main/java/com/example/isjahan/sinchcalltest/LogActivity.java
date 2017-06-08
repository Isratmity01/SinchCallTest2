package com.example.isjahan.sinchcalltest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.fragment.Fragment_RecentCalls;
import com.example.isjahan.sinchcalltest.fragment.Fragment_PlaceCall;
import com.example.isjahan.sinchcalltest.model.CallDetails;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadman.rahman on 07-Jun-17.
 */

public class LogActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab);
        Intent intent=getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        myself= intent.getStringExtra("Name");
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        adapter.addFragment(new Fragment_PlaceCall(), "Place Call");
        adapter.addFragment(new Fragment_RecentCalls(), "Recent");


        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener  (new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
               //if(position==1)adapter.notifyDataSetChanged();
                Fragment_RecentCalls fragment = (Fragment_RecentCalls) adapter.getItem(1);
                fragment.refreshlistview();
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context context2;
        public ViewPagerAdapter(FragmentManager manager,Context context) {
            super(manager);
            this.context2=context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }
        @Override
        public int getItemPosition(Object object) {
            if (mFragmentList.contains(object)) {
                return POSITION_UNCHANGED;
            }
            return POSITION_NONE;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void callButtonClicked(String remoteuserName)
    {
        if (remoteuserName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Call call = getSinchServiceInterface().callUser(remoteuserName);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            final CallDetails user = new CallDetails(remoteuserName,System.currentTimeMillis(),"outgoing");
            final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.addUserLog(user);
            Intent callScreen = new Intent(this, CallScreenActivity.class);

            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }
    }
    @Override
    protected void onServiceConnected() {

        String get=getSinchServiceInterface().getUserName();
        if(TextUtils.isEmpty(get))
        {   getSinchServiceInterface().startClient(myself);


        }

        //else userName.setText(get);
     //   mCallButton.setEnabled(true);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }
}

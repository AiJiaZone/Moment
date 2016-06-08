package com.woody.moment.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.woody.moment.MomentMonitorService;
import com.woody.moment.R;
import com.woody.moment.model.StatDataStruct;
import com.woody.moment.ui.view.CircleView;

public class SplashActivity extends AppCompatActivity {
    final private static String TAG = "SplashActivity";

    TextView mScreenOnText;
    TextView mScreenOffText;
    TextView mUserPresentText;

    private StatDataStruct mStats = StatDataStruct.getStatDataInstance();

    MomentMonitorService mMonitorService = null;

    private final int ACTION_UPDATE_COUNT = 101;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (ACTION_UPDATE_COUNT == msg.what) {
                updateView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initViews();
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MomentMonitorService.class);
        bindService(intent, mConnector, Context.BIND_AUTO_CREATE);
    }


    final private void initViews() {
        CircleView screenOnView = (CircleView) findViewById(R.id.screen_on);
        TextView screenOnHeader = (TextView) screenOnView.findViewById(R.id.id_text_header);
        screenOnHeader.setText(R.string.screen_on_header);
        mScreenOnText = (TextView) screenOnView.findViewById(R.id.id_text_content);

        CircleView screenOffView = (CircleView) findViewById(R.id.screen_off);
        TextView screenOffHeader = (TextView) screenOffView.findViewById(R.id.id_text_header);
        screenOffHeader.setText(R.string.screen_off_header);
        mScreenOffText = (TextView) screenOffView.findViewById(R.id.id_text_content);

        CircleView userPresentView = (CircleView) findViewById(R.id.user_present);
        TextView userPresentHeader = (TextView) userPresentView.findViewById(R.id.id_text_header);
        userPresentHeader.setText(R.string.user_present_header);
        mUserPresentText = (TextView) userPresentView.findViewById(R.id.id_text_content);

        postUpdateView();
    }

    final private void updateView() {
        mScreenOnText.setText(String.format(getString(R.string.counts), mStats.getScreenOnCount()));
        mScreenOffText.setText(String.format(getString(R.string.counts), mStats.getScreenOffCount()));
        mUserPresentText.setText(String.format(getString(R.string.counts), mStats.getUserPresentCount()));
    }

    final private void postUpdateView() {
        mHandler.removeMessages(ACTION_UPDATE_COUNT);
        mHandler.sendEmptyMessage(ACTION_UPDATE_COUNT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged");
        setContentView(R.layout.activity_splash);
        initViews();
    }

    @Override
    protected void onDestroy() {
        if (mConnector != null) {
            unbindService(mConnector);
            mConnector = null;
        }
        super.onDestroy();
    }


    private ServiceConnection mConnector = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MomentMonitorService.MonitorBinder binder = (MomentMonitorService.MonitorBinder) service;
            mMonitorService = binder.getService();
            if (mMonitorService != null) {
                mMonitorService.setCallBack(mCallBack);
                postUpdateView();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    final MomentMonitorService.UiCallback mCallBack = new MomentMonitorService.UiCallback() {
        @Override
        public void onUpdate() {
            postUpdateView();
        }
    };
}

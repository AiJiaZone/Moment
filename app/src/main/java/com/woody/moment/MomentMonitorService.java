package com.woody.moment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.woody.moment.model.StatDataStruct;
import com.woody.moment.ui.MomentAlertActivity;
import com.woody.moment.ui.SplashActivity;

/**
 * Created by john on 6/3/16.
 */
public class MomentMonitorService extends Service {
    private final static String TAG = MomentMonitorService.class.getSimpleName();

    /**
     * Callback for update data
     *
     * @see SplashActivity
     */
    UiCallback mCallBack = null;

    private MonitorBinder mBinder = new MonitorBinder();

    public MomentMonitorService() {
        Log.v(TAG, "MomentMonitorService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private StatDataStruct mStats = StatDataStruct.getStatDataInstance();

    public class MonitorBinder extends Binder {
        public MomentMonitorService getService() {
            return MomentMonitorService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case Intent.ACTION_SCREEN_ON:
                    mStats.increaseScreenOnCount();
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    mStats.increaseScreenOffCount();
                    break;
                case Intent.ACTION_USER_PRESENT:
                    mStats.increaseUserPresentCount();
                    if (mStats.isAboveThreshold()) {
                        startAlertActivity();
                    }
                    break;
                default:
                    return;
            }
            if (mCallBack != null) {
                mCallBack.onUpdate();
            }

        }
    };

    final private void startAlertActivity() {
        Intent alertIntent = new Intent();
        alertIntent.setAction("moment.action.SHOW_ALERT");
        alertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alertIntent.setClass(getApplicationContext(), MomentAlertActivity.class);
        startActivity(alertIntent);
    }

    final public void setCallBack(UiCallback callBack) {
        mCallBack = callBack;
    }

    public interface UiCallback {
        void onUpdate();
    }
}

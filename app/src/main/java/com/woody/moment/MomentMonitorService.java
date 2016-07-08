package com.woody.moment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.UserHandle;
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

    private PendingIntent mPendingIntent;
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

        Intent intent = new Intent(this, MomentAlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
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
                    cancelAlarm();
                    break;
                case Intent.ACTION_USER_PRESENT:
                    mStats.increaseUserPresentCount();
                    if (mStats.isAboveThreshold()) {
                        startAlertActivity();
                    } else {
                        setAlarm();
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

    void setAlarm() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long duration = StatDataStruct.getStatDataInstance().getAlertTime();
        long firstTime = SystemClock.elapsedRealtime() + duration;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, firstTime, duration, mPendingIntent);
    }

    void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(mPendingIntent);
    }
}

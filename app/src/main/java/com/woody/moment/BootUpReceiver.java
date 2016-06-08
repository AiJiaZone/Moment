package com.woody.moment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by john on 6/3/16.
 */
public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        startMonitorService(context);
    }

    final private void startMonitorService(final Context context) {
        Intent monitorService = new Intent();
        monitorService.setAction("moment.action.monitor.START");
        monitorService.setClass(context.getApplicationContext(), MomentMonitorService.class);
        context.startService(monitorService);
    }
}

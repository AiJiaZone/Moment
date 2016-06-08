package com.woody.moment;

import android.app.Application;
import android.content.Intent;

/**
 * Created by john on 6/6/16.
 */
public class MomentApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startMonitorService();
    }

    final private void startMonitorService() {
        Intent monitorService = new Intent();
        monitorService.setAction("moment.action.monitor.START");
        monitorService.setClass(this, MomentMonitorService.class);
        startService(monitorService);
    }
}

package com.woody.moment.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.woody.moment.ui.MomentSetting;

/**
 * Created by john on 7/7/16.
 */
public class SettingHelper {
    public static final int MODE_NORMAL = 1;
    public static final int MODE_EXTREME = MODE_NORMAL << 1;
    public static final int MODE_TIME_TICKER = MODE_NORMAL << 2;
    public static final int MODE_DEFAULT = MODE_NORMAL;
    public static final String MODE_TAG = "mode_tag";

    private static final String SETTING_PREF = "settings_pref";
    private static final String THRESHOLD = "threshold";
    private static final String DURATION = "duration";

    private static SettingHelper sInstance = null;
    private static Context mAppContext;

    static SharedPreferences mPreferences;

    private SettingHelper() {
    }

    public static SettingHelper getHelper(Context context) {
        if (sInstance == null) {
            mAppContext = context.getApplicationContext();
            sInstance = new SettingHelper();
            mPreferences = mAppContext.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);

            mPreferences.registerOnSharedPreferenceChangeListener(mListener);
        }

        return sInstance;
    }

    public final void saveSettings(MomentSetting.SettingData data) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(THRESHOLD, data.threshold);
        editor.putInt(DURATION, data.duration);
        editor.commit();

//        StatDataStruct.getStatDataInstance().setAlertThreshold(data.threshold);
//        StatDataStruct.getStatDataInstance().setAlertDuration(data.duration);
    }

    public final int getAlertThreshold() {
        return mPreferences.getInt(THRESHOLD, StatDataStruct.DEFAULT_ALERT_THRESHOLD);
    }

    public final int getAlertDuaration() {
        return mPreferences.getInt(DURATION, StatDataStruct.DEFAULT_ALERT_THRESHOLD);
    }

    private static SharedPreferences.OnSharedPreferenceChangeListener mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            int threshold = sharedPreferences.getInt(key, StatDataStruct.DEFAULT_ALERT_THRESHOLD);
            if (THRESHOLD.equals(key)) {
                StatDataStruct.getStatDataInstance().setAlertThreshold(threshold);
            } else if (DURATION.equals(key)) {
                StatDataStruct.getStatDataInstance().setAlertDuration(threshold);
            }
        }
    };
}

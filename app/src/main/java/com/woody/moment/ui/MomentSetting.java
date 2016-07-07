package com.woody.moment.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.woody.moment.R;
import com.woody.moment.model.SettingHelper;
import com.woody.moment.model.StatDataStruct;

/**
 * Created by john on 6/13/16.
 */
public class MomentSetting extends AppCompatActivity {

    private EditText mThresholdEditor;
    private EditText mDurationEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mThresholdEditor = (EditText) findViewById(R.id.alert_threshold);
        mThresholdEditor.setText(String.valueOf(SettingHelper.getHelper(this).getAlertThreshold()));
        mDurationEditor = (EditText) findViewById(R.id.alert_duration);
        mDurationEditor.setText(String.valueOf(SettingHelper.getHelper(this).getAlertDuaration()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
    }

    private void saveSettings() {
        String threshold = mThresholdEditor.getText().toString();
        String duration = mDurationEditor.getText().toString();
        SettingData data = new SettingData();
        if (TextUtils.isEmpty(threshold)) {
            data.threshold = StatDataStruct.DEFAULT_ALERT_THRESHOLD;
        } else {
            data.threshold = Integer.valueOf(threshold);
        }
        if (TextUtils.isEmpty(duration)) {
            data.duration = StatDataStruct.DEFAULT_ALERT_THRESHOLD;
        } else {
            data.duration = Integer.valueOf(duration);
        }

        SettingHelper.getHelper(this).saveSettings(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SettingData {
        public int threshold;
        public int duration;
    }
}

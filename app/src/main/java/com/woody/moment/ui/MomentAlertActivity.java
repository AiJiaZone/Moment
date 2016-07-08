package com.woody.moment.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.woody.moment.LockReceiver;
import com.woody.moment.R;
import com.woody.moment.model.SettingHelper;
import com.woody.moment.model.StatDataStruct;

/**
 * Created by john on 6/7/16.
 */
public class MomentAlertActivity extends Activity {

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName componentName;
    private final int MY_REQUEST_CODE = 1001;
    private StatDataStruct mStats = StatDataStruct.getStatDataInstance();

    private AlertDialog mDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (componentName == null) {
            componentName = new ComponentName(this, LockReceiver.class);
        }
        showDialog();
    }

    private String modeToString(int mode) {
        switch (mode) {
//            case SettingHelper.MODE_DEFAULT:
//                return "MODE_DEFAULT()"+SettingHelper.MODE_DEFAULT;
            case SettingHelper.MODE_NORMAL:
                return "MODE_DEFAULT()"+SettingHelper.MODE_NORMAL;
            case SettingHelper.MODE_EXTREME:
                return "MODE_DEFAULT()"+SettingHelper.MODE_EXTREME;
            case SettingHelper.MODE_TIME_TICKER:
                return "MODE_DEFAULT()"+SettingHelper.MODE_TIME_TICKER;
            default:
                throw new IllegalArgumentException();
        }
    }
    @TargetApi(17)
    final private void showDialog() {
        if (mDialog == null) {
            Intent intent = getIntent();
            int mode = intent.getIntExtra(SettingHelper.MODE_TAG, SettingHelper.MODE_DEFAULT);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
            Log.v("wujiujiu", "mode = "+modeToString(mode));
            if (mode != SettingHelper.MODE_TIME_TICKER) {
                builder.setMessage(String.format(getString(R.string.present_alert_content), mStats.getUserPresentCount()));
            } else {
                builder.setMessage(String.format(getString(R.string.present_alert_ticker), mStats.getAlertTime()));
            }
            builder.setOnDismissListener(mDismissListener)
                    .setPositiveButton(android.R.string.yes, mClickListener)
                    .setCancelable(false);

            if (mDevicePolicyManager.isAdminActive(componentName) && mode != SettingHelper.MODE_EXTREME) {
                builder.setNegativeButton(android.R.string.cancel, mClickListener);
            }

            mDialog = builder.create();
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    final private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

    final private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            finish();
        }
    };

    final private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (mDevicePolicyManager.isAdminActive(componentName)) {
                    mDevicePolicyManager.lockNow();
                    dismissDialog();
                } else {
                    activeAdminManager();
                }
            } else {
                dismissDialog();
                finish();
            }
        }
    };

    final private void activeAdminManager() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.admin_activate_hint));
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK) {
            mDevicePolicyManager.lockNow();
            dismissDialog();
            finish();
        } else {
            activeAdminManager();
        }
    }
}

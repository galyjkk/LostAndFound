package com.galy.lostandfound;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
public class MyPostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_post);


//        查询电话号码
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//
//        String deviceid = tm.getDeviceId();
//        Log.e(deviceid,"1");
//        String tel = tm.getLine1Number();
//        Log.e(tel,"2");
//        String imei = tm.getSimSerialNumber();
//        Log.e(imei,"3");
//        String imsi = tm.getSubscriberId();
//        Log.e(imei,"4");
    }
}

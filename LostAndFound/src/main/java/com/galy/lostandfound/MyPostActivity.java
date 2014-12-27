package com.galy.lostandfound;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class MyPostActivity extends Activity {

    private BootstrapButton toLoginBtn;

    private final String ACTION_NAME = "sendBroadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_post);

        registerBoradcastReceiver();

        toLoginBtn = (BootstrapButton) findViewById(R.id.to_login_my_post);

        //to login
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MyPostActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private BroadcastReceiver onPostReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_NAME)) {
                Toast.makeText(MyPostActivity.this, "MyPostActivity接收广播", Toast.LENGTH_SHORT).show();

                setContentView(R.layout.activity_my_post_login);
                //unregisterReceiver(onPostReceiver);

            }
        }
    };

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(onPostReceiver, myIntentFilter);
    }
}

package com.galy.lostandfound;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class MyMessagesActivity extends Activity {

    private BootstrapButton toLoginBtn;

    private final String ACTION_LOGIN = "loginSuccess";
    private final String ACTION_LOGOUT = "logoutSuccess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        registerBroadcastReceiver();
        visitorLayout();
    }

    private BroadcastReceiver onMessagesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_LOGIN)) {
                userLayout();
            }
            if (action.equals(ACTION_LOGOUT)) {
                visitorLayout();
            }
        }
    };

    public void registerBroadcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_LOGIN);
        myIntentFilter.addAction(ACTION_LOGOUT);
        //注册广播
        registerReceiver(onMessagesReceiver, myIntentFilter);
    }

    private void visitorLayout() {
        setContentView(R.layout.activity_my_messages);

        toLoginBtn = (BootstrapButton) findViewById(R.id.to_login_my_messages);

        //to login
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MyMessagesActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLayout() {
        setContentView(R.layout.activity_my_messages_login);
    }
}

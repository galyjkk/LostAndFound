package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class MyMessagesActivity extends Activity {

    private BootstrapButton toLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
}

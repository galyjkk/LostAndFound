package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.galy.lostandfound.service.AsynchronousHttpClient;

import org.json.JSONArray;

import java.util.HashMap;


public class LoginActivity extends Activity implements AsynchronousHttpClient.ILoadListener{

    private ImageButton cancelBtn;
    private BootstrapButton signInBtn;
    private BootstrapButton signUpBtn;
    private BootstrapEditText username;
    private BootstrapEditText password;

    private static final String BASE_URL = "http://182.92.170.217:8000/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        cancelBtn = (ImageButton) findViewById(R.id.cancel_login);
        signInBtn = (BootstrapButton) findViewById(R.id.sign_in_login);
        signUpBtn = (BootstrapButton) findViewById(R.id.sign_up_login);
        username = (BootstrapEditText) findViewById(R.id.username_login);
        password = (BootstrapEditText) findViewById(R.id.password_login);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = username.getText().toString();
                String p = password.getText().toString();

                if(u != "" && p != "") {
                    HashMap<String, String> signInInfo = new HashMap<String, String>();
                    signInInfo.put("username", u);
                    signInInfo.put("password", p);

                    signIn(signInInfo);
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(toSignUpIntent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //登陆成功后发送广播切换布局
                Intent mIntent = new Intent("sendBroadcast");
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);

                LoginActivity.this.finish();

            }
        });
    }

    public void signIn(HashMap signInInfo) {
        new AsynchronousHttpClient(BASE_URL, this).post(signInInfo);
        Toast.makeText(LoginActivity.this, "send", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loading() {

    }

    @Override
    public void complete(JSONArray j) {
        Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
    }
}
